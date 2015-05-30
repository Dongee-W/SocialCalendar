package com.coral.cute.socialcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendPageFrag extends Fragment {
    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friend_page_layout, container, false);

        List<String> weekForecast = new ArrayList<String>();
        ArrayAdapter friendAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_todo, // The name of the layout ID.
                        R.id.list_item_todo_textview, // The ID of the textview to populate.
                        weekForecast);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) view.findViewById(R.id.listview_friend);
        listView.setAdapter(friendAdapter);


        return view;
    }
}
