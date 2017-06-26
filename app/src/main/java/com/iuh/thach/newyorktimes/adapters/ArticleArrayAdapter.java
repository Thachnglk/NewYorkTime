package com.iuh.thach.newyorktimes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iuh.thach.newyorktimes.R;
import com.iuh.thach.newyorktimes.models.Article;

import java.util.List;


public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    private static class ViewHolder{
        private TextView tvHeadLine;
        private ImageView ivThumbNail;
    }
    public ArticleArrayAdapter(Context context, List<Article> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_article,parent,false);
            viewHolder.ivThumbNail = (ImageView) convertView.findViewById(R.id.ivThumbNail);
            viewHolder.tvHeadLine = (TextView) convertView.findViewById(R.id.tvHeadLine);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
       viewHolder.tvHeadLine.setText(article.getHeadLine());
       Glide.with(getContext()).load(article.getThumbNail()).placeholder(R.drawable.loadimage).fitCenter().error(R.color.colorWhite).into(viewHolder.ivThumbNail);
        return convertView;
    }

}
