package com.ymovie.app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.ymovie.app.R;
import com.ymovie.app.databinding.ActivityMainBinding;
import com.ymovie.app.ui.home.HomeFragment;
import com.ymovie.app.ui.search.SearchFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment.newInstance());
        }

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.item_1) {
                    replaceFragment(HomeFragment.newInstance());
                } else if (item.getItemId() == R.id.item_2) {
                    replaceFragment(SearchFragment.newInstance());
                }

                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.commit();
    }
}
