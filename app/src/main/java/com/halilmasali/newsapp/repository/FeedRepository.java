package com.halilmasali.newsapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.halilmasali.newsapp.data.model.feed.FeedModel;
import com.halilmasali.newsapp.data.network.IFeedData;
import com.halilmasali.newsapp.data.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The `FeedRepository` class serves as a data repository for fetching and managing feed data.
 * It abstracts the data access logic, providing a clean interface for the ViewModel layer to interact with.
 * This class utilizes Retrofit for network calls and LiveData for reactive data updates.
 */
public class FeedRepository {
    private final IFeedData feedData;

    public FeedRepository() {
        feedData = RetrofitClient.getRetrofitInstance().create(IFeedData.class);
    }

    public LiveData<FeedModel> getNews() {
        MutableLiveData<FeedModel> newsData = new MutableLiveData<>();
        feedData.getNews().enqueue(new Callback<FeedModel>() {
            @Override
            public void onResponse(Call<FeedModel> call, Response<FeedModel> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<FeedModel> call, Throwable t) {
                newsData.setValue(null);
            }
        });
        return newsData;
    }
}
