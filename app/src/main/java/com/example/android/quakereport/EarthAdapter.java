package com.example.android.quakereport;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hhharsh on 24/10/17.
 */

public class EarthAdapter extends ArrayAdapter<Earth> {
    public EarthAdapter(Activity context, ArrayList<Earth> earthquakes) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.items, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Earth currentWord = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView magTextView = (TextView) listItemView.findViewById(R.id.magid);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        magTextView.setText(String.valueOf(currentWord.get_mag()));

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView placeTextView1 = (TextView) listItemView.findViewById(R.id.placeid1);
        TextView placeTextView2 = (TextView) listItemView.findViewById(R.id.placeid2);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        String pp=currentWord.get_place();

        if(pp.contains("of")) {
            int i = pp.indexOf("of");
            String aa = pp.substring(0, i + 2);
            String bb = pp.substring(i + 2, pp.length());

            placeTextView1.setText(aa);
            placeTextView2.setText(bb);

        }

        else{
            placeTextView1.setText("");
            placeTextView2.setText(pp);
        }

        TextView datee = (TextView) listItemView.findViewById(R.id.dateid);
        TextView timee = (TextView) listItemView.findViewById(R.id.timeid);

        datee.setText(currentWord.get_date());
        timee.setText(currentWord.get_time());


       String url= currentWord.get_url();


        return listItemView;
    }

}

