package com.halilmasali.newsapp.data.model.shared;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the main image associated with a resource.
 * This class encapsulates the URL, height, and width of the main image.
 */
public class MainImage {

    @SerializedName("url")
    public String url;

    @SerializedName("height")
    public int height;

    @SerializedName("width")
    public int width;

}
