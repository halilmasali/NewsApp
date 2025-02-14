package com.halilmasali.newsapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.halilmasali.newsapp.R;
import com.halilmasali.newsapp.data.model.feeddetail.FeedDetailModel;
import com.halilmasali.newsapp.databinding.FragmentDetailBinding;
import com.halilmasali.newsapp.viewmodel.FeedDetailViewModel;


public class DetailFragment extends Fragment {

    FragmentDetailBinding binding;
    FeedDetailViewModel newsDetailViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        loadNewsDetail();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadNewsDetail() {
        // Get the detail url from the arguments
        if (getArguments() != null) {
            String detailUrl = getArguments().getString("detail_url");
            // Get the news detail from the view model
            newsDetailViewModel = new ViewModelProvider(this).get(FeedDetailViewModel.class);
            newsDetailViewModel.getNewsDetail(detailUrl).observe(getViewLifecycleOwner(), feedDetailModel -> {
                if (feedDetailModel != null) {
                    updateUI(feedDetailModel, detailUrl);
                }
            });
        }
    }

    // Update the UI with the feed detail model
    private void updateUI(FeedDetailModel feedDetailModel, String detailUrl) {
        loadImage(feedDetailModel.detail.headerImage.url);
        binding.titleTextView.setText(feedDetailModel.detail.title);
        loadWebViewContent(feedDetailModel.detail.content);
        setupShareButton(feedDetailModel.detail.title, detailUrl);
        addRelatedContent(feedDetailModel);
    }

    // Load the image into the image view
    private void loadImage(String imageUrl) {

        Glide.with(requireContext())
                .load(imageUrl)
                .into(binding.detailImageView);
    }

    // Load the content into the web view with the necessary styling for images
    private void loadWebViewContent(String content) {
        String htmlContent = "<html><head><style>" +
                "img{max-width: 100%; width:auto; height: auto;}</style></head><body>"
                + content
                + "</body></html>";
        binding.webView.loadDataWithBaseURL(null, htmlContent,
                "text/html", "UTF-8", null);
    }

    // Setup the share button to share the news detail
    private void setupShareButton(String title, String detailUrl) {
        binding.shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            shareIntent.putExtra(Intent.EXTRA_TEXT, detailUrl);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
        });
    }

    // Add the related content to the linear layout with the content items
    private void addRelatedContent(FeedDetailModel feedDetailModel) {
        for (int i = 0; i < feedDetailModel.related.items.size(); i++) {
            ContentItem contentItem = new ContentItem(getContext(), null);
            contentItem.setContentTitle(feedDetailModel.related.displayText);
            contentItem.setJsonUrl(feedDetailModel.related.items.get(i).jsonUrl);
            loadImageIntoContentItem(feedDetailModel.related.items.get(i).mainImage.url, contentItem);
            setupContentItemClickListener(contentItem);
            binding.feedLinearLayout.addView(contentItem);
        }
    }

    // Load the image into the content item
    private void loadImageIntoContentItem(String imageUrl, ContentItem contentItem) {
        Glide.with(requireContext())
                .load(imageUrl)
                .into(contentItem.binding.contentImage);
    }

    // Setup the content item click listener to navigate to the detail fragment
    private void setupContentItemClickListener(ContentItem contentItem) {
        contentItem.setOnItemClickListener(jsonUrl -> {
            Bundle bundle = new Bundle();
            bundle.putString("detail_url", jsonUrl);
            NavController navController = NavHostFragment.findNavController(DetailFragment.this);
            navController.navigate(R.id.action_detailFragment_self, bundle);
        });
    }
}