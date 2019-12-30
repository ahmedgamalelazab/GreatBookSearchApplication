package com.example.fullnetworkapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TemplateAdapter extends ArrayAdapter<DataContainerModel> {
    public TemplateAdapter(Context context, ArrayList<DataContainerModel> data) {
        super(context, 0, data);
    }

    //now lets play with how we will preview the data on the book activity


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View ListViewItem = convertView;
        if (ListViewItem == null) {
            ListViewItem = LayoutInflater.from(getContext()).inflate(R.layout.template, parent, false);
        }

        DataContainerModel currentDataContainer = getItem(position);

        //firs lets get the photo from the goddamtt url
        String ImageURL = currentDataContainer.getthumbnailURL();
        UrlImageView ImageView = ListViewItem.findViewById(R.id.thumbnail);
        ImageView.setImageURL(currentDataContainer.ConvertToUrl(ImageURL));
        //now we done with this


        TextView fullBookName = ListViewItem.findViewById(R.id.FullBookName);
        fullBookName.setText(currentDataContainer.getBookName());

        TextView PagesCout = ListViewItem.findViewById(R.id.FullBookPages);
        PagesCout.setText(currentDataContainer.getPageCount());


        return ListViewItem;
    }
}
