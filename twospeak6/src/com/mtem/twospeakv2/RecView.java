package com.mtem.twospeakv2;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;
//import android.widget.Toast;

import java.io.IOException;

import android.util.Log;
import android.view.KeyEvent;
import android.os.CountDownTimer;
import android.content.Context;



public class RecView extends Dialog{
	
	
	//Variables
	static final String TAG = "com.mtem.twospeakv2";
	static final String path1 = "/media/data/recfile1.3gp";
	static final String path2 = "/media/data/recfile2.3gp";
	static final String path3 = "/media/data/recfile3.3gp";
	static final String path4 = "/media/data/recfile4.3gp";
	
	AudioRecorder arec = null; 
	TextView text;	
	TextView instr;
	private final twospeak ts;
	private int select;
	public RecView(Context context, twospeak t_s, int slct)
	{
		super(context);
		this.ts = t_s;
		this.select = slct;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//Set Layout
		setContentView(R.layout.recview);
		
		//Erase Title
		setTitle("");
		
		//Get TextView
		text = (TextView) findViewById(R.id.cdtimer);
		instr = (TextView) findViewById(R.id.instruction);
		instr.setText("Wait");
		text.setText("Wait");
		startRecording(this.select);
		
		
		
	}
	
	public void startRecording(int slct)
	{
		String temppath;
		//Prep recording
		switch(slct)
		{
			case 1: 
				temppath = path2 ;
				break;
			case 2:
				temppath = path3 ;
				break;
			case 3:
				temppath = path4 ;
				break;
			default:
				temppath = path1 ;
				
		}
		
		Log.i("Info", temppath);
		if(arec != null)
		{
			try{
				arec.stop();
				arec.release();
				arec = null;
			}catch(Exception e)
			{
				Log.e(TAG, e.toString());
				//Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
			}
			
		}
		
		try{
			arec = new AudioRecorder(temppath);
		}catch(Exception e)
		{
			Log.e(TAG, e.toString());
			//Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
		//Start Recording
		try
		{		
				arec.run();	
				
		}catch(IOException e)
		{
			Log.e(TAG, e.toString());
			//Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
		
		//Create countdowntimer
		new CountDownTimer(11000,1000)
		{
			public void onTick(long millisUntilFinished){
				if(millisUntilFinished < 10000)
				{
					text.setText(""+millisUntilFinished/1000);
					instr.setText("Say Something");
				}
			}
			public void onFinish(){
				closeRecView();
			}
		}.start();
		
	}
	
	void stopRecording()
	{
		if(arec.getStat())
		{
			//Stop if recording
			try
			{
				arec.stop();
				
			} catch(IOException e)
			{
				Log.d(TAG, e.toString());
				//Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
			}
		}	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			closeRecView();
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		//indicate handled
		return true;
	}
	
	void closeRecView()
	{
		stopRecording();
		
		//Release recording
		try{
			arec.release();
		}catch(IOException expection){}
		this.dismiss();
	}
	
	@Override
	protected void onStop()
	{
		stopRecording();
		ts.stopRecord();
		super.onStop();
	}
}