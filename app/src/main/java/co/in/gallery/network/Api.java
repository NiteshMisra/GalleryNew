package co.in.gallery.network;

import co.in.gallery.response.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("services/rest/")
    Call<ApiResponse> getImages(
      @Query("method") String method,
      @Query("per_page") Integer perPage,
      @Query("page") Integer page,
      @Query("api_key") String apiKey,
      @Query("format") String format,
      @Query("nojsoncallback") Integer noJsonCallBack,
      @Query("extras") String extras
    );

    @GET("services/rest/")
    Call<ApiResponse> searchText(
            @Query("method") String method,
            @Query("text") String text,
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("nojsoncallback") Integer noJsonCallBack,
            @Query("extras") String extras
    );


}
