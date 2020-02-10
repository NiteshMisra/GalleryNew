package co.in.gallery.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import co.in.gallery.R;
import co.in.gallery.adapter.SearchListAdapter;
import co.in.gallery.model.ImageModel;
import co.in.gallery.viewmodel.SearchViewModel;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("deprecation")
public class SearchFragment extends Fragment {

    private CompositeDisposable disposables = new CompositeDisposable();
    private SearchViewModel viewModel;
    private RecyclerView recyclerView;
    private SearchListAdapter adapter;

    public SearchFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(this.getActivity())).get(SearchViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SearchView searchView = Objects.requireNonNull(getView()).findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.searchRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Observable<String> observableQueryText = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(final ObservableEmitter<String> emitter) {

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }



                    @Override

                    public boolean onQueryTextChange(final String newText) {
                        if(!emitter.isDisposed()){
                            emitter.onNext(newText); // Pass the query to the emitter
                        }

                        return false;

                    }

                });

            }

        }).debounce(500, TimeUnit.MILLISECONDS)
          .subscribeOn(Schedulers.io());



        // Subscribe an Observer
        observableQueryText.subscribe(new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(String s) {
                if (!s.isEmpty()){
                    recyclerView.removeAllViewsInLayout();
                    sendRequestToServer(s);
                }else{
                   recyclerView.removeAllViewsInLayout();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

        });
    }

    private void sendRequestToServer(final String s) {
        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isInternet()){
                    viewModel.getSearchList(s).observe(Objects.requireNonNull(getActivity()), new androidx.lifecycle.Observer<List<ImageModel>>() {
                        @Override
                        public void onChanged(List<ImageModel> imageModels) {

                            List<ImageModel> filterList = new ArrayList<>();
                            for (ImageModel list : imageModels){
                                if (list.getTitle().toLowerCase().contains(s.toLowerCase())){
                                    filterList.add(0,list);
                                }else{
                                    filterList.add(list);
                                }
                            }
                            adapter = new SearchListAdapter(filterList,getActivity());
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }

    private Boolean isInternet(){
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
