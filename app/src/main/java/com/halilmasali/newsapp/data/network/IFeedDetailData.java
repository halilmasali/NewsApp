package com.halilmasali.newsapp.data.network;

import com.halilmasali.newsapp.data.model.feeddetail.FeedDetailModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IFeedDetailData {
    @GET("{url}")
    Call<FeedDetailModel> getNewsDetail(@Path("url") String detailUrl);
}
