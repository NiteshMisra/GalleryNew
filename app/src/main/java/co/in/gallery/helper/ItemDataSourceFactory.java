package co.in.gallery.helper;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import co.in.gallery.model.ImageModel;

public class ItemDataSourceFactory extends DataSource.Factory<Integer, ImageModel> {

    private MutableLiveData<PageKeyedDataSource<Integer,ImageModel>> itemLiveDataSource = new MutableLiveData<>();
    private Context context;
    private ProgressListener progressListener;

    public ItemDataSourceFactory(Context context, ProgressListener progressListener) {
        this.context = context;
        this.progressListener = progressListener;
    }

    @Override
    public DataSource<Integer, ImageModel> create() {
        ImageDataSource imageDataSource = new ImageDataSource(context,progressListener);
        itemLiveDataSource.postValue(imageDataSource);
        return imageDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, ImageModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
