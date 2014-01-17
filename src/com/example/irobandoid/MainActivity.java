package com.example.irobandoid;

import java.util.ArrayList;
import java.util.Set;
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
	
	private static final String TAG = "bluetooth2";
	
	private BluetoothAdapter btAdapter;
	private BluetoothDevice pairedDevices;
	private ListView lv;
	
	// SPP UUID service
	//private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView)findViewById(R.id.listView1);
		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		
	    
	    Set<BluetoothDevice> setbtDev;
	    setbtDev = btAdapter.getBondedDevices();
	    for(BluetoothDevice bt : setbtDev)
	         pairedDevices = bt;

	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	         //TextView textView = (TextView)findViewById(R.id.textView2);
	         
	         	//textView.setText(bt.getName());
	         list.add(pairedDevices.getName());
	            //Log.v(bt.getName()+ "", null);

	         final ArrayAdapter adapter = new ArrayAdapter
	         	(this,android.R.layout.simple_list_item_1, list);
	         lv.setAdapter(adapter);
	         
	         v.setEnabled(false);
	      }
	}
	public void getControlDev(View v)
	{
		Toast.makeText(getApplicationContext(),"Beep",
		         Toast.LENGTH_SHORT).show();
		Log.d(TAG, "...Data to send: " + "128" + "...");
	}
	

}
