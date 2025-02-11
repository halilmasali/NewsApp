package com.halilmasali.newsapp.data.network;

import com.halilmasali.newsapp.data.model.feeddetail.FeedDetailModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IFeedDetailData {
    // Todo: Define the endpoint for the news detail
    @GET("feed_detail")
    Call<FeedDetailModel> getNewsDetail();
}
