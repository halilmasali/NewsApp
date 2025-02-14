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

import com.bumptech.glide.Glide;
import com.halilmasali.newsapp.R;
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


        // Get news list from ViewModel
        newsViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        newsViewModel.getNewsList().observe(getViewLifecycleOwner(), feedModel -> {
            // Set carousel adapter with featured items from feedModel featured list
            if (feedModel != null) {
                CarouselAdapter adapter = new CarouselAdapter(getContext(), feedModel.featured);
                binding.recyclerCarousel.setAdapter(adapter);
                // Set onItemClickListener for each carousel item
                adapter.setOnItemClickListener(this::navigateToDetailFragment);

                // Add content items to linear layout dynamically from feedModel items
                for (int i = 0; i < feedModel.items.size(); i++) {
                    ContentItem contentItem = new ContentItem(getContext(), null);
                    contentItem.setContentTitle(feedModel.items.get(i).title);
                    contentItem.setJsonUrl(feedModel.items.get(i).jsonUrl);
                    Glide.with(requireContext())
                            .load(feedModel.items.get(i).mainImage.url)
                            .into(contentItem.binding.contentImage);
                    // Set onItemClickListener for each content item
                    contentItem.setOnItemClickListener(this::navigateToDetailFragment);
                    binding.feedLinearLayout.addView(contentItem);
                }
            }
        });
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
}