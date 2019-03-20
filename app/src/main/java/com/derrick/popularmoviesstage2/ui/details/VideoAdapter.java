package com.derrick.popularmoviesstage2.ui.details;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.derrick.popularmoviesstage2.R;
import com.derrick.popularmoviesstage2.data.local.VideoResult;
import com.derrick.popularmoviesstage2.databinding.VideoItemBinding;
import com.derrick.popularmoviesstage2.utils.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.derrick.popularmoviesstage2.utils.Base_urls.THUMBNAIL;
import static com.derrick.popularmoviesstage2.utils.Base_urls.THUMBNAIL_DEFAULT_QUALITY;
import static com.derrick.popularmoviesstage2.utils.Base_urls.TRAILER_BASE_URL;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private static final String LOG_TAG = VideoAdapter.class.getSimpleName();
    private List<VideoResult> videoResults = null;
    private Context mContext;

    public VideoAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setVideoResults(List<VideoResult> videoResults) {
        this.videoResults = videoResults;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        VideoItemBinding videoItemBinding = VideoItemBinding.inflate(layoutInflater);


        return new VideoHolder(videoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int pos) {
        VideoResult mResult = videoResults.get(pos);


        String thumbnail = THUMBNAIL + mResult.getKey() + THUMBNAIL_DEFAULT_QUALITY;

        String youtubeUrl = TRAILER_BASE_URL + mResult.getKey();


        LogUtils.showLog(LOG_TAG, "@Details mResult.getKey()::" + thumbnail);


        Picasso.get().load(thumbnail).placeholder(R.drawable.video_placeholder).into(holder.videoItemBinding.thumbNail);


        LogUtils.showLog(LOG_TAG, "@Details mResult.getKey()" + mResult.getKey());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watch_video(youtubeUrl);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoResults != null ? videoResults.size() : 0;
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        VideoItemBinding videoItemBinding;

        public VideoHolder(@NonNull VideoItemBinding v) {
            super(v.getRoot());
            videoItemBinding = v;
        }
    }

    void watch_video(String url) {
        // Build the intent
        Uri youtubeUrl = Uri.parse(url);
        Intent youtube = new Intent(Intent.ACTION_VIEW, youtubeUrl);

        // Verify it resolves
        PackageManager packageManager = mContext.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(youtube, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
            mContext.startActivity(youtube);
        }
    }
}
