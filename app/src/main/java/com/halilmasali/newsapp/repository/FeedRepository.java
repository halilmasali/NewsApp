package com.halilmasali.newsapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.halilmasali.newsapp.data.model.feed.FeedModel;
import com.halilmasali.newsapp.data.network.IFeedData;
import com.halilmasali.newsapp.data.network.Resource;
import com.halilmasali.newsapp.data.network.RetrofitClient;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FeedRepository is responsible for fetching and managing feed data from the network.
 * It acts as an intermediary between the data source (Retrofit API) and the ViewModel.
 * It handles network requests, processes responses, and provides LiveData to observe the data.
 */
public class FeedRepository {
    private final IFeedData feedData;

    public FeedRepository() {
        feedData = RetrofitClient.getRetrofitInstance().create(IFeedData.class);
    }

    public LiveData<Resource<FeedModel>> getNews() {
        MutableLiveData<Resource<FeedModel>> newsData = new MutableLiveData<>();

        // Set loading status
        newsData.setValue(Resource.loading());

        feedData.getNews().enqueue(new Callback<FeedModel>() {
            @Override
            public void onResponse(@NonNull Call<FeedModel> call, @NonNull Response<FeedModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newsData.setValue(Resource.success(response.body()));
                } else {
                    newsData.setValue(Resource.error("Sunucu Hatası: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<FeedModel> call, @NonNull Throwable t) {
                String errorMessage;
                if (t instanceof UnknownHostException) {
                    errorMessage = "İnternet bağlantınızı kontrol edin.";
                } else if (t instanceof SocketTimeoutException) {
                    errorMessage = "Bağlantı zaman aşımına uğradı!";
                } else {
                    errorMessage = "Bilinmeyen hata: " + t.getMessage();
                }
                newsData.setValue(Resource.error(errorMessage, null));
            }
        });
        return newsData;
    }
}
