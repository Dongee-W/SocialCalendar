package com.coral.cute.socialcalendar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FriendRequest {
    public ArrayList<String> userIDs = new ArrayList();

    public FriendRequest(String jsonAllFriend) {
        try {
            JSONArray ja = new JSONArray(jsonAllFriend);


            for(int i=0;i<ja.length();i++){
                userIDs.add(ja.getJSONObject(i).getString("Invite"));
            }

        } catch (JSONException e) {
            System.out.println("JSON error");
        }
    }
}
