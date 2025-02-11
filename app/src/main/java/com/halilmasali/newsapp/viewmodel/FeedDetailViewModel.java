package com.halilmasali.newsapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.halilmasali.newsapp.data.model.feeddetail.FeedDetailModel;
import com.halilmasali.newsapp.repository.FeedDetailRepository;

public class FeedDetailViewModel extends ViewModel {
    private final LiveData<FeedDetailModel> newsDetail;

    public FeedDetailViewModel() {
        FeedDetailRepository repository = new FeedDetailRepository();
        newsDetail = repository.getNewsDetail();
    }

    public LiveData<FeedDetailModel> getNewsDetail() {
        return newsDetail;
    }

}
