package co.in.gallery.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;
import co.in.gallery.R;
import co.in.gallery.activity.MainActivity;
import co.in.gallery.adapter.ImageAdapter;
import co.in.gallery.helper.ProgressListener;
import co.in.gallery.model.ImageModel;
import co.in.gallery.viewmodel.HomeViewModel;

@SuppressWarnings("deprecation")
public class HomeFragment extends Fragment implements ProgressListener {

    private HomeViewModel viewModel;
    private ImageAdapter imageAdapter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private int listSize = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(this.getActivity())).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        coordinatorLayout = view.findViewById(R.id.home_coordinator);
        progressBar = view.findViewById(R.id.home_progress);
        refreshLayout = view.findViewById(R.id.home_refresh);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setHasFixedSize(true);
        imageAdapter = new ImageAdapter(getActivity());
        viewModel.loadData(this).observe(Objects.requireNonNull(getActivity()), new Observer<PagedList<ImageModel>>() {
            @Override
            public void onChanged(PagedList<ImageModel> imageModels) {
                if (imageModels == null){
                    Toast.makeText(getActivity(),"Some error Occurred, Try again later",Toast.LENGTH_SHORT).show();
                    return;
                }
                imageAdapter.submitList(imageModels);
                listSize += imageModels.size();
                hideProgress();
            }
        });
        recyclerView.setAdapter(imageAdapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                observeList();
            }
        });
    }

    private void showSnackBar() {
        snackbar = Snackbar.make(coordinatorLayout,"No Internet Connection",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternet()){
                    snackbar.dismiss();
                }else{
                    showSnackBar();
                }
            }
        }).setActionTextColor(Color.RED);
        snackbar.show();
    }

    private void observeList() {
        if (isInternet()){
            imageAdapter = new ImageAdapter(getActivity());
            viewModel.refreshData(this).observe(Objects.requireNonNull(getActivity()), new Observer<PagedList<ImageModel>>() {
                @Override
                public void onChanged(PagedList<ImageModel> imageModels) {
                    if (imageModels == null){
                        Toast.makeText(getActivity(),"Some error Occurred, Try again later",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    refreshLayout.setRefreshing(false);
                    imageAdapter.submitList(imageModels);
                    listSize += imageModels.size();
                }
            });
            recyclerView.setAdapter(imageAdapter);
        }else{
            refreshLayout.setRefreshing(false);
            showSnackBar();
            this.hideProgress();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!isInternet()){
                    showSnackBar();
                }else{
                    if (snackbar != null){
                        snackbar.dismiss();
                        if (listSize <= 20){
                            observeList();
                        }
                    }
                }
            }
        };
        if (this.getActivity() instanceof MainActivity){
            ((MainActivity)this.getActivity()).registerReceivers(broadcastReceiver,intentFilter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.getActivity() instanceof MainActivity){
            ((MainActivity)this.getActivity()).unRegisterReceivers(broadcastReceiver);
        }
    }

    private Boolean isInternet(){
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(this.getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;
    }

    @Override
    public void showProgress() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgress() {
        try {
            Log.e("Progress Bar","hide");
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }catch (NullPointerException e){
            // it's use to remove Null Pointer Exception
        }
    }
}
