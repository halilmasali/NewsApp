package com.halilmasali.newsapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.halilmasali.newsapp.R;
import com.halilmasali.newsapp.databinding.ActivityMainBinding;
import com.halilmasali.newsapp.viewmodel.FeedDetailViewModel;
import com.halilmasali.newsapp.viewmodel.FeedViewModel;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FeedViewModel newsViewModel;
    FeedDetailViewModel newsDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.topAppBar);
        // Navigation controller
        navigation();

        newsViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        newsViewModel.getNewsList().observe(this, feedModel -> {
            // Update UI
            if (feedModel != null) {
                for (int i = 0; i < feedModel.items.size(); i++) {
                    Log.d("responseNews", feedModel.items.get(i).detailMiniContent);
                }
            }
        });

        newsDetailViewModel = new ViewModelProvider(this).get(FeedDetailViewModel.class);
        newsDetailViewModel.getNewsDetail().observe(this, feedDetailModel -> {
            // Update UI
            if (feedDetailModel != null) {
                Log.d("responseNewsDetail", feedDetailModel.detail.content);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

    private void navigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.detailFragment) {
                // Show back button
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                binding.logoImageView.setVisibility(View.GONE);
            } else {
                // Hide back button
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                binding.logoImageView.setVisibility(View.VISIBLE);
            }
        });
    }
}