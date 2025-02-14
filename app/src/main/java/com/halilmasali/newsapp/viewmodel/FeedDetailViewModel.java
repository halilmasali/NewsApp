package com.halilmasali.newsapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.halilmasali.newsapp.data.model.feeddetail.FeedDetailModel;
import com.halilmasali.newsapp.repository.FeedDetailRepository;

public class FeedDetailViewModel extends ViewModel {
    FeedDetailRepository repository;

    public FeedDetailViewModel() {
        repository = new FeedDetailRepository();
    }

    public LiveData<FeedDetailModel> getNewsDetail(String url) {
        return repository.getNewsDetail(url);
    }

}
