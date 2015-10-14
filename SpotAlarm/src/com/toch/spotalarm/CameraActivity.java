package com.toch.spotalarm;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraActivity extends Activity {

	final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	Uri imageUri = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		
		String fileName = "Camera_Example.jpg";
        
        // Create parameters for Intent with filename
         
        ContentValues values = new ContentValues();
         
        values.put(MediaStore.Images.Media.TITLE, fileName);
         
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
         
        // imageUri is the current activity attribute, define and save it for later usage  
         
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
         
        /**** EXTERNAL_CONTENT_URI : style URI for the "primary" external storage volume. ****/

         
        // Standard Intent action that can be sent to have the camera
        // application capture an image and return it.  
         
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
         
         intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
          
         intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
          
        startActivityForResult( intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}
	
	
	@Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data)
       {
           if ( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                
               if ( resultCode == RESULT_OK) {
                
                  /*********** Load Captured Image And Data Start ****************/
                    
                   String imageId = convertImageUriToFile( imageUri,CameraActivity.this);
                    

                  //  Create and excecute AsyncTask to load capture image

                   new LoadImagesFromSDCard().execute(""+imageId);
                    
                 /*********** Load Captured Image And Data End ****************/
                    
               
               } else if ( resultCode == RESULT_CANCELED) {
                    
                   Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
               } else {
                    
                   Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
               }
           }
       }
	
	
	public static String convertImageUriToFile ( Uri imageUri, Activity activity )  {
	      
        Cursor cursor = null;
        int imageID = 0;
         
        try {
         
            /*********** Which columns values want to get *******/
            String [] proj={
                             MediaStore.Images.Media.DATA,
                             MediaStore.Images.Media._ID,
                             MediaStore.Images.Thumbnails._ID,
                             MediaStore.Images.ImageColumns.ORIENTATION
                           };
             
            cursor = activity.managedQuery(
                     
                            imageUri,         //  Get data for specific image URI
                            proj,             //  Which columns to return
                            null,             //  WHERE clause; which rows to return (all rows)
                            null,             //  WHERE clause selection arguments (none)
                            null              //  Order-by clause (ascending by name)
                             
                         );
                               
            //  Get Query Data
             
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
             
            //int orientation_ColumnIndex = cursor.
            //    getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
             
            int size = cursor.getCount();
             
            /*******  If size is 0, there are no images on the SD Card. *****/
             
            if (size == 0) {


//                imageDetails.setText("No Image");
            }
            else
            {
            
                int thumbID = 0;
                if (cursor.moveToFirst()) {
                     
                    /**************** Captured image details ************/
                     
                    /*****  Used to show image on view in LoadImagesFromSDCard class ******/
                    imageID     = cursor.getInt(columnIndex);
                     
                    thumbID     = cursor.getInt(columnIndexThumb);
                     
                    String Path = cursor.getString(file_ColumnIndex);
                     
                    //String orientation =  cursor.getString(orientation_ColumnIndex);
                     
                    String CapturedImageDetails = " CapturedImageDetails : \n\n"
                                                      +" ImageID :"+imageID+"\n"
                                                      +" ThumbID :"+thumbID+"\n"
                                                      +" Path :"+Path+"\n";
                     
                    // Show Captured Image detail on activity
//                    imageDetails.setText( CapturedImageDetails );
                     
                }
            }    
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
         
        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )
         
        return ""+imageID;
    }
	
	
	public class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {
        
        private ProgressDialog Dialog = new ProgressDialog(CameraActivity.this);
         
        Bitmap mBitmap;
         
        protected void onPreExecute() {
            /****** NOTE: You can call UI Element here. *****/
             
            // Progress Dialog
            Dialog.setMessage(" Loading image from Sdcard..");
            Dialog.show();
        }


        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
             
            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            Uri uri = null;       
                 
                 
                try {
                     
                    /**  Uri.withAppendedPath Method Description
                    * Parameters
                    *    baseUri  Uri to append path segment to
                    *    pathSegment  encoded path segment to append
                    * Returns
                    *    a new Uri based on baseUri with the given segment appended to the path
                    */
                     
                    uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + urls[0]);
                     
                    /**************  Decode an input stream into a bitmap. *********/
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                     
                    if (bitmap != null) {
                         
                        /********* Creates a new bitmap, scaled from an existing bitmap. ***********/

                        newBitmap = Bitmap.createScaledBitmap(bitmap, 170, 170, true);
                         
                        bitmap.recycle();
                         
                        if (newBitmap != null) {
                             
                            mBitmap = newBitmap;
                        }
                    }
                } catch (IOException e) {
                    // Error fetching image, try to recover
                     
                    /********* Cancel execution of this task. **********/
                    cancel(true);
                }
             
            return null;
        }
         
         
        protected void onPostExecute(Void unused) {
             
            // NOTE: You can call UI Element here.
             
            // Close progress dialog
              Dialog.dismiss();
             
            if(mBitmap != null)
            {
              // Set Image to ImageView  
            	
            	ImageView imageView = (ImageView)findViewById(R.id.imageViewPhoto);
               
               imageView.setImageBitmap(mBitmap);
            }  
             
        }
         
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
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
}
