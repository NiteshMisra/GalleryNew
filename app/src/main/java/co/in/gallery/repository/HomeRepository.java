package co.in.gallery.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import java.util.List;
import co.in.gallery.database.ImageDao;
import co.in.gallery.database.ImageDatabase;
import co.in.gallery.helper.ImageDataSource;
import co.in.gallery.helper.ItemDataSourceFactory;
import co.in.gallery.helper.ProgressListener;
import co.in.gallery.model.ImageModel;
import co.in.gallery.network.Api;
import co.in.gallery.network.RetrofitClient;

@SuppressWarnings("ALL")
public class HomeRepository {

    private Api api;
    private ImageDao imageDao;
    private List<ImageModel> imageModels;
    private Context context;
    private LiveData<PagedList<ImageModel>> imagePagedList;
    private LiveData<PageKeyedDataSource<Integer,ImageModel>> imageDataSource;

    public LiveData<PagedList<ImageModel>> getImagePagedList() {
        return imagePagedList;
    }

    public HomeRepository(Context context, ProgressListener progressListener) {
        api = RetrofitClient.getInstance().getApi();
        ImageDatabase imageDatabase = ImageDatabase.getInstance(context);
        imageDao = imageDatabase.imageDao();
        this.context = context;

        ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory(context,progressListener);
        imageDataSource = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(ImageDataSource.PAGE_SIZE)
                        .build();

        imagePagedList = (new LivePagedListBuilder(itemDataSourceFactory,config)).build();

    }

}
