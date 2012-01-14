package com.mtem.twospeakv2;

import java.io.*;
import android.app.Activity;
import android.media.MediaPlayer;


import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.net.Uri;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.IOException;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
//import android.content.Context;


import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class twospeak extends Activity {
 
	//Other activities
	private static final int SELECT_IMAGE = 1001;
	
	//Declare Path Variables
	String path1 = "/media/data/recfile1.3gp";
	String path2 = "/media/data/recfile2.3gp";
	String path3 = "/media/data/recfile3.3gp";
	String path4 = "/media/data/recfile4.3gp";
	
	//Initialize placeholders for the icons
	int record_icon = android.R.drawable.ic_btn_speak_now;
	int play_icon 	= android.R.drawable.ic_media_play;
	int repeat_icon = android.R.drawable.ic_menu_rotate;
	int bad_icon	= android.R.drawable.ic_lock_silent_mode;
	

	//Gesture Detector
	GestureDetector gestureDetector;
	
	//Button Variables Placeholder
	TSButton [] button_inst ;	
	
	//State Variables
	boolean nowrecording = false;
	int select = 0;
	int clicks = 0;
	
	//MediaPlayer Variables for Playing
    boolean paused = false;
    MediaPlayer m_mediaplayer;
    FileInputStream fileInputStream; 
    
	//Function during initialization for the paths
	private String sanitizePath(String path) 
	{
	    if (!path.startsWith("/")) {
	      path = "/" + path;
	    }
	    if (!path.contains(".")) {
	      path += ".3gp";
	    }
	    return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
	 }
	
	//Variables
	int numButtons = 0;
	
	private void configureLayout()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String temp = prefs.getString("numbuttonpref", "0");
		numButtons = Integer.parseInt(temp);
		LinearLayout.LayoutParams layoutParams;
		Display display; 
        int width;
        int height;
        Log.i("info", "Number of buttons is 3.");
		switch(numButtons)
		{
			case 3:	
				setContentView(R.layout.main4);
				button_inst[0].addButton((ImageButton) findViewById(R.id.button_1));
				button_inst[1].addButton((ImageButton) findViewById(R.id.button_2));
				button_inst[2].addButton((ImageButton) findViewById(R.id.button_3));
				button_inst[3].addButton((ImageButton) findViewById(R.id.button_4));
				
				//Configure layout
				display = getWindowManager().getDefaultDisplay(); 
		        width = display.getWidth()-70;
		        height = display.getHeight()-140;
		        layoutParams = new LinearLayout.LayoutParams(width/2,height/2);
		        button_inst[0].resizeButton(layoutParams);
		        button_inst[1].resizeButton(layoutParams);
		        button_inst[2].resizeButton(layoutParams);
		        button_inst[3].resizeButton(layoutParams);
				break;
				
			case 2:
				setContentView(R.layout.main3);
				button_inst[0].addButton((ImageButton) findViewById(R.id.button_1));
				button_inst[1].addButton((ImageButton) findViewById(R.id.button_2));
				button_inst[2].addButton((ImageButton) findViewById(R.id.button_3));
				
				//Configure layout
				display = getWindowManager().getDefaultDisplay(); 
		        width = display.getWidth()-70;//60 is from padding
		        height = display.getHeight()-140;
		        layoutParams = new LinearLayout.LayoutParams(width/2,height/2);
		        button_inst[0].resizeButton(layoutParams);
		        button_inst[1].resizeButton(layoutParams);
		        
		        layoutParams = new LinearLayout.LayoutParams(width,height/2);
		        button_inst[2].resizeButton(layoutParams);
				break;
				
			case 1:
				setContentView(R.layout.main2);
				button_inst[0].addButton((ImageButton) findViewById(R.id.button_1));
				button_inst[1].addButton((ImageButton) findViewById(R.id.button_2));
				
				//Configure layout
				display = getWindowManager().getDefaultDisplay(); 
		        width = display.getWidth()-70;//60 is from padding
		        height = display.getHeight()-140;
		        layoutParams = new LinearLayout.LayoutParams(width,height/2);
		        button_inst[0].resizeButton(layoutParams);
		        button_inst[1].resizeButton(layoutParams);
				break;
				
			default:
				setContentView(R.layout.main);
				button_inst[0].addButton((ImageButton) findViewById(R.id.button_1));
		}
		
		//Configure ClickListeners for the Buttons
		for(int i=0;i<numButtons+1;i++)
		{
			switch(i)
			{
				
				case 1:
				//Configure listeners
				button_inst[1].getButton().setOnClickListener(new View.OnClickListener() {
					//--------------------------------------------
		        	//Create listener for the play button
		        	//The critical component is the !nowrecording statement
		        	//that prevents crashing.
		        	//---------------------------------------------
					public void onClick(View v) {
						select = 1;
						paused = false;
						clicks++;
						for(int i=0;i<numButtons+1;i++)
							button_inst[i].drawPlayImage();
						button_inst[1].drawSelectImage();
						Thread initBkgdThread = new Thread(new Runnable(){
							public void run()
							{
										if(!nowrecording)
										{
											play_recording(1);
										}
							}
						});
						
						initBkgdThread.start(); //this thread actually handles playback
					}
				});
				break;
				
				case 2:
				button_inst[2].getButton().setOnClickListener(new View.OnClickListener() {
					//--------------------------------------------
		        	//Create listener for the play button
		        	//The critical component is the !nowrecording statement
		        	//that prevents crashing.
		        	//---------------------------------------------
					public void onClick(View v) {
						select = 2;
						paused = false;
						clicks++;
						for(int i=0;i<numButtons+1;i++)
							button_inst[i].drawPlayImage();
						button_inst[2].drawSelectImage();
						Thread initBkgdThread = new Thread(new Runnable(){
							public void run()
							{
										select = 2;
										if(!nowrecording)
										{
											play_recording(2);
										}
							}
						});
						
						initBkgdThread.start(); //this thread actually handles playback
					}
				});
				break;
				
				case 3:
				button_inst[3].getButton().setOnClickListener(new View.OnClickListener() {
					//--------------------------------------------
		        	//Create listener for the play button
		        	//The critical component is the !nowrecording statement
		        	//that prevents crashing.
		        	//---------------------------------------------
					public void onClick(View v) {
						select = 3;
						paused = false;
						clicks++;
						for(int i=0;i<numButtons+1;i++)
							button_inst[i].drawPlayImage();
						button_inst[3].drawSelectImage();
						Thread initBkgdThread = new Thread(new Runnable(){
							public void run()
							{
										select = 3;
										if(!nowrecording)
										{
											play_recording(3);
										}
							}
						});
						
						initBkgdThread.start(); //this thread actually handles playback
					}
				});
				break;
				
				default:
				//Configure listeners
				button_inst[0].getButton().setOnClickListener(new View.OnClickListener() {
					//--------------------------------------------
		        	//Create listener for the play button
		        	//The critical component is the !nowrecording statement
		        	//that prevents crashing.
		        	//---------------------------------------------
					public void onClick(View v) {
						select = 0;
						paused = false;
						clicks++;
						for(int i=0;i<numButtons+1;i++)
							button_inst[i].drawPlayImage();
						button_inst[0].drawSelectImage();
						Thread initBkgdThread = new Thread(new Runnable(){
							public void run()
							{
										select = 0;
										if(!nowrecording)
										{
											play_recording(0);
										}
							}
						});
						
						initBkgdThread.start(); //this thread actually handles playback
					}
				});
			}//END SWITCH CASE
		}//END FOR LOOP
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        //Create new imagebutton placeholders (just in case)
        button_inst = new TSButton[4];
        button_inst[0] = new TSButton(this);
        button_inst[1] = new TSButton(this);
        button_inst[2] = new TSButton(this);
        button_inst[3] = new TSButton(this);
        
        //Check preferences for number of buttons
        configureLayout();
        Toast.makeText(this, "Press the [Menu] key for help.", Toast.LENGTH_SHORT).show();
        //Default values
        this.select = 0;
        m_mediaplayer = new MediaPlayer();
        this.paused = false;
        
        //Configure the listeners
        //Create gesture listeners
  		gestureDetector = new GestureDetector(this, 
			new SimpleOnGestureListener(){
				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY){
					float xv, yv;
					xv = e2.getX() - e1.getX();
					yv = e2.getY() - e1.getY();
					//If motion is large
					if(Math.pow(xv, 2) + 
							Math.pow(yv, 2) > 60.0)
					{
						//Determine direction
						int val = 0;
						if(xv > 0)
							val = 10;
						if(yv > 0)
							val++;
						
						switch(val)
						{
							case 1://Bottom Left
								select = 2;
							break;
							case 10://Top Right
								select = 1;
							break;
							case 11://Bottom Right
								select = 3;
							break;
							default://Top Left
								select = 0;
						}		
						
						//Now reconcile this to the number of buttons
						if(numButtons+1 == 2)
						{
							if(select == 0 || select == 1)
								select = 0;
							else if(select == 2 || select == 3)
								select = 1;	
						}	
						else
							if(select > numButtons)
								select = numButtons;
						
						Log.i("Info", "select: " + select);
						
						for(int i=0;i<numButtons+1;i++)
							button_inst[i].drawPlayImage();
						button_inst[select].drawSelectImage();
						return true;
					}
					else 
						return false;
				}	
  		});
        
        //Setup AudioRecorder path and object
        //AudioRecorder is a custom class
        
    	//arec = new AudioRecorder(path);
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return gestureDetector.onTouchEvent(event);
	}
           
    private boolean play_recording(int slct)
    {
    	if(this.clicks < 2)
    	{
	    	String temppath ;
	    	switch(slct)
	    	{
	    		case 3: 
	    			temppath = path4 ;
	    			break;
	    		case 2:
	    			temppath = path3 ;
	    			break;
	    		case 1:
	    			temppath = path2 ;
	    			break;
	    		default:
	    			temppath = path1 ;
	    	}
	    	
	    	//Checks to see if unpaused and avoids a nullpointer
	    	if(m_mediaplayer!=null){
	    		if(m_mediaplayer.isPlaying())
	    		{
	    			m_mediaplayer.stop();
	    		}
	    		m_mediaplayer.release();
	    	}
	    	
			m_mediaplayer = new MediaPlayer();
			try{
				fileInputStream = new FileInputStream(sanitizePath(temppath));
			    m_mediaplayer.setDataSource(fileInputStream.getFD()); 
				//m_mediaplayer.setDataSource(sanitizePath(temppath));
				m_mediaplayer.prepare();
			}catch(IOException e)
			{
				
				Log.i("CaughtIO", e.toString());
			}
			
			m_mediaplayer.start();
			try{
				Thread.sleep(400);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			this.clicks--;
			return true;
    	}
    	else
    	{
    		this.clicks--;
    		return false;
    	}
    	// if failed
    }
	
    //Pauses song when program is paused
	@Override
	protected void onPause()
	{
		paused = true;
		if(m_mediaplayer!=null)
				if(m_mediaplayer.isPlaying())
					m_mediaplayer.stop();
		
		if(nowrecording)
			dg.dismiss();
		
		super.onPause();
	}
	
	//Sets the stoprecord when resumed
	@Override
	protected void onResume()
	{
		super.onResume();
		configureLayout();
		for(int i=0;i<numButtons+1;i++)
		{
			button_inst[i].normalizeImage();
		}
	}
	
	//Onward to recording
	//--If DPAD pressed left, record,
	//--If DPAD pressed right, stop recording
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_DPAD_LEFT:
			recordSound(select);
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		
		//indicate handled
		return true;
	}
	
	Dialog dg;
	public void recordSound(int slct)
	{
		if(m_mediaplayer != null)
		{
			if(m_mediaplayer.isPlaying()){m_mediaplayer.stop();}
		}
		if(!nowrecording)
		{	
			//Start Record Dialog
			//Intent launchRecord = new Intent(this,RecView.class);
			//startActivity(launchRecord);
			
			dg = new RecView(this, this, slct);
			dg.show();
		}
		
	}
	
	public void stopRecord()
	{
		if(nowrecording)
		{
			button_inst[select].drawPlayImage();
			nowrecording = false;
		}
	}
	
	@Override
	protected void onDestroy()
	{	
		super.onDestroy();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch(item.getItemId())
		{
			case R.id.tutorial:
				startActivity(new Intent(this, about.class));
				return true;
			case R.id.aboutus:
				startActivity(new Intent(this, aboutus.class));
				return true;
			case R.id.settings:
				startActivity(new Intent(this, Settings.class));
				return true;
			case R.id.setimage:
				//startActivity(new Intent(this, ImageNav.class));
				startActivityForResult(new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
						SELECT_IMAGE);
				return true;
			case R.id.record:
				Log.i("Info", "select: " + select);
				recordSound(select);
				return true;
			default:
		        return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == SELECT_IMAGE && resultCode == RESULT_OK) 
		{
			//Grab Uri
			Uri imageUri = Uri.parse(data.getDataString());
	          
        	//BitmapFactory.Options options = new BitmapFactory.Options();
        	//options.inTempStorage = new byte[16*1024];
        	button_inst[select].addImage(imageUri);
	            
	            	
	           
	            
	        

		}
	}
	

}