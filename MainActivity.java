package com.moo.moostockm;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.annotation.Documented;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*
        Static variables are required to
        distinguish between the type of action to call
        Since adding a cow and editing a cow uses the
        same screen, this is a standard way to execute
        the correct action corresponding the user request.
    * */
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private CowViewModel cowViewModel;

    /*Variable to hold default image location.
    If the user does not add a picture, the default image will be used*/
    private String defaultImage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/img_cow_basic.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.main_app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //The + button that appears at the bottom
        FloatingActionButton fab = findViewById(R.id.button_add_cow);
        //Listener for the + button
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*An intent is an abstract description of an operation to be performed.
                         It can be used with startActivity to launch an Activity.
                         The 1st param is the name of the class that's calling the activity
                         2nd param is the activity class we want to launch.*/
                        Intent intent = new Intent(MainActivity.this, AddEditCowActivity.class);

                        // Built in method to start the activity.
                        // This activity means that the user wants to add a new Cow
                        // That is why the ADD_NOTE_REQUEST is used.

                        startActivityForResult(intent, ADD_NOTE_REQUEST);
                    }
                }
        );

        /*This if block checks if the application has access to use the camera.
         * If not, it will request permission from user.*/
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        /*Checking if we have storage permission.
          Otherwise, there is no need to execute any code as the end result will be
          an exception*/
        if (isStoragePermissionGranted()) {

            //Recycler view is an updated version of ListView.
            //ListView is no longer used for current APIs.
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);

            //An adapter is responsible for attaching data to UI components.
            final CowAdapter adapter = new CowAdapter();
            recyclerView.setAdapter(adapter);

            //Getting the cow viewmodel, adding existing cows to the viewmodel,
            //and observing for changes.
            cowViewModel = ViewModelProviders.of(this).get(CowViewModel.class);
            cowViewModel.getAllCows().observe(this, new Observer<List<Cow>>() {
                @Override
                public void onChanged(@Nullable List<Cow> cows) {
                    adapter.submitList(cows);
                }
            });

            //Recycler view's list item touch helper method. This is built in.
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                //This is where we handle what happens when an item is swiped left/right
                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    cowViewModel.delete(adapter.getCowAt(viewHolder.getAdapterPosition()));
                    Toast.makeText(MainActivity.this, "Cow deleted!", Toast.LENGTH_SHORT).show();
                }
            }).attachToRecyclerView(recyclerView);

            //This is where we handle what happens when user clicks an item.
            adapter.setOnItemClickListener(new CowAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Cow cow) {
                    //User clicked a cow, we need to launch the AddEditCowActivity.
                    Intent intent = new Intent(MainActivity.this, AddEditCowActivity.class);

                    //However, the activity does not know what Cow we are referring to.
                    //We manually add the cow data into the intent object, this way we can
                    //Access this information from AddEditCowActivity
                    intent.putExtra(AddEditCowActivity.EXTRA_ID, cow.getId());
                    intent.putExtra(AddEditCowActivity.EXTRA_BIRTHWEIGHT, cow.getBirthweight());
                    intent.putExtra(AddEditCowActivity.EXTRA_COLOR, cow.getColor());
                    intent.putExtra(AddEditCowActivity.EXTRA_DOB, cow.getDob());
                    intent.putExtra(AddEditCowActivity.EXTRA_GENDER, cow.getGender());
                    intent.putExtra(AddEditCowActivity.EXTRA_IMGURL, cow.getImgurl());
                    intent.putExtra(AddEditCowActivity.EXTRA_NAME, cow.getName());
                    startActivityForResult(intent, EDIT_NOTE_REQUEST);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Checking if it's a new cow to be added, and the activity result is OK
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditCowActivity.EXTRA_NAME);
            String color = data.getStringExtra(AddEditCowActivity.EXTRA_COLOR);
            String dob = data.getStringExtra(AddEditCowActivity.EXTRA_DOB);
            String birthweight = data.getStringExtra(AddEditCowActivity.EXTRA_BIRTHWEIGHT);
            String g = data.getStringExtra(AddEditCowActivity.EXTRA_GENDER);
            String imgurl = data.getStringExtra(AddEditCowActivity.EXTRA_IMGURL);
            int gender = Integer.parseInt(g);
            if (imgurl == null) {
                imgurl = defaultImage;
            }
            Cow cow = new Cow(name, color, dob, gender, birthweight, imgurl);
            cowViewModel.insert(cow);

            Toast.makeText(this, "Cow saved!", Toast.LENGTH_SHORT).show();

        }
        //Otherwise checking if it's an edit activity on an existing cow.
        else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            //Checking if the cow to be updated has ID, without ID we cannot update a cow.
            int id = data.getIntExtra(AddEditCowActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Cow can't be updated", Toast.LENGTH_SHORT).show();
                //ID was -1, that means something went wrong. We finish the activity with a cancel request.
                finishActivity(RESULT_CANCELED);
            }
            //We retrieve the data back from AddEditCowActivity.
            String name = data.getStringExtra(AddEditCowActivity.EXTRA_NAME);
            String color = data.getStringExtra(AddEditCowActivity.EXTRA_COLOR);
            String dob = data.getStringExtra(AddEditCowActivity.EXTRA_DOB);
            String birthweight = data.getStringExtra(AddEditCowActivity.EXTRA_BIRTHWEIGHT);
            String g = data.getStringExtra(AddEditCowActivity.EXTRA_GENDER);
            String imgurl = data.getStringExtra(AddEditCowActivity.EXTRA_IMGURL);
            int gender = Integer.parseInt(g);
            //If imgurl is empty, that means a picture for the cow does not exist
            // we replace it with default image.
            if (imgurl.isEmpty()) {
                imgurl = defaultImage;
            }

            //Creating a new Cow object and passing retrieved data.
            Cow cow = new Cow(name, color, dob, gender, birthweight, imgurl);

            //Setting ID, as this is an existing cow.
            cow.setId(id);
            //Updating the viewmodel.
            cowViewModel.update(cow);
            Toast.makeText(this, "Cow updated!", Toast.LENGTH_SHORT).show();
        } else {

        }

    }


    //Method to provide menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //Delete All cows menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                cowViewModel.deleteAllCows();
                Toast.makeText(this, "All cows deleted", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*  Method to check is the application has permission
        to write to the device's external storage. Otherwise,
        we won't allow application to be used.
    * */

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(null, "isStoragePermissionGranted() called");
                return true;
            } else {

                Log.v(null, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(null, "Permission is granted");
            return true;
        }
    }
}
