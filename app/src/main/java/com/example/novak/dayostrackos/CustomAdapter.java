package com.example.novak.dayostrackos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by novak on 17-Dec-17.
 */

public class CustomAdapter extends ArrayAdapter<Record> implements View.OnClickListener {

    private ArrayList<Record> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView titleTextView;
        TextView categoryTextView;
        TextView commentaryTextView;
        TextView typeTextView;
        ImageView itemImageView;
    }

    public CustomAdapter(ArrayList<Record> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Record record = (Record) object;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Record record = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            viewHolder.categoryTextView = (TextView) convertView.findViewById(R.id.categoryTextView);
            viewHolder.commentaryTextView = (TextView) convertView.findViewById(R.id.contentTextView);
            viewHolder.typeTextView = (TextView) convertView.findViewById(R.id.typeTextView);
            viewHolder.itemImageView = (ImageView) convertView.findViewById(R.id.itemImageView);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        Bitmap bitmapToDisplay = getBitmap(record);

        String type = record.getType();
        String capitalizedType = type.substring(0, 1).toUpperCase() + type.substring(1) + ":";

        String shortenedCommentary;
        if (record.getText().length() > 35)
            shortenedCommentary = record.getText().substring(0, 35).concat(" ...");
        else
            shortenedCommentary = record.getText();

        String shortenedTitle;
        if (record.getTitle().length() > 30)
            shortenedTitle = record.getTitle().substring(0, 35).concat("...");
        else
            shortenedTitle = record.getTitle();

        viewHolder.itemImageView.setImageBitmap(bitmapToDisplay);
        viewHolder.titleTextView.setText(shortenedTitle);
        viewHolder.categoryTextView.setText(record.getCategory());
        viewHolder.commentaryTextView.setText(shortenedCommentary);
        viewHolder.typeTextView.setText(capitalizedType);
        viewHolder.itemImageView.setOnClickListener(this);
        viewHolder.itemImageView.setTag(position);

        return convertView;
    }

    private Bitmap getBitmap(Record record) {

        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_menu_camera);

        switch (record.getType()) {
            case "note":
                return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.note);
            case "photo":
                if (record.getLinkToResource() != null)
                    return displayPictureThumbnail(record.getLinkToResource());
                else
                    return icon;
            case "video":
                return displayVideoThumbnail(record.getLinkToResource());
            case "audio":
                return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.voice);
            default:
                return icon;
        }
    }


    private Bitmap displayVideoThumbnail(String link) {

        Uri fileUri = Uri.parse(link);

        File videoFile = new File(fileUri.getPath());

        Bitmap thumbImage = createThumbnailFromPath(videoFile.getAbsolutePath(), MediaStore.Images.Thumbnails.MICRO_KIND);

        return thumbImage;
    }

    public Bitmap createThumbnailFromPath(String filePath, int type) {
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
    }


    private Bitmap displayPictureThumbnail(String link) {
        final int THUMBSIZE = 128;

        Uri fileUri = Uri.parse(link);

        File imageFile = new File(fileUri.getPath());
        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(imageFile.getAbsolutePath()),
                THUMBSIZE,
                THUMBSIZE);

        return thumbImage;
    }
}