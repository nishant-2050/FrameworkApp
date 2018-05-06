package com.nis.frameworkapp.news.ui.newsdisplay.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.nis.frameworkapp.R;
import com.nis.frameworkapp.news.data.model.News;
import com.nis.frameworkapp.news.data.model.NewsList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter
        .NewsAdapterViewHolder> {

    private Context mContext;
    private NewsAdapterOnItemClickHandler mClickHandler;
    private NewsList mNewsList;

    public NewsListAdapter(Context context, NewsAdapterOnItemClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public NewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_list_item, parent, false);
        view.setFocusable(true);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterViewHolder holder, int position) {
        News news = mNewsList.news.get(position);
        holder.title.setText(news.title);
        holder.details.setText(news.details);
        Glide.with(mContext)
                .load(news.thumbnail)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        if (null == mNewsList) return 0;
        return mNewsList.news.size();
    }

    public interface NewsAdapterOnItemClickHandler {
        void onItemClick(int selectedItem);
    }

    public void swapNewsList(final NewsList newNewsList) {
        if (mNewsList == null) {
            mNewsList = newNewsList;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mNewsList.news.size();
                }

                @Override
                public int getNewListSize() {
                    return newNewsList.news.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mNewsList.news.get(oldItemPosition).id ==
                            newNewsList.news.get(oldItemPosition).id;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    News newNews = newNewsList.news.get(newItemPosition);
                    News oldNews = mNewsList.news.get(oldItemPosition);
                    return newNews.id == oldNews.id
                            && newNews.title.equals(oldNews.title);
                }
            });
            mNewsList = newNewsList;
            result.dispatchUpdatesTo(this);
        }
    }

    class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, details;
        private ImageView thumbnail;

        NewsAdapterViewHolder(View view) {
            super(view);
            this.title = view.findViewById(R.id.id_title);
            this.details = view.findViewById(R.id.id_ingr);
            this.thumbnail = view.findViewById(R.id.id_thumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onItemClick(getAdapterPosition());
        }
    }
}
