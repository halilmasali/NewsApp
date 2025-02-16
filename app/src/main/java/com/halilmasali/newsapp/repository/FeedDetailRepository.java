package com.halilmasali.newsapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.halilmasali.newsapp.data.model.feeddetail.FeedDetailModel;
import com.halilmasali.newsapp.data.network.IFeedDetailData;
import com.halilmasali.newsapp.data.network.Resource;
import com.halilmasali.newsapp.data.network.RetrofitClient;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository class responsible for fetching and managing feed detail data.
 * <p>
 * This class interacts with the {@link IFeedDetailData} interface to retrieve feed detail information
 * from a remote server using Retrofit. It handles network requests, responses, and potential errors,
 * providing a clean and structured way to access feed detail data.
 */
public class FeedDetailRepository {
    private final IFeedDetailData feedDetailData;

    public FeedDetailRepository() {
        feedDetailData = RetrofitClient.getRetrofitInstance().create(IFeedDetailData.class);
    }

    public LiveData<Resource<FeedDetailModel>> getNewsDetail(String url) {
        // If the URL is empty or null, use a default URL for fetching feed details
        if (url == null || url.isEmpty()) {
            url = "https://demo6216114.mockable.io/feed_detail";
        }
        // Extract the detail URL from the full URL
        String detailUrl = url.substring(url.lastIndexOf("/") + 1);

        MutableLiveData<Resource<FeedDetailModel>> newsDetailData = new MutableLiveData<>();
        feedDetailData.getNewsDetail(detailUrl).enqueue(new Callback<FeedDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<FeedDetailModel> call, @NonNull Response<FeedDetailModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newsDetailData.setValue(Resource.success(response.body()));
                } else {
                    newsDetailData.setValue(Resource.error("Sunucu Hatası: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<FeedDetailModel> call, @NonNull Throwable t) {
                String errorMessage;
                if (t instanceof UnknownHostException) {
                    errorMessage = "İnternet bağlantınızı kontrol edin.";
                } else if (t instanceof SocketTimeoutException) {
                    errorMessage = "Bağlantı zaman aşımına uğradı!";
                } else {
                    errorMessage = "Bilinmeyen hata: " + t.getMessage();
                }
                newsDetailData.setValue(Resource.error(errorMessage, null));
            }
        });
        return newsDetailData;
    }
}
