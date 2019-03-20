package com.derrick.popularmoviesstage2.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.derrick.popularmoviesstage2.data.local.Result;
import com.derrick.popularmoviesstage2.databinding.GridItemBinding;
import com.derrick.popularmoviesstage2.utils.Base_urls;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Result> results = null;

    private onListClickLister onListClickLister;


    public interface onListClickLister {
        void onClick(long movie_id);
    }

    public void setOnListClickLister(MovieAdapter.onListClickLister onListClickLister) {
        this.onListClickLister = onListClickLister;
    }


    public MovieAdapter() {
    }

    public void setResults(List<Result> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());


        GridItemBinding mGridItemBinding = GridItemBinding.inflate(layoutInflater);

        return new MovieViewHolder(mGridItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, final int pos) {
        Result movie = results.get(pos);

        String poster = Base_urls.TMDB_IMG_BASE_URL + movie.getPosterPath();

        Picasso.get().load(poster).into(holder.mItemBinding.itemImg);

        holder.mItemBinding.getRoot().setOnClickListener(v -> onListClickLister.onClick(movie.getId()));

    }

    @Override
    public int getItemCount() {
        return results == null ? 0 : results.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        GridItemBinding mItemBinding;

        public MovieViewHolder(@NonNull GridItemBinding itemView) {
            super(itemView.getRoot());
            mItemBinding = itemView;
        }

    }
}
