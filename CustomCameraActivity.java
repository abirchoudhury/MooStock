package com.moo.moostockm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CaptureResult;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DebugUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


/*Activity that handles all camera related operations.*/
public class CustomCameraActivity extends AppCompatActivity {

    View addEditView;
    private String defaultImage = Environment.getExternalStorageDirectory().getAbsolutePath() +"/img_cow_basic.png";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = CustomCameraActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.activity_add_cow);
        //Camera was launched, but user wants to go back. We set the
        //Back button to take user back to main activity screen.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE, i);
    }

    // Method to launch the camera.
    private void dispatchTakePictureIntent(int actionCode, Intent extras) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(extras.hasExtra(AddEditCowActivity.EXTRA_NEW_IMAGENAME)){
            takePictureIntent.putExtras(extras);
        }
        startActivityForResult(takePictureIntent, actionCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Checking if the picture was taken properly
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Intent i = getIntent();
            //Getting the image data
            if(i.hasExtra(AddEditCowActivity.EXTRA_NEW_IMAGENAME)){
                data.putExtras(i);
            }
            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");
            String filename;
            if(data.hasExtra(AddEditCowActivity.EXTRA_NEW_IMAGENAME)){
                filename = data.getStringExtra(AddEditCowActivity.EXTRA_NEW_IMAGENAME);
            }
            else {
                filename = defaultImage;
            }

            //Saving the image.
            SaveImage(this, bmp, filename);
            finish();
        }

    }

    //Method to save an image to the external storage directory of the device.
    public void SaveImage(Context context, Bitmap imgToSave, String cowImgFullPath) {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(defaultImage);
        if (!cowImgFullPath.isEmpty()) {
            file = new File(cowImgFullPath);
        }
        try {
            if(!file.getPath().equals(defaultImage)){
                FileOutputStream fOut = new FileOutputStream(file);
                imgToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();
            }
            else {
                Log.d(TAG, "Default image selected");
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "SaveImage() returned: " + e);
        } catch (IOException e) {
            Log.d(TAG, "SaveImage() called with: context = [" + context + "], imgToSave = [" + imgToSave + "]");
        }
    }




}
