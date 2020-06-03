package tr.edu.duzce.bm443.sinav.Retrofit;
import retrofit2.Call;
import retrofit2.http.GET;

import tr.edu.duzce.bm443.sinav.Models.News;

public interface IRetrofitApiCall {

    //method for getting all usa headline news from server
    @GET("v2/top-headlines?country=us&apiKey="+RetrofitClient.API_KEY)
    Call<News> getTrHeadLine();

    //method for getting all turkey headline news from server
    @GET("v2/top-headlines?country=tr&apiKey="+RetrofitClient.API_KEY)
    Call<News> getHeadLine();



}
