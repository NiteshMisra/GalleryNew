package co.in.gallery.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import co.in.gallery.helper.ProgressListener;
import co.in.gallery.model.ImageModel;
import co.in.gallery.repository.HomeRepository;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<PagedList<ImageModel>> imagePagedList;
    private Application application;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<PagedList<ImageModel>> loadData(ProgressListener progressListener){
        HomeRepository repository = new HomeRepository(application,progressListener);
        imagePagedList = repository.getImagePagedList();
        return imagePagedList;
    }

    public LiveData<PagedList<ImageModel>> refreshData(ProgressListener progressListener){
        loadData(progressListener);
        return imagePagedList;
    }
}
