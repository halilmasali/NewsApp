package com.halilmasali.newsapp.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.halilmasali.newsapp.R;
import com.halilmasali.newsapp.databinding.SampleContentItemBinding;


public class ContentItem extends LinearLayout {

    SampleContentItemBinding binding;
    private String jsonUrl;

    public ContentItem(Context context) {
        super(context);
        init(null, 0);
    }

    public ContentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ContentItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LayoutInflater.from(getContext()).inflate(R.layout.sample_content_item, this, true);
        // Load attributes
        binding = SampleContentItemBinding.bind(this);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ContentItem, defStyle, 0);
        String contentTitle = a.getString(R.styleable.ContentItem_contentTitle);
        if (contentTitle != null) {
            binding.contentTitle.setText(contentTitle);
        }
        Drawable image = a.getDrawable(R.styleable.ContentItem_contentImage);
        if (image != null) {
            binding.contentImage.setImageDrawable(image);
        }
        a.recycle();

        setOnClickListener(v -> {
            // Open new activity with jsonUrl
            if (jsonUrl != null) {
                Toast.makeText(getContext(), jsonUrl, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setContentTitle(String title) {
        binding.contentTitle.setText(title);
    }

    public void setContentImage(Drawable image) {
        binding.contentImage.setImageDrawable(image);
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }
}