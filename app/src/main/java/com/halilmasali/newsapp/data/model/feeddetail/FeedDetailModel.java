package com.halilmasali.newsapp.data.model.feeddetail;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the detailed information for a feed item, including the main detail and related content.
 * This class is designed to be used with Gson for JSON serialization and deserialization.
 */
public class FeedDetailModel {

    @SerializedName("detail")
    public Detail detail;

    @SerializedName("related")
    public Related related;

}
