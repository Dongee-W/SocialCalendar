package com.coral.cute.socialcalendar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Event {
    public String remark;
    public int eventMode;
    public Calendar start, end;
    private int year = 2015, month = 5, date = 15;


    public Event(String dataString) {

        try {
            JSONObject jo = new JSONObject(dataString);

            start = Calendar.getInstance();
            start.set(year, month, date, jo.getInt("startHour"), jo.getInt("startMin"));
            end = Calendar.getInstance();
            end.set(year, month, date, jo.getInt("endHour"), jo.getInt("endMin"));
            eventMode = jo.getInt("eventMode");
            remark = jo.getString("remark");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Event(int eventMode, String remark,int startHourOfDay, int startMinute,
                 int endHourOfDay, int endMinute) {
        this.eventMode = eventMode;
        this.remark = remark;
        start = Calendar.getInstance();
        start.set(year, month, date, startHourOfDay, startMinute);
        end = Calendar.getInstance();
        end.set(year, month, date, endHourOfDay, endMinute);
    }

    /** Convert this Event object to JSON string for storage */
    public JSONObject toJSON(){
        JSONObject newJSON = new JSONObject();
        try {
            newJSON.put("eventMode", eventMode);
            newJSON.put("startHour", start.get(Calendar.HOUR_OF_DAY));
            newJSON.put("startMin", start.get(Calendar.MINUTE));
            newJSON.put("endHour", end.get(Calendar.HOUR_OF_DAY));
            newJSON.put("endMin", end.get(Calendar.MINUTE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newJSON;
    }
    
    public String eventModeToString() {
    	    	switch(eventMode) {
    		case 0: return "Other";
    		case 1: return "Work";
    		case 2: return "Class";
    		case 3: return "Workout";
    		case 4: return "Meal";
    		case 5: return "Homework";
    		case 6: return "Meeting";
    		case 7: return "Dining";
    		case 8: return "Study";
    		case 9: return "Rest";
    		default: return "Error";
    	}
    }

    @Override
    public String toString() {

        String startFormatMinute = String.format("%02d", start.get(Calendar.MINUTE));
        String endFormatMinute = String.format("%02d", end.get(Calendar.MINUTE));
        return this.eventModeToString() + "  "+ start.get(Calendar.HOUR_OF_DAY) +
                ":" + startFormatMinute + "-" +
                end.get(Calendar.HOUR_OF_DAY) +
                ":" + endFormatMinute;
    }

    public static void main(String[] args) {
        String kk = "{\"startMin\":12,\"eventMode\":\"Say Happy\",\"startHour\":2,\"endMin\":5,\"endHour\":23}";
        Event gg = new Event(kk);
        System.out.println(gg.toJSON());
    }

}
