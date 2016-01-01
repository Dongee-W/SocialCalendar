package com.coral.cute.socialcalendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class EventForm extends ActionBarActivity {

    private TimePicker timePicker1;
    private PopupWindow mPopupWindow, mPopupWindow2;
    FrameLayout layout_MainMenu;
    int startHour, startMin, endHour, endMin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
        final EditText eventMode = (EditText)findViewById(R.id.event_type);
        final EditText eventRemark = (EditText)findViewById(R.id.event_remark);
        Button startTimeButton = (Button)findViewById(R.id.start);
        Button endTimeButton = (Button)findViewById(R.id.end);
        Button addEventButton = (Button)findViewById(R.id.add_event);
        final Switch isPublic = (Switch)findViewById(R.id.privacy_state);

        layout_MainMenu = (FrameLayout) findViewById(R.id.mainmenu);
        layout_MainMenu.getForeground().setAlpha(0);


        startTimeButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                getPopupWindowInstance();
                mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                layout_MainMenu.getForeground().setAlpha(220);
            }

        });
        endTimeButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                getPopupWindowInstance2();
                mPopupWindow2.showAtLocation(v, Gravity.CENTER, 0, 0);
                layout_MainMenu.getForeground().setAlpha(200);

            }

        });
        addEventButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                SaveEventTask set = new SaveEventTask();
                set.execute("1", "2015", "6", "8",
                        Integer.toString(startHour), Integer.toString(startMin),
                        Integer.toString(endHour), Integer.toString(endMin),
                        eventMode.getText().toString(),
                        eventRemark.getText().toString().replaceAll(" ", "%20"),
                        (isPublic.isChecked()) ? "1" : "0");

                Intent intent = new Intent(EventForm.this, DailyDetail.class);
                startActivity(intent);
            }

        });
    }

    private void getPopupWindowInstance() {
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    private void getPopupWindowInstance2() {
        if (null != mPopupWindow) {
            mPopupWindow2.dismiss();
            return;
        } else {
            //Toast.makeText(MainActivity.this, type, Toast.LENGTH_SHORT).show();
            initPopuptWindow2();
        }
    }

    private void initPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View popupWindow = layoutInflater.inflate(R.layout.popup, null);

        timePicker1 = (TimePicker) popupWindow.findViewById(R.id.timePicker);
        Button confirm = (Button)popupWindow.findViewById(R.id.button2);

        confirm.setOnClickListener(new Button.OnClickListener(){

            @Override

            public void onClick(View v) {
                mPopupWindow.dismiss();
                layout_MainMenu.getForeground().setAlpha(0);
            }

        });

        mPopupWindow = new PopupWindow(popupWindow, 750, 1250, true);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                startHour = timePicker1.getCurrentHour();
                startMin = timePicker1.getCurrentMinute();
                String dds = "Start time " + startHour + ":" + startMin;
                Toast.makeText(popupWindow.getContext(), dds , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPopuptWindow2() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View popupWindow = layoutInflater.inflate(R.layout.popup, null);

        timePicker1 = (TimePicker) popupWindow.findViewById(R.id.timePicker);
        Button confirm = (Button)popupWindow.findViewById(R.id.button2);

        confirm.setOnClickListener(new Button.OnClickListener(){

            @Override

            public void onClick(View v) {
                mPopupWindow2.dismiss();
                layout_MainMenu.getForeground().setAlpha(0);
            }

        });

        mPopupWindow2 = new PopupWindow(popupWindow, 750, 1250, true);

        mPopupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                endHour = timePicker1.getCurrentHour();
                endMin = timePicker1.getCurrentMinute();
                String dds2 = "End time " + endHour + ":" + endMin;
                Toast.makeText(popupWindow.getContext(), dds2 , Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SaveEventTask extends AsyncTask<String, Void, String> {

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
            String dd = "UserID=" + params[0] + "&Year=" + params[1] +
                    "&Month=" + params[2] + "&Day=" + params[3] +
                    "&BeginHour=" + params[4] + "&BeginMin=" + params[5] +
                    "&EndHour=" + params[6] + "&EndMin=" + params[7] +
                    "&EventMode=" + params[8] + "&Remark=" + params[9] +
                    "&Public=" + params[10];
            //return dd;
            return getWebServre("SaveEvent", dd);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(EventForm.this, "Event Saved!", Toast.LENGTH_SHORT).show();
        }

    }
}
