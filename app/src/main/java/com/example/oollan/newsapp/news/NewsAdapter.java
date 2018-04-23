package com.example.oollan.newsapp.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oollan.newsapp.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsList;
    private static final String SEPARATOR_1 = "T";
    private static final String SEPARATOR_2 = "-";
    private static final String DASH_SEPARATOR = " / ";

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
        holder.thumbnail.setImageBitmap(newsList.get(position).getThumbnail());
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
        String[] parts = src.split(SEPARATOR_1);
        String[] splits = parts[0].split(SEPARATOR_2);
        return splits[2] + DASH_SEPARATOR + splits[1] + DASH_SEPARATOR + splits[0];
    }

    public News getCurrentNews(int position) {
        return newsList.get(position);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        private TextView title;
        private TextView date;

        NewsViewHolder(View recyclerItem) {
            super(recyclerItem);
            thumbnail = recyclerItem.findViewById(R.id.thumbnail);
            title = recyclerItem.findViewById(R.id.title);
            date = recyclerItem.findViewById(R.id.date);
        }
    }
}