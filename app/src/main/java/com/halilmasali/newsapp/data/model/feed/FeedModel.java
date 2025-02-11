package com.halilmasali.newsapp.data.model.feed;

import com.google.gson.annotations.SerializedName;
import com.halilmasali.newsapp.data.model.shared.Item;

import java.util.ArrayList;

/**
 * Represents the root model for a feed, containing both featured and regular items.
 * This class is designed to be used with Gson for JSON deserialization.
 */
public class FeedModel {

    @SerializedName("featured")
    public ArrayList<Item> featured;

    @SerializedName("items")
    public ArrayList<Item> items;

}
