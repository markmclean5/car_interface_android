package com.example.sendudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.example.sendudp.MainActivity.ReceiveTask;
import com.example.sendudp.MainActivity.TransmitTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
		SharedPreferences pref1 = getApplicationContext().getSharedPreferences("SendUDP", MODE_PRIVATE); 


		if(bulbOn) {
			ib1.setImageResource(R.drawable.lightbulb_off);
			bulbOn = false;
			String transmitData = "";
			transmitData = pref1.getString("deviceName", "CONTROLLER") + ": TURN BULB OFF";
	    	new TransmitTask().execute(transmitData);
		}
		else {
			ib1.setImageResource(R.drawable.lightbulb_on);
			bulbOn = true;
			String transmitData = "";
			transmitData = pref1.getString("deviceName", "CONTROLLER") + ": TURN BULB ON";

	    	new TransmitTask().execute(transmitData);
		}
    }
	
	//TransmitTask: Used to transmit UDP messages
	// doInBackground: calls runUdpClient() which sends the data
	// onPostExcecute: handles UI
	public class TransmitTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			String udpMsg = params[0];
			runUdpClient(udpMsg);
			return udpMsg;
		}
		protected void onPostExecute(String result) {
	    	System.out.println("Data sent: " + result + " \n");
	    }
	    private String runUdpClient(String input)  {
	    	final int UDP_SERVER_PORT = 45678;
	        DatagramSocket ds = null;
	        try {
	            ds = new DatagramSocket();
	            InetAddress serverAddr = InetAddress.getByName("10.0.0.19");
	            DatagramPacket dp;
	            dp = new DatagramPacket(input.getBytes(), input.length(), serverAddr, UDP_SERVER_PORT);
	            ds.send(dp);
	            return(input);
	        } catch (SocketException e) {
	        	System.out.println("Socket Exception");
	            e.printStackTrace();
	            return "Socket Exception";
	        }catch (UnknownHostException e) {
	        	System.out.println("Unknown Host Exception");
	        	e.printStackTrace();
	        	return "Unknown Host Exception";
	        } catch (IOException e) {
	        	System.out.println("IO Exception");
	            e.printStackTrace();
	            return "IO Exception";
	        } catch (Exception e) {
	        	System.out.println("Regular Exception");
	            e.printStackTrace();
	            return "Regular Exception";
	        } finally {
	            if (ds != null) {
	                ds.close();
	            }
	        }
	    }
	}
}




