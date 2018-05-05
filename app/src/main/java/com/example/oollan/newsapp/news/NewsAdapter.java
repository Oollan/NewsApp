package com.example.oollan.newsapp.news;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oollan.newsapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.oollan.newsapp.utils.Constants.DASH_SEPARATOR;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsList;

    public void setNewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        if (newsList.get(position).getThumbnail() != null) {
            holder.thumbnail.setImageBitmap(newsList.get(position).getThumbnail());
            holder.thumbnail.setVisibility(View.VISIBLE);
        } else {
            holder.thumbnail.setVisibility(View.GONE);
        }
        holder.title.setText(newsList.get(position).getTitle());
        holder.date.setText(dateFormatter(newsList.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void clearData() {
        newsList.clear();
        notifyDataSetChanged();
    }

    private String dateFormatter(String src) {
        String[] parts = src.split("T");
        String[] splits = parts[0].split("-");
        return splits[2] + DASH_SEPARATOR + splits[1] + DASH_SEPARATOR + splits[0];
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail)
        ImageView thumbnail;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;

        NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick
        void onClick() {
            News currentNews = newsList.get(getAdapterPosition());
            Uri newsUri = Uri.parse(currentNews.getUrl());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
            itemView.getContext().startActivity(websiteIntent);
        }
    }
}