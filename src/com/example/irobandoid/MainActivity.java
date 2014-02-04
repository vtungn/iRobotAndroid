package com.example.irobandoid;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.bluetooth.*;
import android.content.Intent;

public class MainActivity extends Activity {
	
	private static final String TAG = "TungBT";
	
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;
	private Set<BluetoothDevice> pairedDevices;
	private ListView lv;
	private OutputStream outStream = null;
	
	// SPP UUID service
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	// MAC address of FireFly
	private static String address = "00:06:66:0A:AB:27";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView)findViewById(R.id.listView1);
		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		pairedDevices = btAdapter.getBondedDevices();
	      

		Log.e(TAG, "+++DONE CREATE +++");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "+ ON RESUME +");
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
        	Log.e(TAG, "ON RESUME: Socket creation failed.", e);
        }
        //blocking connect
        btAdapter.cancelDiscovery();
        
        Log.d(TAG, "+ ON RESUME : connect socket +");
        try {
            btSocket.connect();
            Log.e(TAG, "ON RESUME: BT connection established, data transfer link open.");
        } catch (IOException e) {
        	try {
        		btSocket.close();
        	} catch (IOException e2) {
        		Log.e(TAG, "ON RESUME: Unable to close socket during connection failure", e2);
        	}
        }
        
        //say something to server
        Log.d(TAG, "+ DONE RESUME ready to say sth +");
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "ON SEND MSG: Output stream creation failed.", e);
        }
	}
        

	public void searchDevClick(View v)
	{
	  	
	  	if (!btAdapter.isEnabled()) {
	         Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	         startActivityForResult(turnOn, 0);
	         Toast.makeText(getApplicationContext(),"Turning on..." 
	        		 ,Toast.LENGTH_LONG).show();
	      }
	      {
//	    	 Intent getVisible = new Intent(BluetoothAdapter.
//	    		      ACTION_REQUEST_DISCOVERABLE);
//	    		      startActivityForResult(getVisible, 0);
	    	  Toast.makeText(getApplicationContext(),"Show Paired Devices",
	    		         Toast.LENGTH_SHORT).show();
	         ArrayList list = new ArrayList();
	         
	         for(BluetoothDevice bt : pairedDevices)
	        	 list.add(bt.getName());
	         	//textView.setText(bt.getName());
//	         if(pairedDevices != null)
//	        	 list.add(pairedDevices.getName());
//	         else
//	        	 Toast.makeText(getApplicationContext(),"NO Paired Devices",
//	    		         Toast.LENGTH_SHORT).show();
	         final ArrayAdapter adapter = new ArrayAdapter
	         	(this,android.R.layout.simple_list_item_1, list);
	         lv.setAdapter(adapter);
	         
	         //v.setEnabled(false);
	      }
	}
	public void sendCommandtoiRobot(char msg){
		try {
            outStream.write(msg);
        } catch (IOException e) {
            Log.e(TAG, "ON SEND MSGE: Exception during write.", e);
        }
	}
	public void getControlDev(View v) throws IOException
	{
		Toast.makeText(getApplicationContext(),"Beep",
		         Toast.LENGTH_SHORT).show();
		Log.d(TAG, "...Data to send: " + "128" + "...");
		
		Log.d(TAG, "+ send msg +");
        
        
//        String message = "abc";
        char charmsg = 128;
        
//        byte[] msgBuffer = message.getBytes();
//        byte byteMSG = (byte)-128;
//        int intMSG = 128;
//        byte byteint = (byte) intMSG;
//        Log.d(TAG, "byte:"+ byteMSG + ",int:"+intMSG +",my mess:"+ msgBuffer);
        sendCommandtoiRobot(charmsg);
        charmsg = 132;
        sendCommandtoiRobot(charmsg);
        
		
	}
	public void CtrlRight(View v) throws IOException
	{	
		char msg = 137;
		sendCommandtoiRobot(msg);
		msg = 0;
		sendCommandtoiRobot(msg);
		msg = 200;
		sendCommandtoiRobot(msg);
	}
	public void CtrlUP(View v) throws IOException
	{	
		char msg = 137;
		sendCommandtoiRobot(msg);
		msg = 200;
		sendCommandtoiRobot(msg);
		msg = 200;
		sendCommandtoiRobot(msg);
	}
	public void CtrlLeft(View v) throws IOException
	{	
		
	}
	public void CtrlDown(View v) throws IOException
	{	
		char msg = 137;
		sendCommandtoiRobot(msg);
		msg = 200;
		sendCommandtoiRobot(msg);
		msg = 0;
		sendCommandtoiRobot(msg);
	}
}
