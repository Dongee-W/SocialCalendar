
package com.coral.cute.socialcalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AdapterForInvites extends BaseAdapter {

    private Activity activity;
    public String userID;
    private List<String> data;
    private int isE;
    private static LayoutInflater inflater=null;


    public AdapterForInvites(Activity a, List<String> d, int isEmpty) {
        activity = a;
        data=d;
        isE = isEmpty;
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

    public void remove(int position) {
        data.remove(position);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        final int jjj = position;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_item_friend, null);

        /** Get user id */
        Intent intent = activity.getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            userID = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        TextView text=(TextView)vi.findViewById(R.id.list_item_friend_main);
        final ImageView image=(ImageView)vi.findViewById(R.id.image);

        final Button acc = (Button)vi.findViewById(R.id.accept);
        final Button dec = (Button)vi.findViewById(R.id.decline);
        acc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AnswerInviteTask fdt = new AnswerInviteTask();
                fdt.execute(userID, data.get(jjj), "1");
                remove(position);
                if (data.size() == 0) {
                    isE = 1;
                    data.add("No new friends request!");
                    dec.setVisibility(View.GONE);
                    acc.setVisibility(View.GONE);
                    image.setImageResource(R.drawable.ph);
                }
                notifyDataSetChanged();

            }
        });

        dec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AnswerInviteTask fdt = new AnswerInviteTask();
                fdt.execute(userID, data.get(jjj), "0");
                remove(position);
                if (data.size() == 0) {
                    isE = 1;
                    data.add("No new friends request!");
                    dec.setVisibility(View.GONE);
                    acc.setVisibility(View.GONE);
                    image.setImageResource(R.drawable.ph);
                }
                notifyDataSetChanged();
            }
        });

        if (isE == 1) {
            dec.setVisibility(View.GONE);
            acc.setVisibility(View.GONE);
            image.setImageResource(R.drawable.ph);
        } else {
            if(position == 0)
                image.setImageResource(R.drawable.pi1);
            else if(position == 1)
                image.setImageResource(R.drawable.pi2);
            else if(position == 2)
                image.setImageResource(R.drawable.pi3);
            else
                image.setImageResource(R.drawable.pi4);
        }
        text.setText(data.get(position));







        return vi;
    }

    public class AnswerInviteTask extends AsyncTask<String, Void, String> {

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
            return getWebServre("AnswerInvite", "User1=" + params[0] + "&User2=" + params[1] +
                    "&Answer=" + params[2]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Sucess0"))
                Toast.makeText(activity,
                        "Friend request declined.",
                        Toast.LENGTH_LONG).show();
            else Toast.makeText(activity,
                    "Friend request accepted.",
                    Toast.LENGTH_LONG).show();
        }

    }
}
