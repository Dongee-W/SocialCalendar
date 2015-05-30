package com.coral.cute.socialcalendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class LoginRegister extends ActionBarActivity {

    private Button loginBtn;
    private TextView accountTf;
    private TextView passwordTf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        accountTf = (TextView) findViewById(R.id.accountid);
        passwordTf = (TextView) findViewById(R.id.password);

        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LoginTask lrt = new LoginTask();
                lrt.execute(accountTf.getText().toString(), passwordTf.getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_register, menu);
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

    /** Fetch data from server */
    public class LoginTask extends AsyncTask<String, Void, String> {

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
        protected String doInBackground(String... params) {
            return getWebServre("Login", "UserID=" + params[0] + "&Pwd=" + params[1] + "");
        }

        @Override
        protected void onPostExecute(String result) {
            if(!result.isEmpty()) {
                if(result.equals("true")) {
                    Intent intent = new Intent(LoginRegister.this, MainActivity.class).putExtra(Intent.EXTRA_TEXT, accountTf.getText().toString());
                    startActivity(intent);
                }else if(result.equals("false")){
                    Toast.makeText(LoginRegister.this, "Wrong", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginRegister.this, result, Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(LoginRegister.this, "No Data Return", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
}
