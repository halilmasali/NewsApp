package com.halilmasali.newsapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.halilmasali.newsapp.R;
import com.halilmasali.newsapp.data.model.feeddetail.FeedDetailModel;
import com.halilmasali.newsapp.data.network.Resource;
import com.halilmasali.newsapp.databinding.FragmentDetailBinding;
import com.halilmasali.newsapp.viewmodel.FeedDetailViewModel;


public class DetailFragment extends Fragment {

    FragmentDetailBinding binding;
    FeedDetailViewModel newsDetailViewModel;
    private InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        addAdmobBanner();
        loadInterstitialAd();
        loadNewsDetail();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        showInterstitialAd();
    }

    private void loadNewsDetail() {
        // Get the detail url from the arguments
        if (getArguments() != null) {
            String detailUrl = getArguments().getString("detail_url");
            // Get the news detail from the view model
            newsDetailViewModel = new ViewModelProvider(this).get(FeedDetailViewModel.class);
            newsDetailViewModel.getNewsDetail(detailUrl).observe(getViewLifecycleOwner(), resource -> {
                if (resource.status == Resource.Status.LOADING) {
                    setShimmerVisibility(true);
                } else if (resource.status == Resource.Status.SUCCESS) {
                    setShimmerVisibility(false);
                    updateUI(resource.data);
                } else if (resource.status == Resource.Status.ERROR) {
                    setShimmerVisibility(false);
                    Log.e("DetailFragment", "Error loading news detail: " + resource.message);
                    setErrorMessage(resource.message);
                }
            });
        }
    }

    // Update the UI with the feed detail model
    private void updateUI(FeedDetailModel feedDetailModel) {
        loadImage(feedDetailModel.detail.headerImage.url);
        binding.titleTextView.setText(feedDetailModel.detail.title);
        loadWebViewContent(feedDetailModel.detail.content);
        setupShareButton(feedDetailModel.detail.title, feedDetailModel.detail.shareUrl);
        addRelatedContent(feedDetailModel);
    }

    // Load the image into the image view
    private void loadImage(String imageUrl) {

        Glide.with(requireContext())
                .load(imageUrl)
                .into(binding.detailImageView);
    }

    // Load the content into the web view with the necessary styling for images and text
    private void loadWebViewContent(String content) {
        boolean isNightMode = (getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES;

        String backgroundColor = isNightMode ? "#000000" : "#FFFFFF";
        String textColor = isNightMode ? "#FFFFFF" : "#000000";

        String htmlContent = "<html><head><style>" +
                "body { background-color: " + backgroundColor + "; color: " + textColor + "; }" +
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

            // Add Admob banner every 3 content items
            if ((i + 1) % 3 == 0) {
                binding.feedLinearLayout.addView(createAdView());
            }
        }
    }

    private AdView createAdView() {
        // Add layout params for AdView and set margins
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 8, 0, 8);
        // Create AdView and add it to feedLinearLayout
        AdView adView = new AdView(requireContext());
        adView.setLayoutParams(layoutParams);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getString(R.string.admob_banner_unit_id)); // Test ad unit ID
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        return adView;
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

    // Add Admob banner
    private void addAdmobBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adViewDetail.loadAd(adRequest);
    }

    // Load the interstitial ad
    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(requireContext(), getString(R.string.admob_interstitial_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    // Show the interstitial ad
    private void showInterstitialAd() {
        NavController navController = NavHostFragment.findNavController(DetailFragment.this);
        if (navController.getCurrentDestination() != null &&
                navController.getCurrentDestination().getId() == R.id.feedFragment) {
            if (mInterstitialAd != null) {
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d("Admob", "Interstitial Ad was dismissed.");
                    }
                });
                mInterstitialAd.show(requireActivity());
            } else {
                navigateToFeedFragment();
            }
        }
    }

    // Navigate to feed fragment
    private void navigateToFeedFragment() {
        NavController navController = NavHostFragment.findNavController(DetailFragment.this);
        navController.navigateUp();
    }

    // Set shimmer visibility for loading effect
    private void setShimmerVisibility(boolean visible) {
        if (visible) {
            binding.shimmerDetailLayout.startShimmer();
            binding.shimmerDetailLayout.setVisibility(View.VISIBLE);
            // Set all other views to INVISIBLE
            for (int i = 0; i < binding.feedLinearLayout.getChildCount(); i++) {
                View child = binding.feedLinearLayout.getChildAt(i);
                child.setVisibility(View.INVISIBLE);
            }
        } else {
            binding.shimmerDetailLayout.stopShimmer();
            binding.shimmerDetailLayout.setVisibility(View.GONE);
            // Set all other views to VISIBLE
            for (int i = 0; i < binding.feedLinearLayout.getChildCount(); i++) {
                View child = binding.feedLinearLayout.getChildAt(i);
                child.setVisibility(View.VISIBLE);
            }
        }
    }

    // Set error message for error loading news detail
    private void setErrorMessage(String errorMessage) {
        binding.errorMessage.setText(errorMessage);
        binding.errorLayout.setVisibility(View.VISIBLE);
        binding.retryButton.setOnClickListener(v -> {
            loadNewsDetail();
            binding.errorLayout.setVisibility(View.GONE);
        });
    }
}