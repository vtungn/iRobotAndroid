package com.example.irobandoid;

import java.io.IOException;
import android.view.ViewGroup;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.*;
import android.content.Intent;
import android.transition.*;
public class MainActivity extends Activity {
	
	private static final String TAG = "TungBT";
	
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;
	private Set<BluetoothDevice> pairedDevices;
	private ListView lv;
	private OutputStream outStream = null;
	private TextView fwdTextV, rotTextV; 
	private SeekBar fwdBar, rotBar;
	private char fwdSpeed, rotSpeed;
	// SPP UUID service
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	// MAC address of FireFly
	private static String address = "00:06:66:0A:AB:27";
	Scene sceneConnecttoPC;
	ViewGroup rootView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rootlayout);
		lv = (ListView)findViewById(R.id.listView1);
//		fwdTextV = (TextView)findViewById(R.id.FwdProgr);
//		rotTextV = (TextView)findViewById(R.id.RotProgr);
		
		

		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if(!btAdapter.isEnabled()){
			Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(turnOn, 0);
		}
		

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
        
	}
	
    public void setupbluetoothConnect() {
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
	public void drive(char velocity, char rotatespeed) {
		sendCommandtoiRobot((char)137);
		velocity = (char) (velocity * 5);
		sendCommandtoiRobot(toHighBytes(velocity));
		sendCommandtoiRobot(toLowBytes(velocity));
		sendCommandtoiRobot(toHighBytes(rotatespeed));
		sendCommandtoiRobot(toLowBytes(rotatespeed)); 
	}
	
	char toHighBytes(char value ){
//	    """ returns two bytes (ints) in high, low order
//	    whose bits form the input value when interpreted in
//	    two's complement
//	    """
		char eqBitVal;
//	    # if positive or zero, it's OK
	    if (value >= 0)
	        eqBitVal = value;
//	    # if it's negative, I think it is this
	    else
	        eqBitVal = (char) ((1<<16) + value);
	    char charreturn = (char)( (eqBitVal >> 8) & 0xFF );
	    return charreturn;
	}
	char toLowBytes(char value ){
//	    """ returns two bytes (ints) in high, low order
//	    whose bits form the input value when interpreted in
//	    two's complement
//	    """
		char eqBitVal;
//	    # if positive or zero, it's OK
	    if (value >= 0)
	        eqBitVal = value;
//	    # if it's negative, I think it is this
	    else
	        eqBitVal = (char) ((1<<16) + value);
		char charreturn = (char)( eqBitVal & 0xFF  );
	    return charreturn;
	    //return ( (eqBitVal >> 8) & 0xFF, eqBitVal & 0xFF )
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
		drive(rotSpeed,(char) -1);
		Toast.makeText(getApplicationContext(),"CtrlRight",
		         Toast.LENGTH_SHORT).show();
	}
	public void CtrlUP(View v) throws IOException
	{	
		drive(fwdSpeed,(char) 32768);
		Toast.makeText(getApplicationContext(),"CtrlUP",
		         Toast.LENGTH_SHORT).show();
	}
	public void CtrlLeft(View v) throws IOException
	{	
		drive(rotSpeed,(char) 1);
		Toast.makeText(getApplicationContext(),"CtrlLeft",
		         Toast.LENGTH_SHORT).show();
	}
	public void CtrlDown(View v) throws IOException
	{	
		drive((char)-fwdSpeed,(char) 32768);
		Toast.makeText(getApplicationContext(),"CtrlDown",
		         Toast.LENGTH_SHORT).show();
	}
	
	public void ReconnectClick(View v) throws IOException {
		setupbluetoothConnect();
		sendCommandtoiRobot((char)128);
		sendCommandtoiRobot((char)132);
	}
	public void CtrlStop(View v) throws IOException
	{	
		drive((char) 0,(char) 32767);
		
	}
	public void CtrliRobot(View v)
	{	
		setContentView(R.layout.irobotconfigure);
		pairedDevices = btAdapter.getBondedDevices();
		setupbluetoothConnect();
		Log.e(TAG, "+++DONE getBT for robot");
		setupSeekBar();
	}
	private void setupSeekBar() {
		fwdTextV = (TextView)findViewById(R.id.FwdProgr);
		rotTextV = (TextView)findViewById(R.id.RotProgr);
		fwdBar = (SeekBar)findViewById(R.id.fwdBar);
		rotBar = (SeekBar)findViewById(R.id.rotBar);
		
		fwdBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				fwdTextV.setText(" "+progress);
				fwdSpeed = (char)progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		
		});
		rotBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				rotTextV.setText(" "+progress);
				rotSpeed = (char) progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		
		});
	}
	public void CtrlConnectPC(View v)
	{	
		setContentView(R.layout.connectpc);
		
	}
	
}
