package com.halilmasali.newsapp.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.halilmasali.newsapp.R;
import com.halilmasali.newsapp.databinding.ActivityMainBinding;
import com.halilmasali.newsapp.viewmodel.FeedDetailViewModel;
import com.halilmasali.newsapp.viewmodel.FeedViewModel;

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
}