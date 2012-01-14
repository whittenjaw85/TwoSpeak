package com.mtem.twospeakv2;

import java.io.File;
import java.io.IOException;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioRecorder
{
	final String TAG = "com.mtem.twospeakv2";
	final MediaRecorder recorder;
	final String path;
	boolean stat; //statemachine
	
	//Create AudioRecorder and clean path
	public AudioRecorder(String path)
	{
		this.path = sanitizePath(path);
		recorder = new MediaRecorder();
	}
	
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
	
	public String getPath()
	{
		return path;
	}
	
	public void run() throws IOException
	{
		try{
			String state = android.os.Environment.getExternalStorageState();
			if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
		        throw new IOException("SD Card is not mounted. It is " + state + ".");
		    }
		}catch(Exception e)
		{
			Log.e(TAG, e.toString());
			return;
		}
		
		//Ensure that the recording directory exists	
		File directory = new File(path).getParentFile();
		try{
			
			if(!directory.exists() && !directory.mkdirs())
			{
				throw new IOException(path);
			}
		}catch(Exception e){
			Log.e(TAG, e.toString());
			return;
		}
		
		//Setup recorder configuration
		
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(path);
		recorder.prepare();
		stat = true;
		recorder.start();
		
	}
	
	//Also need a stop case
	public void stop() throws IOException {
	    recorder.stop();
	    stat = false;
	   
	  }
	
	public void release() throws IOException{
		 recorder.release();
	}
	
	public boolean getStat()
	{
		//true is recording, false is done
		return stat;
	}
}