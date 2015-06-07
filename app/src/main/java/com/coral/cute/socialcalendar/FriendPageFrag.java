package com.coral.cute.socialcalendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendPageFrag extends Fragment {

    private String userID;
    ArrayAdapter friendAdapter;
    private Button addFriendBtn;
    private TextView friendID;
    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friend_page_layout, container, false);

        friendID = (TextView) view.findViewById(R.id.friendID);

        addFriendBtn = (Button) view.findViewById(R.id.addFriend);
        addFriendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InviteFriendTask ift = new InviteFriendTask();
                ift.execute(userID, friendID.getText().toString());
            }
        });

        List<String> weekForecast = new ArrayList<String>();
        friendAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_friend, // The name of the layout ID.
                        R.id.list_item_friend_main, // The ID of the textview to populate.
                        weekForecast);



        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) view.findViewById(R.id.listview_friend);
        listView.setAdapter(friendAdapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            userID = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.i("pp", userID);
        }
        FetchDataTask fdt = new FetchDataTask();
        fdt.execute(userID);
    }


    /** Fetch friend request from server */
    public class FetchDataTask extends AsyncTask<String, Void, List<String>> {

        public String getWebServre(String Action, String Para) {


            String ServerUrl = "http://140.116.86.54/Public/AI_Account.aspx?";

            String requestData = "Fail";


            try {
                URL url = new URL(ServerUrl + "Action=" + Action + "&" + Para);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setConnectTimeout(6000);
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
                con.connect();


                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        con.getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                reader.close();
                // json
                //String jsonString = jsonString1;
                //JSONArray jsonarr = new JSONArray(jsonString);
                //requestData = jsonObj.getJSONObject("ID") + "";

                //requestData = jsonarr.getJSONObject(0).getString("remark")+"";

                return jsonString;

            } catch (Exception e) {
                return e.toString();
            }
        }

        @Override
        protected List<String> doInBackground(String... params) {
            String friends = getWebServre("GetFriendList", "UserID=" + params[0]);
            //Log.e("sdf",total);
            Friends fs = new Friends(friends);
            return fs.userIDs;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            friendAdapter.clear();
            if (result.size() == 0) {
                friendAdapter.add("No friend yet.");
            } else {
                for(String friendStr : result) {
                    friendAdapter.add(friendStr);
                }
            }

        }
    }

    public class InviteFriendTask extends AsyncTask<String, Void, String> {

        public String getWebServre(String Action, String Para) {


            String ServerUrl = "http://140.116.86.54/Public/AI_Account.aspx?";

            try {
                URL url = new URL(ServerUrl + "Action=" + Action + "&" + Para);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setConnectTimeout(6000);
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
                con.connect();


                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        con.getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                reader.close();
                return jsonString;

            } catch (Exception e) {
                return e.toString();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return getWebServre("FriendInvite", "User1=" + params[0] + "&User2=" + params[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Success"))
                Toast.makeText(getActivity(),
                        "Friend request sended.",
                        Toast.LENGTH_LONG).show();
            else Toast.makeText(getActivity(),
                    "Error",
                    Toast.LENGTH_LONG).show();

        }

    }
}
