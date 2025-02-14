package com.halilmasali.newsapp.data.model.feeddetail;

import com.google.gson.annotations.SerializedName;
import com.halilmasali.newsapp.data.model.shared.MainImage;

/**
 * Represents a detailed content object.
 * This class encapsulates various attributes of a content item, such as its unique identifier,
 * title, summary, full content, type, category, header image, share URL, and video URL.
 * It's designed to be used with Gson for easy serialization and deserialization from JSON.
 */
public class Detail {

    @SerializedName("uuid")
    public int uuid;

    @SerializedName("title")
    public String title;

    @SerializedName("summary")
    public String summary;

    @SerializedName("content")
    public String content;

    @SerializedName("type")
    public String type;

    @SerializedName("category_name")
    public String categoryName;

    @SerializedName("header_image")
    public MainImage headerImage;

    @SerializedName("share_url")
    public String shareUrl;

    @SerializedName("video_url")
    public String videoUrl;

}
