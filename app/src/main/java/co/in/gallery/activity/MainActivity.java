package co.in.gallery.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import co.in.gallery.R;
import co.in.gallery.fragment.HomeFragment;
import co.in.gallery.fragment.SearchFragment;
import co.in.gallery.helper.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavView();
    }

    private void bottomNavView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }

                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        fragment = new SearchFragment();
                        break;
                    default:
                        fragment = new HomeFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment,fragment.getTag())
                        .commit();

                return true;
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new HomeFragment())
                .commit();
    }

    public void registerReceivers(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter){
        this.registerReceiver(broadcastReceiver,intentFilter);
    }

    public void unRegisterReceivers(BroadcastReceiver broadcastReceiver){
        this.unregisterReceiver(broadcastReceiver);
    }


}
