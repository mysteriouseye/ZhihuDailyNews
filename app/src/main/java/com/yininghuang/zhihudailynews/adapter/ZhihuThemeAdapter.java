package com.yininghuang.zhihudailynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.comment.ZhihuTheme;
import com.yininghuang.zhihudailynews.detail.ZhihuNewsDetailActivity;
import com.yininghuang.zhihudailynews.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/31.
 */

public class ZhihuThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int TYPE_LOADING = -1;
    private int TYPE_POSTER = 0;
    private int TYPE_STORY = 1;

    private List<ZhihuTheme> mZhihuThemes = new ArrayList<>();
    private List<ZhihuTheme.StoriesBean> mStories = new ArrayList<>();
    private Context mContext;

    public ZhihuThemeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_POSTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_header, parent, false);
            return new PosterHolder(view);
        } else if (viewType == TYPE_STORY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhihu_lastest, parent, false);
            return new ZhihuLatestAdapter.NewsHolder(view);
        } else if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_status, parent, false);
            return new ZhihuLatestAdapter.LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PosterHolder) {
            PosterHolder posterHolder = ((PosterHolder) holder);
            ImageLoader.load(mContext, posterHolder.mPoster, mZhihuThemes.get(0).getImage());
            posterHolder.mTitle.setText(mZhihuThemes.get(0).getDescription());
        } else if (holder instanceof ZhihuLatestAdapter.NewsHolder) {
            ZhihuTheme.StoriesBean data = mStories.get(position - 1);
            ZhihuLatestAdapter.NewsHolder newsHolder = (ZhihuLatestAdapter.NewsHolder) holder;
            newsHolder.title.setText(data.getTitle());

            newsHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ZhihuNewsDetailActivity.class);
                    intent.putExtra("id", mStories.get(holder.getAdapterPosition() - 1).getId());
                    mContext.startActivity(intent);
                }
            });

            if (data.getImages() == null || data.getImages().size() == 0) {
                newsHolder.imageView.setVisibility(View.GONE);
            } else {
                newsHolder.imageView.setVisibility(View.VISIBLE);
                ImageLoader.load(mContext, newsHolder.imageView, data.getImages().get(0));
            }
        }
    }

    public List<ZhihuTheme> getZhihuThemes() {
        return mZhihuThemes;
    }

    public List<ZhihuTheme.StoriesBean> getStories() {
        return mStories;
    }

    public void addTheme(ZhihuTheme theme) {
        mZhihuThemes.add(theme);
        mStories.addAll(theme.getStories());
    }

    public void addThemes(List<ZhihuTheme> themes) {
        for (ZhihuTheme theme : themes) {
            addTheme(theme);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_POSTER;
        else if (position == mStories.size() + 1)
            return TYPE_LOADING;
        else return TYPE_STORY;
    }

    @Override
    public int getItemCount() {
        if (mStories.size() == 0)
            return 0;
        return mStories.size() + 2;
    }

    public static class PosterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.posterImage)
        ImageView mPoster;

        @BindView(R.id.title)
        TextView mTitle;

        public PosterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}