package com.coral.cute.socialcalendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainPageFrag extends Fragment {

    private String userID;
    ArrayAdapter mForecastAdapter;
    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_page_layout, container, false);



        final com.prolificinteractive.materialcalendarview.MaterialCalendarView cal =
                (com.prolificinteractive.materialcalendarview.MaterialCalendarView) view.findViewById(R.id.calendarView);

        cal.setOnDateChangedListener(
                new com.prolificinteractive.materialcalendarview.OnDateChangedListener(){
                    @Override
                    public void onDateChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                        String day = calendarDay.toString();
                        Intent intent = new Intent(getActivity(), DailyDetail.class).putExtra(Intent.EXTRA_TEXT, day);
                        startActivity(intent);
                    }
                }
        );


        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "Study Android     May 12, 2015  3:02-9:05",
                "Return toy     May 13, 2015  10:00-12:00",
                "See Avengers     May 13, 2015  13:00-14:00",
                "Grand Budapest     May 13, 2015  13:00-19:00",
                "Visit London     May 14, 2015  20:00-20:47",
                "Sing music     May 14, 2015  13:00-19:00",
                "Lord of rings     May 14, 2015  13:20-19:34"
        };
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));


        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_todo, // The name of the layout ID.
                        R.id.list_item_todo_textview, // The ID of the textview to populate.
                        weekForecast);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) view.findViewById(R.id.listview_todo);
        listView.setAdapter(mForecastAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            userID = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.i("yy", userID);
        }
        FetchDataTask fdt = new FetchDataTask();
        fdt.execute("yes");
    }

    /** Fetch data from server */
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
            String dayString = "{\"day\": {\"year\": 2015, \"month\": 5, \"date\": 18}, \"todo\":";
            String todo = getWebServre("GetDay", "UserID=gaga&Year=2015&Month=05&Date=21");
            String total = dayString + todo + "}";
            //Log.e("sdf",total);
            DailyList myDL = new DailyList(total);
            return myDL.eventStringArray();
        }

        @Override
        protected void onPostExecute(List<String> result) {
            mForecastAdapter.clear();
            for(String dayForecastStr : result) {
                mForecastAdapter.add(dayForecastStr);
            }
        }
    }

}
