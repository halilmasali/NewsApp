package com.halilmasali.newsapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.MobileAds;
import com.halilmasali.newsapp.R;
import com.halilmasali.newsapp.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeAdmob();
        setSupportActionBar(binding.topAppBar);
        // Navigation controller
        navigation();
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

    // Initialize Admob for ads
    private void initializeAdmob() {
        MobileAds.initialize(this, initializationStatus -> Log.d("Admob", initializationStatus.toString()));
    }
}