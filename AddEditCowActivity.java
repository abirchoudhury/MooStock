package com.moo.moostockm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/*Activity to handle Adding and Editing cows from the database.*/
public class AddEditCowActivity extends AppCompatActivity {

    /*The static final strings are variables
    that are used to pass data between activities.*/
    public static final String EXTRA_NAME =
            "com.moo.moostockm.EXTRA_NAME";
    public static final String EXTRA_COLOR =
            "com.moo.moostockm.EXTRA_COLOR";
    public static final String EXTRA_DOB =
            "com.moo.moostockm.EXTRA_DOB";
    public static final String EXTRA_GENDER =
            "com.moo.moostockm.EXTRA_GENDER";
    public static final String EXTRA_BIRTHWEIGHT =
            "com.moo.moostockm.EXTRA_BIRTHWEIGHT";
    public static final String EXTRA_IMGURL =
            "com.moo.moostockm.EXTRA_IMGURL";
    public static final String EXTRA_ID =
            "com.moo.moostockm.EXTRA_ID";
    public static final String EXTRA_NEW_IMAGENAME =
            "com.moo.moostockm.EXTRA_NEW_IMAGENAME";

    //Field variables
    private EditText editTextName;
    private EditText editTextColor;
    private EditText editTextDob;
    private EditText editTextBirthweight;
    private RadioGroup radioGroup;
    String imgUrl;
    int selectedGenderBtnID;
    private String defaultImage = Environment.getExternalStorageDirectory().getAbsolutePath() +"/img_cow_basic.png";
    private String TAG = AddEditCowActivity.class.toString();


    /*onCreate method is executed when an
      activity is launched.
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cow);
        ImageView cowImg;

        editTextBirthweight = findViewById(R.id.edit_text_birthweight);
        editTextColor = findViewById(R.id.edit_text_color);
        editTextDob = findViewById(R.id.edit_text_dob);
        editTextName = findViewById(R.id.edit_text_name);
        cowImg = findViewById(R.id.cowImg);
        radioGroup = findViewById(R.id.radioGroup);



        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.main_app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        imgUrl = intent.getStringExtra(EXTRA_IMGURL);
        //Checking if selected cow has ID, that means its an edit request.
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextBirthweight.setText(intent.getStringExtra(EXTRA_BIRTHWEIGHT));

            editTextColor.setText(intent.getStringExtra(EXTRA_COLOR));
            editTextDob.setText(intent.getStringExtra(EXTRA_DOB));
            int gender = intent.getIntExtra(EXTRA_GENDER, -1);
            if (gender == 0) {
                radioGroup.check(R.id.radMaleButton);
            } else {
                radioGroup.check(R.id.radFemaleButton);
            }
            int id = intent.getIntExtra(EXTRA_ID, -1);
            if (!imgUrl.isEmpty()) {
                File file = new File(imgUrl);
                Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bmp != null) {
                    try
                    {
                        cowImg.setImageBitmap(bmp);
                    }
                    catch(Exception e)
                    {
                        bmp = BitmapFactory.decodeFile(defaultImage);
                        cowImg.setImageBitmap(bmp);
                        Log.d(TAG, "bitmap variable was null, using default image");
                    }
                    imgUrl = file.getAbsolutePath();
                }
                else
                {
                    imgUrl = defaultImage;
                    Drawable dr = Drawable.createFromPath(imgUrl);
                    cowImg.setImageDrawable(dr);
                }

            }
            else
            {
                imgUrl = defaultImage;
                Drawable dr = Drawable.createFromPath(imgUrl);
                cowImg.setImageDrawable(dr);
            }
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));
        }
        else
        {
            setTitle("Add Cow");

        }


    }

    private void saveCow() {
        String name = editTextName.getText().toString().trim();
        String color = editTextColor.getText().toString().trim();
        String dob = editTextDob.getText().toString().trim();
        String birthweight = editTextBirthweight.getText().toString().trim();
        RadioButton radioButton;
        String imageFileName;

        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please provide name!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_COLOR, color);
        data.putExtra(EXTRA_DOB, dob);
        data.putExtra(EXTRA_BIRTHWEIGHT, birthweight);
        radioGroup = findViewById(R.id.radioGroup);
        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioButton = findViewById(selectedId);
        if (radioButton == findViewById(R.id.radMaleButton)) {
            selectedGenderBtnID = 0;
        } else if (radioButton == findViewById(R.id.radFemaleButton)) {
            selectedGenderBtnID = 1;
        } else {
            selectedGenderBtnID = -1;
        }
        if (selectedGenderBtnID == 0) {
            data.putExtra(EXTRA_GENDER, "0");
        } else {
            data.putExtra(EXTRA_GENDER, "1");
        }
        data.putExtra(EXTRA_IMGURL, imgUrl);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        } else {
            imageFileName = "cow_img_" + name.trim().toLowerCase() + ".jpg";
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_cow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_cow:
                saveCow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void takePictureButtonClicked(View view) {
        Intent i = new Intent(AddEditCowActivity.this, CustomCameraActivity.class);
        String imageName;
        if(editTextName.getText() != null)
        {

          imageName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cow_img_"+editTextName.getText().toString().trim().toLowerCase()+".jpg";
          imgUrl = imageName;
        }
        else {
            imageName = defaultImage;
            imgUrl = defaultImage;
        }
        i.putExtra(EXTRA_NEW_IMAGENAME, imageName);
        startActivityForResult(i, 1);
        saveCow();
        finish();
    }



}
