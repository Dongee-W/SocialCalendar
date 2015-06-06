package com.coral.cute.socialcalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class DailyList {
    public Calendar today;
    public List<Event> toDoList;

    public DailyList(String jsonDaily){

        try {
            JSONObject jo = new JSONObject(jsonDaily);
            JSONObject jsonDay = jo.getJSONObject("day");
            int year = jsonDay.getInt("year");
            int month = jsonDay.getInt("month");
            int date = jsonDay.getInt("date");
            today = Calendar.getInstance();
            today.set(year, month, date);

            toDoList = new ArrayList<Event>();
            JSONArray ja = jo.getJSONArray("todo");

            for(int i=0;i<ja.length();i++){
                Event et = new Event(ja.getJSONObject(i).toString());
                toDoList.add(et);
            }

        } catch (JSONException e) {
            System.out.println("JSON error");
        }

    }

    /** Convert this DailyList object to JSON string for storage */
    public JSONObject toJSON(){
        JSONObject newJSON = new JSONObject();
        JSONObject dayObj = new JSONObject();
        JSONArray newJA = new JSONArray();
        try {
            /**Construct day object */
            dayObj.put("year", today.get(Calendar.YEAR));
            dayObj.put("month", today.get(Calendar.MONTH));
            dayObj.put("date", today.get(Calendar.DATE));

            newJSON.put("day", dayObj);

            for (Event ev:toDoList) {
                newJA.put(ev.toJSON());
            }
            newJSON.put("todo", newJA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newJSON;
    }

    public List<String> eventStringArray() {
    	List<String> newSA = new ArrayList<String>();
        Locale locale = Locale.getDefault();

    	for (Event event: toDoList) {
    		newSA.add(event.toString() + ", " +
                    today.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale));
        }
    	
    	return newSA;
    }

    public List<String> eventRemarkArray() {
        List<String> newSA = new ArrayList<String>();
        for (Event event: toDoList) {
            newSA.add("Remark: " + event.remark);
        }

        return newSA;
    }

    public List<Integer> eventTypeArray() {
        List<Integer> types = new ArrayList<>();
        for (Event event: toDoList) {
            types.add(event.eventMode);
        }

        return types;
    }
    
    @Override
    public String toString() {
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int date = today.get(Calendar.DATE);
        String base = "Date: " + String.valueOf(year) +
                "." + String.valueOf(month) +
                "." + String.valueOf(date) + "\n";
        for (Event event: toDoList) {
            base += event.toString() + "\n";
        }

        return base;
    }

    public void addEvent(Event event) {
        toDoList.add(event);
    }

    public void removeEvent(int index) {
        toDoList.remove(index);
    }



    public static void main(String[] args) {
        String input = "{\"day\":{\"year\":2015,\"month\":5,\"date\":18},\"todo\":[{\"startMin\":12,\"name\":\"Say Happy\",\"startHour\":2,\"endMin\":5,\"endHour\":23},{\"startMin\":11,\"name\":\"Coding\",\"startHour\":2,\"endMin\":5,\"endHour\":22}]}";
        System.out.println(input);
        DailyList gg = new DailyList(input);
        System.out.println(gg.eventStringArray());
    }
}
