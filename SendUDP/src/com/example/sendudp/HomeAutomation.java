package com.example.sendudp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class HomeAutomation extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_automation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_automation, menu);
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
	boolean bulbOn = false;
	
	// Change button state
	public void toggleState(View view) {
		ImageButton ib1 = (ImageButton)findViewById(R.id.imageButton1);
		if(bulbOn) {
			ib1.setImageResource(R.drawable.lightbulb_off);
			bulbOn = false;
		}
		else {
			ib1.setImageResource(R.drawable.lightbulb_on);
			bulbOn = true;
		}
    }
}
