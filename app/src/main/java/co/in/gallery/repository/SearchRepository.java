package co.in.gallery.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import co.in.gallery.model.ImageModel;
import co.in.gallery.network.Api;
import co.in.gallery.network.RetrofitClient;
import co.in.gallery.response.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private Api api;

    public SearchRepository(){
        api = RetrofitClient.getInstance().getApi();
    }

    public MutableLiveData<List<ImageModel>> searchCall(String s){
        final MutableLiveData<List<ImageModel>> searchList = new MutableLiveData<>();

        Call<ApiResponse> call = api.searchText("flickr.photos.getRecent",s,"6f102c62f41998d151e5a1b48713cf13","json",1,"url_s");
        call.enqueue(new Callback<ApiResponse>(){

            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() != null){
                    if (response.body().getPhotos().getImageList().size() > 0){
                        searchList.setValue(response.body().getPhotos().getImageList());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
            }
        });

        return searchList;
    }

}
