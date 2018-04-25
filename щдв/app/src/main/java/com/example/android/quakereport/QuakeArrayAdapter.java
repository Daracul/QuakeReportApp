package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by amalakhov on 16.04.2018.
 */

public class QuakeArrayAdapter extends ArrayAdapter<Quake> {
    private static final String LOCATION_SEPARATOR = " of ";
    public static final String LOG_TAG = QuakeArrayAdapter.class.getSimpleName();

    public QuakeArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Quake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        if (listViewItem == null){
            listViewItem= LayoutInflater.from(getContext()).inflate(R.layout.item,parent,false);
        }
        Quake currentQuake = getItem(position);

        TextView magTextView = (TextView)listViewItem.findViewById(R.id.magnitude);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        magTextView.setText(decimalFormat.format(currentQuake.getMagnitude()));

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentQuake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        TextView locationOffset = (TextView)listViewItem.findViewById(R.id.location_offset);
        TextView primaryLocation = (TextView)listViewItem.findViewById(R.id.location_primary);

        String location = currentQuake.getLocation();

        locationOffset.setText(getLocationOffset(location));
        primaryLocation.setText(getPrimaryLocation(location));

        Date dateEvent = new Date(currentQuake.getTimeInMilliseconds());
        Log.d(LOG_TAG,"time Unix : "+currentQuake.getTimeInMilliseconds());
        TextView dateTextView = (TextView)listViewItem.findViewById(R.id.date);
        String formattedDate = formatDate(dateEvent);
        Log.d(LOG_TAG,"formatted Date : "+ formattedDate);
        dateTextView.setText(formattedDate);

        TextView timeTextView = (TextView)listViewItem.findViewById(R.id.time) ;
        String formattedTime = formatTime(dateEvent);
        timeTextView.setText(formattedTime);

        return listViewItem;
    }

    private int getMagnitudeColor(double magnitude) {
        int colorResourceId=R.color.magnitude1;
        switch ((int)magnitude){
            case 2:
                colorResourceId = R.color.magnitude2;
                break;
            case 3:
                colorResourceId = R.color.magnitude3;
                break;
            case 4:
                colorResourceId = R.color.magnitude4;
                break;
            case 5:
                colorResourceId = R.color.magnitude5;
                break;
            case 6:
                colorResourceId = R.color.magnitude6;
                break;
            case 7:
                colorResourceId = R.color.magnitude7;
                break;
            case 8:
                colorResourceId = R.color.magnitude8;
                break;
            case 9:
                colorResourceId = R.color.magnitude9;
                break;
            case 10:
                colorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),colorResourceId);
    }

    private String formatDate (Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(date);
    }

    private String formatTime(Date time){
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm",Locale.getDefault());
        return timeFormat.format(time);
    }

    private String getLocationOffset(String location){
        if (location.contains(LOCATION_SEPARATOR)){
            String locations[] = location.split(LOCATION_SEPARATOR);
            return locations[0]+LOCATION_SEPARATOR;
        }
        else return getContext().getString(R.string.near_the);
    }

    private String getPrimaryLocation(String location){
        if (location.contains(LOCATION_SEPARATOR)){
            String locations[] = location.split(LOCATION_SEPARATOR);
            return locations[1];
        }
        else return location;
    }
}
