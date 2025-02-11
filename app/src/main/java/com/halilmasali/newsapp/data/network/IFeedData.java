package com.halilmasali.newsapp.data.network;

import com.halilmasali.newsapp.data.model.feed.FeedModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IFeedData {
    @GET("feed")
    Call<FeedModel> getNews();
}
