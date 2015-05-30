package com.coral.cute.socialcalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Friends {
	public ArrayList<String> userIDs = new ArrayList();
	
	public Friends(String jsonAllFriend) {
        try {
            JSONArray ja = new JSONArray(jsonAllFriend);
            

            for(int i=0;i<ja.length();i++){
                userIDs.add(ja.getJSONObject(i).getString("Friend"));
            }

        } catch (JSONException e) {
            System.out.println("JSON error");
        }
	}
	
	
}
