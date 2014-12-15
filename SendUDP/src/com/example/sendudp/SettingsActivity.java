package com.example.sendudp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		SharedPreferences pref = getApplicationContext().getSharedPreferences("SendUDP", MODE_PRIVATE); 
        EditText text0 = (EditText)findViewById(R.id.editText0);
		EditText text1 = (EditText)findViewById(R.id.editText1);
		EditText text2 = (EditText)findViewById(R.id.editText2);
		EditText text3 = (EditText)findViewById(R.id.editText3);
		EditText text4 = (EditText)findViewById(R.id.deviceNameEditText);
		
		text0.setText(pref.getString("server_ip_0", "0"));
		text1.setText(pref.getString("server_ip_1", "1"));
		text2.setText(pref.getString("server_ip_2", "2"));
		text3.setText(pref.getString("server_ip_3", "3"));
		text4.setText(pref.getString("deviceName", "CONTROLLER"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setServerIp(View view) {
		EditText text0 = (EditText)findViewById(R.id.editText0);
		EditText text1 = (EditText)findViewById(R.id.editText1);
		EditText text2 = (EditText)findViewById(R.id.editText2);
		EditText text3 = (EditText)findViewById(R.id.editText3);
		
		// Validate input - set to 255 if greater than 255
		int check = 0;
		check = Integer.parseInt(text0.getText().toString());
		if(check>255) check = 255;
		text0.setText(Integer.toString(check));
		
		check = Integer.parseInt(text1.getText().toString());
		if(check>255) check = 255;
		text1.setText(Integer.toString(check));
		
		check = Integer.parseInt(text2.getText().toString());
		if(check>255) check = 255;
		text2.setText(Integer.toString(check));
		
		check = Integer.parseInt(text3.getText().toString());
		if(check>255) check = 255;
		text3.setText(Integer.toString(check));
        
        // Save server IP address keys to SharedPreferences
		SharedPreferences pref = getApplicationContext().getSharedPreferences("SendUDP", MODE_PRIVATE); 
        Editor editor = pref.edit();
		editor.putString("server_ip_0", text0.getText().toString());
		editor.putString("server_ip_1", text1.getText().toString());
		editor.putString("server_ip_2", text2.getText().toString());
		editor.putString("server_ip_3", text3.getText().toString());
		String serverIpAddress = 	text0.getText().toString() + "." +
									text1.getText().toString() + "." +
									text2.getText().toString() + "." +
									text3.getText().toString();
		editor.putString("server_ip", serverIpAddress);
		editor.commit();
		
	}
	
	public void setDeviceName(View view) {
		EditText text4 = (EditText)findViewById(R.id.deviceNameEditText);
		SharedPreferences pref = getApplicationContext().getSharedPreferences("SendUDP", MODE_PRIVATE); 
        Editor editor = pref.edit();
        editor.putString("deviceName", text4.getText().toString());
        editor.commit();
	}
}