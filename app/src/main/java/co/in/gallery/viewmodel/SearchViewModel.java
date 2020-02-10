package co.in.gallery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import co.in.gallery.model.ImageModel;
import co.in.gallery.repository.SearchRepository;

public class SearchViewModel extends ViewModel {

    private SearchRepository searchRepository;

    public SearchViewModel(){
        searchRepository = new SearchRepository();
    }

    public LiveData<List<ImageModel>> getSearchList(String text){
        return searchRepository.searchCall(text);
    }

}
