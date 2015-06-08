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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DetailFragment extends Fragment {

    ListView listView;
    private String userID;
    String date;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.daily_detail, container, false);
        // The detail Activity called via intent.  Inspect the intent for forecast data.

        // Create some dummy data for the ListView.
        String[] data = {
                "Study Android     May 12, 2015  3:02-9:05",
                "Return toy     May 13, 2015  10:00-12:00",
                "See Avengers     May 13, 2015  13:00-14:00",
                "Grand Budapest     May 13, 2015  13:00-19:00",
                "Visit London     May 14, 2015  20:00-20:47",
                "Sing music     May 14, 2015  13:00-19:00",
                "Lord of rings     May 14, 2015  13:20-19:34"
        };
        List<String> todayList = new ArrayList<String>(Arrays.asList(data));
        List<Integer> gggg = new ArrayList<Integer>(){{
            add(0);
            add(1);
            add(2);
            add(3);
            add(6);
            add(5);
            add(4);
        }};

        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) rootView.findViewById(R.id.listview_todo);
        List<String> placeholder = new ArrayList<String>();
        placeholder.add("Check Internet Connection");
        LazyAdapter adapter = new LazyAdapter(getActivity(),
                todayList, gggg, placeholder);
        listView.setAdapter(adapter);
        //listView.setAdapter(mForecastAdapter);
        setListViewHeightBasedOnChildren(listView);

        ((TextView) rootView.findViewById(R.id.detail_text))
                .setText(date);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        /** Get current user */
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("USERID");
            date = intent.getStringExtra("DATE");
        }
        /** Get today's date */
        Calendar c = Calendar.getInstance();
        FetchDataTask fdt = new FetchDataTask();
        fdt.execute(userID, Integer.toString(c.get(Calendar.YEAR)),
                Integer.toString(c.get(Calendar.MONTH) + 1),
                Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /** Fetch today's list from server */
    public class FetchDataTask extends AsyncTask<String, Void, DailyList> {

        public String getWebServre(String Action, String Para) {

            String serverUrl = "http://140.116.86.54/Public/AI_Account.aspx?";

            try {
                URL url = new URL(serverUrl + "Action=" + Action + "&" + Para);
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
        protected DailyList doInBackground(String... params) {
            String dayString = "{\"day\": {\"year\": " + params[1] +
                    ", \"month\": " + params[2] +", \"date\": " +
                    params[3] + "}, \"todo\":";
            String todo = getWebServre("GetDay", "UserID=" + params[0] + "&Year=" +
                    params[1] + "&Month=" + params[2] +
                    "&Date=" + params[3]);
            //String todo = getWebServre("GetDay", "UserID=3&Year=2015&Month=2&Date=20");
            String total = dayString + todo + "}";
            return new DailyList(total);
        }

        @Override
        protected void onPostExecute(DailyList result) {
            /** Check if there are data for today */
            if (result.eventStringArray().size() == 0) {
                List<String> error = new ArrayList<String>();
                error.add("No data yet.");
                List<Integer> errorcode = new ArrayList<Integer>();
                errorcode.add(111);
                List<String> errorRemark = new ArrayList<String>();
                errorRemark.add("Check Internet Connection");
                LazyAdapter adapter = new LazyAdapter(getActivity(), error,
                        errorcode, errorRemark);
                listView.setAdapter(adapter);
            } else {
                LazyAdapter adapter = new LazyAdapter(getActivity(),
                        result.eventStringArray(),
                        result.eventTypeArray(),
                        result.eventRemarkArray());
                listView.setAdapter(adapter);
                setListViewHeightBasedOnChildren(listView);

            }

        }
    }

}
