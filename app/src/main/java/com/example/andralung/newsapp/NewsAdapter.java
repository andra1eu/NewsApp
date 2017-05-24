package com.example.andralung.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, @NonNull List<News> newsList) {
        super(context, 0, newsList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);

            News currentNews = getItem(position);

            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView section = (TextView) convertView.findViewById(R.id.description);
            title.setText(currentNews.getTitle());
            section.setText(currentNews.getDescription());
        }
        return convertView;
    }


    public void updateData(ArrayList<News> data) {
        clear();
        if (data == null) return;
        addAll(data);
    }
}
