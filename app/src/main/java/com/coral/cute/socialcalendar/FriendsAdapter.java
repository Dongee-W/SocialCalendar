
package com.coral.cute.socialcalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FriendsAdapter extends BaseAdapter {

    private Activity activity;
    public String userID;
    private List<String> data;
    private boolean isE;
    private static LayoutInflater inflater=null;


    public FriendsAdapter(Activity a, List<String> d, boolean isEmpty) {
        activity = a;
        data=d;
        isE = isEmpty;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        final int jjj = position;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_item_friends, null);

        /** Get user id */
        Intent intent = activity.getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            userID = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        TextView text=(TextView)vi.findViewById(R.id.list_item_friend_main);
        final ImageView image=(ImageView)vi.findViewById(R.id.image);

        if (isE) {
            image.setImageResource(R.drawable.ph);
        } else {
            if(position == 0)
                image.setImageResource(R.drawable.pi5);
            else if(position == 1)
                image.setImageResource(R.drawable.pi6);
            else if(position == 2)
                image.setImageResource(R.drawable.pi7);
            else if(position == 3)
                image.setImageResource(R.drawable.pi1);
            else if(position == 4)
                image.setImageResource(R.drawable.pi2);
            else if(position == 5)
                image.setImageResource(R.drawable.pi3);
            else
                image.setImageResource(R.drawable.pi4);
        }


        text.setText(data.get(position));

        return vi;
    }

}
