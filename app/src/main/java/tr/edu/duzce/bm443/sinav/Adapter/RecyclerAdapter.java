package tr.edu.duzce.bm443.sinav.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tr.edu.duzce.bm443.sinav.Common;
import tr.edu.duzce.bm443.sinav.MainActivity;
import tr.edu.duzce.bm443.sinav.Models.Article;
import tr.edu.duzce.bm443.sinav.NewsItemClickListener;
import tr.edu.duzce.bm443.sinav.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<Article> articleList = new ArrayList<>();
    private NewsItemClickListener newsItemClickListener;

    public RecyclerAdapter(Context context, List<Article> articleList, NewsItemClickListener newsItemClickListener) {
        this.context = context;
        this.articleList = articleList;
        this.newsItemClickListener = newsItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_news_item, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Article article = articleList.get(i);
        myViewHolder.txtContent.setText(article.getContent());
        myViewHolder.txtTitle.setText(article.getTitle());
        myViewHolder.txtDate.setText(Common.dateFormatter(article.getPublishedAt()));
        if (article.getSource()!= null && article.getSource().getName()!=null){
            myViewHolder.txtSource.setText(article.getSource().getName());
        }

        Picasso.with(context).load(article.getUrlToImage()).into(myViewHolder.imgNews);

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgNews;
        TextView txtTitle, txtSource, txtDate, txtContent;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            imgNews = itemView.findViewById(R.id.imgNewsImg);
            txtTitle = itemView.findViewById(R.id.txtNewsTitle);
            txtSource = itemView.findViewById(R.id.txtNewsSource);
            txtDate = itemView.findViewById(R.id.txtNewsDate);
            txtContent = itemView.findViewById(R.id.txtNewsContent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    newsItemClickListener.onClick(pos, MainActivity.NEWS_TYPE_RECY);
                }
            });
        }


    }
}
