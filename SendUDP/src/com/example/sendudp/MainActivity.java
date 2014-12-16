package com.example.sendudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
		
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    // closeApp: Executes when exit button is pressed
    // Closes the application
    public void closeApp(View view) {
    	System.exit(0);
    }
    
    // sendMessage: Executes when send button is pressed
    // Launches AsyncTasks needed to separate network from UI
    public void sendMessage(View view) {
    	String transmitData = "RQ";
    	new TransmitTask().execute(transmitData);
        new ReceiveTask().execute();
    }
    
    // graphicsView: Executes when the GL button is pressed
    // Launches OpenGL activity
    public void graphicsView(View view) {
    	Intent intent = new Intent(this, OpenGL.class);
        startActivity(intent);
    }
    
 // drawView: Executes when the GL button is pressed
    // Launches Draw / Canvas activity
    public void drawView(View view) {
    	Intent intent = new Intent(this, Draw.class);
        startActivity(intent);
    }
 // surfaceView: Executes when the GL button is pressed
    // Launches SurfaceView / Canvas activity
    public void surfaceView(View view) {
    	Intent intent = new Intent(this, SurfaceViewActivity.class);
        startActivity(intent);
    }
    
    // homeAutomation: Executes when the Home Automation button is pressed
    // Launches Home Automation activity
    public void homeAutomation(View view) {
    	Intent intent = new Intent(this, HomeAutomation.class);
        startActivity(intent);
    }
    
    // settings: Executes when the Settings button is pressed
    // Launches settings activity
    public void settings(View view) {
    	Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    
	
 // TransmitTask: Used to transmit UDP messages
    // doInBackground: calls runUdpClient() which sends the data
    // onPostExcecute: handles UI
	public class TransmitTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String udpMsg = params[0];
			runUdpClient(udpMsg);
			return udpMsg;
		}
		protected void onPostExecute(String result) {
	    	TextView tv1 = (TextView)findViewById(R.id.textView1);
	    	tv1.append("Data sent: " + result + " \n");
	    }
	    private String runUdpClient(String input)  {
	    	final int UDP_SERVER_PORT = 45678;
	        DatagramSocket ds = null;
	        try {
	            ds = new DatagramSocket();
	            InetAddress serverAddr = InetAddress.getByName("10.0.0.21");
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
	
	// ReceiveTask: Used to receive UDP messages
    // doInBackground: calls runUdpServer() which receives the data
    // onPostExcecute: handles UI
	public class ReceiveTask extends AsyncTask<Void, Void, String>  {
		protected String doInBackground(Void... params) {
			String received = runUdpServer();
			return received;
		}
	    protected void onPostExecute(String input) {
	    	TextView tv2 = (TextView)findViewById(R.id.textView1);
	    	if(input.contains("ERROR")) {
	    		input = "Something bad happened.";
	    	}
	    	long millis = System.currentTimeMillis();
	    	tv2.append("Time: " + Long.toString(millis) + "\n");
	    	tv2.append("Data Received: " + input + "\n");
	    }
	    private String runUdpServer()  {
			String lText;
	    	int MAX_UDP_DATAGRAM_LEN = 50;
	    	int UDP_SERVER_PORT = 45679;
	    	byte[] lMsg = new byte[MAX_UDP_DATAGRAM_LEN];
	    	DatagramPacket dp = new DatagramPacket(lMsg, lMsg.length);
	    	DatagramSocket ds = null;
	    	try {
	        	ds = new DatagramSocket(UDP_SERVER_PORT);
	        	ds.setSoTimeout(2000);
	        	ds.receive(dp);
	        	lText = new String(lMsg, 0, dp.getLength());
	        	Log.i("UDP packet received", lText);
	        	publishProgress();
	        	return lText;
	    	} catch (SocketTimeoutException e) {
	    		e.printStackTrace();
	    		return "ERROR Timeout";
	    	} catch (SocketException e) {
	    		e.printStackTrace();
	    		return "ERROR";
	    	} catch (IOException e) {
	        	e.printStackTrace();
	        	return "ERROR";
	    	} finally {
	        	if (ds != null) {
	            	ds.close();
	        	}
	    	}  	
		}
	}
}
	