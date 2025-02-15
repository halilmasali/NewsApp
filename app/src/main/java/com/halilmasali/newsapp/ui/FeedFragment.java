package com.halilmasali.newsapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.halilmasali.newsapp.R;
import com.halilmasali.newsapp.data.model.feed.FeedModel;
import com.halilmasali.newsapp.databinding.FragmentFeedBinding;
import com.halilmasali.newsapp.viewmodel.FeedViewModel;

public class FeedFragment extends Fragment {

    FragmentFeedBinding binding;
    FeedViewModel newsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedBinding.inflate(inflater, container, false);
        newsViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        addAdmobBanner();
        observeNewsList();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Navigate to detail fragment with jsonUrl
    private void navigateToDetailFragment(String jsonUrl) {
        Bundle bundle = new Bundle();
        bundle.putString("detail_url", jsonUrl);
        NavController navController = NavHostFragment.findNavController(FeedFragment.this);
        navController.navigate(R.id.action_feedFragment_to_detailFragment, bundle);
    }

    // Add Admob banner
    private void addAdmobBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adViewCarousel.loadAd(adRequest);
    }

    // Observe news list
    private void observeNewsList() {
        newsViewModel.getNewsList().observe(getViewLifecycleOwner(), feedModel -> {
            if (feedModel != null) {
                setupCarouselAdapter(feedModel);
                addContentItems(feedModel);
            }
        });
    }

    // Setup carousel adapter
    private void setupCarouselAdapter(FeedModel feedModel) {
        CarouselAdapter adapter = new CarouselAdapter(getContext(), feedModel.featured);
        binding.recyclerCarousel.setAdapter(adapter);
        adapter.setOnItemClickListener(this::navigateToDetailFragment);
    }

    // Add content items
    private void addContentItems(FeedModel feedModel) {
        for (int i = 0; i < feedModel.items.size(); i++) {
            ContentItem contentItem = new ContentItem(getContext(), null);
            contentItem.setContentTitle(feedModel.items.get(i).title);
            contentItem.setJsonUrl(feedModel.items.get(i).jsonUrl);
            loadImageIntoContentItem(feedModel.items.get(i).mainImage.url, contentItem);
            // Set onItemClickListener for each content item
            contentItem.setOnItemClickListener(this::navigateToDetailFragment);
            binding.feedLinearLayout.addView(contentItem);

            // Add Admob banner every 3 content items
            if ((i + 1) % 3 == 0) {
                // Add layout params for AdView and set margins
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 8, 0, 8);
                // Create AdView and add it to feedLinearLayout
                AdView adView = new AdView(requireContext());
                adView.setLayoutParams(layoutParams);
                adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                adView.setAdUnitId(getString(R.string.admob_banner_unit_id)); // Test ad unit ID
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
                binding.feedLinearLayout.addView(adView);
            }
        }
    }

    // Load image into content item
    private void loadImageIntoContentItem(String imageUrl, ContentItem contentItem) {
        Glide.with(requireContext())
                .load(imageUrl)
                .into(contentItem.binding.contentImage);
    }
}