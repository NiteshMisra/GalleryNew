package co.in.gallery.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import java.util.List;
import java.util.Objects;
import co.in.gallery.database.ImageDao;
import co.in.gallery.database.ImageDatabase;
import co.in.gallery.model.ImageModel;
import co.in.gallery.network.RetrofitClient;
import co.in.gallery.response.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class ImageDataSource extends PageKeyedDataSource<Integer, ImageModel> {

    public static final int PAGE_SIZE = 20;
    private static final int FIRST_PAGE = 1;
    private Context context;
    private ImageDao imageDao;
    private ProgressListener progressListener;

    ImageDataSource(Context context, ProgressListener progressListener) {
        this.context = context;
        ImageDatabase imageDatabase = ImageDatabase.getInstance(context);
        imageDao = imageDatabase.imageDao();
        this.progressListener = progressListener;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ImageModel> callback) {
        if (isInternet()){
            Call<ApiResponse> call = RetrofitClient.getInstance().getApi().getImages("flickr.photos.getRecent",PAGE_SIZE,FIRST_PAGE,"6f102c62f41998d151e5a1b48713cf13","json",1,"url_s");
            call.enqueue(new Callback<ApiResponse>(){

                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                    if (response.body() != null){
                        if (response.body().getPhotos().getImageList().size() > 0){
                            new insertData(imageDao).execute(response.body().getPhotos().getImageList());
                            callback.onResult(response.body().getPhotos().getImageList(),null,FIRST_PAGE + 1);
                            progressListener.hideProgress();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                }
            });
        }else{
            if (imageDao.isDataPresent() > 0){
                callback.onResult(imageDao.getImages(),null,FIRST_PAGE + 1);
                Log.e("db",imageDao.getImages().get(0).getUrl());
                progressListener.hideProgress();
            }
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ImageModel> callback) {
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ImageModel> callback) {
        Log.e("Load ","after1");
        if (isInternet()){
            Log.e("Load ","after2");
            progressListener.showProgress();
            Call<ApiResponse> call = RetrofitClient.getInstance().getApi().getImages("flickr.photos.getRecent",PAGE_SIZE,params.key,"6f102c62f41998d151e5a1b48713cf13","json",1,"url_s");
            call.enqueue(new Callback<ApiResponse>(){

                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                    if (response.body() != null){
                        progressListener.hideProgress();
                        if (response.body().getPhotos().getImageList().size() > 0){
                            new insertData(imageDao).execute(response.body().getPhotos().getImageList());
                            callback.onResult(response.body().getPhotos().getImageList(),params.key + 1);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                }
            });
        }else{
            Log.e("Load ","after3");
        }
    }

    private Boolean isInternet(){
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(context).getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;
    }

    private class insertData extends AsyncTask<List<ImageModel>,Void,Void> {

        private ImageDao imageDao;

        public insertData(ImageDao imageDao) {
            this.imageDao = imageDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<ImageModel>... lists) {
            imageDao.deleteAll();
            imageDao.insert(lists[0]);
            return null;
        }
    }


}
