package com.coral.cute.socialcalendar;

import android.app.Activity;
import android.content.Context;
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

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private List<String> data, remarks;
    private List<Integer> type;
    private static LayoutInflater inflater=null;

    
    public LazyAdapter(Activity a, List<String> d, List<Integer> i, List<String> r) {
        activity = a;
        data=d;
        type = i;
        remarks = r;
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
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_item_todo, null);

        TextView text=(TextView)vi.findViewById(R.id.list_item_todo_textview);
        TextView remark = (TextView)vi.findViewById(R.id.list_item_remark_textview);
        ImageView image=(ImageView)vi.findViewById(R.id.image);
        text.setText(data.get(position));
        if(remarks.size() <= position)
            remark.setText("No remark on this one.");
        else
            remark.setText(remarks.get(position));

        if(type.get(position) == 0)
            image.setImageResource(R.drawable.event_party);
        else if(type.get(position) == 1)
            image.setImageResource(R.drawable.event_work);
        else if(type.get(position) == 2)
            image.setImageResource(R.drawable.event_class);
        else if(type.get(position) == 3)
            image.setImageResource(R.drawable.event_workout);
        else if(type.get(position) == 4)
            image.setImageResource(R.drawable.event_meal);
        else if(type.get(position) == 5)
            image.setImageResource(R.drawable.event_hw);
        else if(type.get(position) == 6)
            image.setImageResource(R.drawable.event_meeting);
        else if(type.get(position) == 7)
            image.setImageResource(R.drawable.event_movie);
        else if(type.get(position) == 8)
            image.setImageResource(R.drawable.event_study);
        else if(type.get(position) == 9)
            image.setImageResource(R.drawable.event_sleep);
        else
            image.setImageResource(R.drawable.event_error);




        return vi;
    }


}