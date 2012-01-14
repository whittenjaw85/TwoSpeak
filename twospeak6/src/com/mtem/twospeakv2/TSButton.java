package com.mtem.twospeakv2;

import java.io.FileNotFoundException;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;


public class TSButton {

	private Bitmap imgPlay;
//	private String txt;
	private ImageButton imgButton;
	private Uri imageUri;
	Context ctxt;
	
	//Button Dimensions
	int X;
	int Y;
	
	public TSButton(Context cntxt)
	{
//		txt = null;
		this.ctxt = cntxt;
		imgButton = new ImageButton(cntxt);
//		imgSelect = null;
		imgPlay = null;
	}
	
	public void addButton(ImageButton btn)
	{
		imgButton = btn;
		this.drawPlayImage();
	}
	
	public ImageButton getButton()
	{
		return imgButton;
	}
	
	public void addImage(Uri imgUri)
	{
		this.imageUri = imgUri;
		normalizeImage();
	}
	
	public void normalizeImage()
	{
		if(imageUri != null)
		{
			try{
				
				Bitmap bitmapa;
				bitmapa = BitmapFactory.decodeStream(ctxt.getContentResolver().openInputStream(this.imageUri));
				
				//Get width and height of original image
		        int w = bitmapa.getWidth();
		        int h = bitmapa.getHeight();
		        
		        //Check Button size
		        int ref = 0;
				if(this.Y>=this.X)
					ref = this.X;
				else
					ref = this.Y;
				
				ref -= 15;
				 Log.i("refval", "" + ref);
		        if(h>w){
		        	w = (int)( (double)((double)(ref)/(double)(h))*w);
		        	h = ref;
		        }
		        else{
		        	h = (int)( (double)((double)(ref)/(double)(w))*h);
		        	w = ref;
		        }
		        //Resize to small to reduce resources
		        imgPlay = Bitmap.createScaledBitmap (bitmapa,w,h,false);
		        
		        //Set resources and invalidate ImageButton
		        imgButton.setImageBitmap(this.imgPlay);
		        imgButton.invalidate();
		        
		        //release unused large bitmap
		        bitmapa.recycle();
			}catch (FileNotFoundException e) {  
		        // TODO Auto-generated catch block  
		        e.printStackTrace(); 
		        Log.i("Info", "failed");
			}
		}
        
	}
	
	public void drawPlayImage()
	{
		if(imgPlay == null)
			imgButton.setImageResource(android.R.drawable.ic_media_play);
		else
			imgButton.setImageBitmap(imgPlay);
	}
	
	public void drawSelectImage()
	{
		if(imgPlay == null)
			imgButton.setImageResource(android.R.drawable.ic_btn_speak_now);
		else
			imgButton.setImageBitmap(imgPlay);
	}
	
	public void resizeButton(LinearLayout.LayoutParams layoutParams)
	{
		imgButton.setLayoutParams(layoutParams);
		X = layoutParams.width;
		Y = layoutParams.height;
		
		Log.i("NewChange", "X is" + X);
		normalizeImage();
	}
}
