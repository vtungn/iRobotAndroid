package com.example.irobandoid;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.util.Log;

public class iRobotController {
	
	public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	// MAC address of FireFly
	public static String address = "00:06:66:0A:AB:27";
	private static final String TAG = "RobotController";
	public OutputStream outStream = null;
	
	public iRobotController(OutputStream outputStream) {
		// TODO Auto-generated constructor stub
		outStream = outputStream;
	}

	public void sendCommandtoiRobot(char msg){
		try {
            outStream.write(msg);
        } catch (IOException e) {
            Log.e(TAG, "ON SEND MSGE: Exception during write.", e);
        }
	}
	public void setOutputStream(OutputStream outputStream){
		outStream = outputStream;
	}
	public void drive(char velocity, char rotatespeed) {
		sendCommandtoiRobot((char)137);
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
}
