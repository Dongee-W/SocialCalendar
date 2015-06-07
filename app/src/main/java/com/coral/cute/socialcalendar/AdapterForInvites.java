
package com.coral.cute.socialcalendar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterForInvites extends BaseAdapter {

    private Activity activity;
    private List<String> data;
    private static LayoutInflater inflater=null;


    public AdapterForInvites(Activity a, List<String> d) {
        activity = a;
        data=d;
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
            vi = inflater.inflate(R.layout.list_item_friend, null);

        TextView text=(TextView)vi.findViewById(R.id.list_item_friend_main);
        ImageView image=(ImageView)vi.findViewById(R.id.image);
        text.setText(data.get(position));

        if(position == 0)
            image.setImageResource(R.drawable.pi1);
        else if(position == 1)
            image.setImageResource(R.drawable.pi2);
        else if(position == 2)
            image.setImageResource(R.drawable.pi3);
        else
            image.setImageResource(R.drawable.pi4);


        return vi;
    }
}
