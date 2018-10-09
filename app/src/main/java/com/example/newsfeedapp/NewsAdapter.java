package com.example.newsfeedapp;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ferhat on 8.5.2018.
 */

public class NewsAdapter extends ArrayAdapter<NewsLibrary> {

    public NewsAdapter(Activity context, List<NewsLibrary> items) {

        super(context, 0, items);
    }

    static class ViewHolder {
        ImageView getImage;
        TextView getTitle;
        TextView getUrl;
        TextView getAuthor;
        TextView getDate;
    }

    @Override
    public View getView(int position, View listItemView, ViewGroup parent) {
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        ViewHolder holder = new ViewHolder();

        Resources context = getContext().getResources();

        NewsLibrary currentLib = getItem(position);

        /*** Image ***/

        holder.getImage = listItemView.findViewById(R.id.getImage);

        String url = currentLib.getImage();

        Picasso.get().load(url).into(holder.getImage);

        /*** Title ***/

        holder.getTitle = listItemView.findViewById(R.id.getTitle);

        holder.getTitle.setText(currentLib.getWebTitle());

        /*** Url ***/

        holder.getUrl= listItemView.findViewById(R.id.getUrl);

        holder.getUrl.setText(currentLib.getWebUrl().substring(0,50)+context.getString(R.string.dots));

        /*** Author ***/

        holder.getAuthor = listItemView.findViewById(R.id.author);

        holder.getAuthor.setText(currentLib.getAuthor());

        /*** Date ***/

        holder.getDate = listItemView.findViewById(R.id.getDate);

        String timeSubstr = currentLib.getWebPublicationDate().substring(12,16);

        String daySubstr = currentLib.getWebPublicationDate().substring(8,10);

        String monthSubstr = currentLib.getWebPublicationDate().substring(5,7);

        String yearSubstr = currentLib.getWebPublicationDate().substring(0,4);

        holder.getDate.setText(timeSubstr+"   "+monthSubstr+context.getString(R.string.slash)+daySubstr+
                context.getString(R.string.slash)+yearSubstr);

        return listItemView;
    }

}
