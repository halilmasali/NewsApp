package com.halilmasali.newsapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.halilmasali.newsapp.data.model.feeddetail.FeedDetailModel;
import com.halilmasali.newsapp.data.network.IFeedDetailData;
import com.halilmasali.newsapp.data.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The `FeedDetailRepository` class serves as a data repository for fetching and managing feed data.
 * It abstracts the data access logic, providing a clean interface for the ViewModel layer to interact with.
 * This class utilizes Retrofit for network calls and LiveData for reactive data updates.
 */
public class FeedDetailRepository {
    private final IFeedDetailData feedDetailData;

    public FeedDetailRepository() {
        feedDetailData = RetrofitClient.getRetrofitInstance().create(IFeedDetailData.class);
    }

    public LiveData<FeedDetailModel> getNewsDetail() {
        MutableLiveData<FeedDetailModel> newsDetailData = new MutableLiveData<>();
        feedDetailData.getNewsDetail().enqueue(new Callback<FeedDetailModel>() {
            @Override
            public void onResponse(Call<FeedDetailModel> call, Response<FeedDetailModel> response) {
                if (response.isSuccessful()) {
                    newsDetailData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<FeedDetailModel> call, Throwable t) {
                newsDetailData.setValue(null);
            }
        });
        return newsDetailData;
    }
}
