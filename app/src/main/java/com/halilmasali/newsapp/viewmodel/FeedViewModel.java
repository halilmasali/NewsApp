package com.halilmasali.newsapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.halilmasali.newsapp.data.model.feed.FeedModel;
import com.halilmasali.newsapp.data.network.Resource;
import com.halilmasali.newsapp.repository.FeedRepository;

public class FeedViewModel extends ViewModel {
    FeedRepository repository;

    public FeedViewModel() {
        repository = new FeedRepository();
    }

    public LiveData<Resource<FeedModel>> getNewsList() {
        return repository.getNews();
    }
}
