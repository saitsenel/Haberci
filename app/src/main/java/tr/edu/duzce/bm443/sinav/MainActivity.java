package tr.edu.duzce.bm443.sinav;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.edu.duzce.bm443.sinav.Adapter.RecyclerAdapter;
import tr.edu.duzce.bm443.sinav.Models.Article;
import tr.edu.duzce.bm443.sinav.Models.News;
import tr.edu.duzce.bm443.sinav.Retrofit.IRetrofitApiCall;
import tr.edu.duzce.bm443.sinav.Retrofit.RetrofitClient;
import tr.edu.duzce.bm443.sinav.Sqlite.MySqliteHandler;


public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, NewsItemClickListener {

    public static int NEWS_TYPE_RECY = 2;
    public static int NEWS_TYPE_HEAD = 1;
    SliderLayout slider;
    RecyclerView recyclerView;
    private List<Article> headLineArticleList =new ArrayList<>();
    private MySqliteHandler mySqliteHandler;
    private LinearLayoutManager layoutManager;
    private NewsItemClickListener newsItemClickListener;
    private RecyclerAdapter adapter;
    private List<Article> recyclerArticleList = new ArrayList<>();
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initiate all xml view from that method
        initView();

        //Create sqlite handler object for different sqlite operation
        mySqliteHandler = new MySqliteHandler(this);


        if(Common.isNetworkConnected(this)){ // check if data connection or wifi is enable
            //if internet connection enable then we request to server for data or news
            getNewsHeader(); //get news from server for header slide
           // getRecyclerItem(); // get news from server for recycler view item
        }else {

            //internet connection not available
            //so we here request to offline sqlite database for both slider and recycler

            //sqlite data only available only when the app at least one time data sync from server
            getNewsHeadeeFromSqlite();
            //getRecyclerItemFromSqlite();
        }
    }

    private void getRecyclerItemFromSqlite() {
        recyclerArticleList = mySqliteHandler.getAllRecyArticles();

        if (recyclerArticleList.size()>0){
            addRecycler(recyclerArticleList);
        }
    }

        private void getRecyclerItem() {
            //here we did, same things that we did with getting headline news
            IRetrofitApiCall iRetrofitApiCall = RetrofitClient.getRetrofit().create(IRetrofitApiCall.class);
            iRetrofitApiCall.getTrHeadLine()
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            if (response.body() != null){
                                News news = response.body();
                                //if content of request not null and  there at least one news
                                if (news.getArticles()!=null && news.getArticles().size() >0){
                                    //now adding headline news article to slider
                                    recyclerArticleList = news.getArticles();
                                    addRecycler(news.getArticles());
                                    addRecyclerForOffLine(recyclerArticleList);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {
                            getRecyclerItemFromSqlite();
                        }
                    });

        }

    private void addRecyclerForOffLine(List<Article> articleList) {
        mySqliteHandler.addRecyArticleList(articleList);
    }

    private void addRecycler(List<Article> articles) {
        recyclerArticleList = articles;
        adapter = new RecyclerAdapter(MainActivity.this, recyclerArticleList, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void getNewsHeadeeFromSqlite() {
        //we call getAllHeadline() method for getting all sqlite headline news
        //that we saved in last server call
        recyclerArticleList = mySqliteHandler.getAllHeadLine();

        if (recyclerArticleList.size()>0){
            //if news size is grater than 0 then pss the list of headline news to add slider
            addHeadLineToSlider(recyclerArticleList);
            addRecycler(recyclerArticleList);
        }

    }

    private void getNewsHeader() {

        //First create RestApi interface to call method
        IRetrofitApiCall iRetrofitApiCall = RetrofitClient.getRetrofit().create(IRetrofitApiCall.class);
        iRetrofitApiCall.getHeadLine() // here we call getHeadline() method for getting headline news from server
                .enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {
                        //of body not null then we execute more

                        if (response.body() != null){
                            News news = response.body();
                            //if content of request not null and  there at least one news
                            if (news.getArticles()!=null && news.getArticles().size() >0){
                                //now adding headline news article to slider
                                addHeadLineToSlider(news.getArticles());
                                addRecycler(news.getArticles());
                                //here we add all of our news comes from server to sqlite
                                //for offline use.
                                addHeadLineForOffLine(news.getArticles());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {
                        //If data from server failed then we load it from sqlite
                        getNewsHeadeeFromSqlite();
                    }
                });
    }

    private void addHeadLineForOffLine(List<Article> articleList) {
        mySqliteHandler.addArticleList(articleList);
    }

    private void addHeadLineToSlider(List<Article> articles) {
        //here we add all headline news to slider on by one by using for each loop
        int pos = 0;
        int size = articles.size()-1;

        for (int i= size; i>=0; i--) { // for each loop
            headLineArticleList.add(articles.get(i));
        }

        for (Article article: headLineArticleList){
            TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                .description(article.getTitle())
                .image(article.getUrlToImage())
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener(MainActivity.this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("Date",article.getPublishedAt());
                textSliderView.getBundle()
                        .putInt("Pos", pos);
                pos++;

                slider.addSlider(textSliderView);
        }

    }

    private void initView() {
        slider = findViewById(R.id.slider);
        recyclerView = findViewById(R.id.recyclerView);
        
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(10000);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int pos = slider.getBundle().getInt("Pos");
        onClick(pos, NEWS_TYPE_HEAD);
    }

    @Override
    protected void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onClick(int pos, int type) {
        //this function related to newsItemClickListener
        //and it trigger when both recycler item and slider item click
        //both click function pass cliked item position and news type, 1==NEWS_TYPE_RECY, 2==NEWS_TYPE_HEAD
        Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
        if (type==NEWS_TYPE_RECY){
            //if clck form recycler item  then we pass that full article (recyclerArticleList) object to ArticleActivity
            intent.putExtra("Article", recyclerArticleList.get(pos));
        }else {
            //if clck form slider item  then we pass that full article (headLineArticleList) object to ArticleActivity
            intent.putExtra("Article", headLineArticleList.get(pos));


        }

        //trigger activity switch
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
