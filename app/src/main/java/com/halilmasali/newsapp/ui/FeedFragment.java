package com.halilmasali.newsapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.halilmasali.newsapp.databinding.FragmentFeedBinding;
import com.halilmasali.newsapp.viewmodel.FeedViewModel;

public class FeedFragment extends Fragment {

    FragmentFeedBinding binding;
    FeedViewModel newsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedBinding.inflate(inflater, container, false);


        // Get news list from ViewModel
        newsViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        newsViewModel.getNewsList().observe(getViewLifecycleOwner(), feedModel -> {
            // Update UI
            if (feedModel != null) {
                CarouselAdapter adapter = new CarouselAdapter(getContext(), feedModel.featured);
                binding.recyclerCarousel.setAdapter(adapter);
                adapter.setOnItemClickListener(jsonUrl ->
                        // Todo open DetailFragment
                        Toast.makeText(getContext(), jsonUrl, Toast.LENGTH_SHORT).show()
                );

                for (int i = 0; i < feedModel.items.size(); i++) {
                    ContentItem contentItem = new ContentItem(getContext(), null);
                    contentItem.setContentTitle(feedModel.items.get(i).title);
                    contentItem.setJsonUrl(feedModel.items.get(i).jsonUrl);
                    Glide.with(requireContext())
                            .load(feedModel.items.get(i).mainImage.url)
                            .into(contentItem.binding.contentImage);
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
}