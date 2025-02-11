package com.halilmasali.newsapp.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.halilmasali.newsapp.R;
import com.halilmasali.newsapp.repository.FeedDetailRepository;
import com.halilmasali.newsapp.viewmodel.FeedDetailViewModel;
import com.halilmasali.newsapp.viewmodel.FeedViewModel;

public class MainActivity extends AppCompatActivity {

    FeedViewModel newsViewModel;
    FeedDetailViewModel newsDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        newsViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        newsViewModel.getNewsList().observe(this, feedModel -> {
            // Update UI
            if (feedModel != null) {
                for(int i = 0; i < feedModel.items.size(); i++){
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
}