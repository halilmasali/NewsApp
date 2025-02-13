package com.halilmasali.newsapp.data.model.shared;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an item object with various properties.
 * This class is designed to be used with Gson for JSON serialization and deserialization.
 */
public class Item {

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("title")
    public String title;

    @SerializedName("type")
    public String type;

    @SerializedName("category_name")
    public String categoryName;

    @SerializedName("main_image")
    public MainImage mainImage;

    @SerializedName("json_url")
    public String jsonUrl;

    @SerializedName("detail_mini_content")
    public String detailMiniContent;

}
