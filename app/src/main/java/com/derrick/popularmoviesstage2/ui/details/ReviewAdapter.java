package com.derrick.popularmoviesstage2.ui.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derrick.popularmoviesstage2.data.local.ReviewsResult;
import com.derrick.popularmoviesstage2.databinding.ReviewsRowBinding;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    public void setReviewsResults(List<ReviewsResult> mReviewsResults) {
        this.mReviewsResults = mReviewsResults;
        notifyDataSetChanged();
    }

    List<ReviewsResult> mReviewsResults = null;

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ReviewsRowBinding reviewItemsBinding = ReviewsRowBinding.inflate(layoutInflater);
        return new ReviewViewHolder(reviewItemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int pos) {
        ReviewsResult author = mReviewsResults.get(pos);
        reviewViewHolder.itemsBinding.authorName.setText(author.getAuthor());
        reviewViewHolder.itemsBinding.authorInitialChar.setText(author.getAuthor().substring(0, 1).toUpperCase());
        reviewViewHolder.itemsBinding.authorReview.setText(author.getContent());

    }

    @Override
    public int getItemCount() {
        return mReviewsResults == null ? 0 : mReviewsResults.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ReviewsRowBinding itemsBinding;

        public ReviewViewHolder(@NonNull ReviewsRowBinding itemView) {
            super(itemView.getRoot());
            itemsBinding = itemView;
        }
    }
}
