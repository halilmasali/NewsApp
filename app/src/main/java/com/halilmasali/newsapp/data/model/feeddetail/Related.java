package com.halilmasali.newsapp.data.model.feeddetail;

import com.google.gson.annotations.SerializedName;
import com.halilmasali.newsapp.data.model.shared.Item;

import java.util.ArrayList;

/**
 * Represents a set of related items.
 * <p>
 * This class encapsulates the information about a group of related items,
 * including a display text describing the relationship and a list of
 * the actual items. It is typically used to represent a section of
 * recommendations or related content.
 * </p>
 */
public class Related {

    @SerializedName("display_text")
    public String displayText;

    @SerializedName("items")
    public ArrayList<Item> items;

}
