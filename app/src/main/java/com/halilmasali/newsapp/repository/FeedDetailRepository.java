package com.halilmasali.newsapp.repository;

import androidx.annotation.NonNull;
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

    public LiveData<FeedDetailModel> getNewsDetail(String url) {
        // If the URL is empty or null, use a default URL for fetching feed details
        if (url == null || url.isEmpty()) {
            url = "https://demo6216114.mockable.io/feed_detail";
        }
        // Extract the detail URL from the full URL
        String detailUrl = url.substring(url.lastIndexOf("/") + 1);

        MutableLiveData<FeedDetailModel> newsDetailData = new MutableLiveData<>();
        feedDetailData.getNewsDetail(detailUrl).enqueue(new Callback<FeedDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<FeedDetailModel> call, @NonNull Response<FeedDetailModel> response) {
                if (response.isSuccessful()) {
                    newsDetailData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FeedDetailModel> call, @NonNull Throwable t) {
                newsDetailData.setValue(null);
            }
        });
        return newsDetailData;
    }
}
