package com.halilmasali.newsapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.halilmasali.newsapp.data.model.feed.FeedModel;
import com.halilmasali.newsapp.repository.FeedRepository;

import java.util.List;

public class FeedViewModel extends ViewModel {
    private final LiveData<FeedModel> newsList;

    public FeedViewModel() {
        FeedRepository repository = new FeedRepository();
        newsList = repository.getNews();
    }

    public LiveData<FeedModel> getNewsList() {
        return newsList;
    }
}
