package com.moo.moostockm;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Cow.class}, version = 1, exportSchema = false)
public abstract class CowDatabase extends RoomDatabase {

    private static CowDatabase instance;

    public abstract CowDao cowDao();

    public static synchronized CowDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CowDatabase.class, "cow_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private CowDao cowDao;
        private PopulateDbAsyncTask(CowDatabase db){
            cowDao = db.cowDao();
        }

        @Override
        protected Void doInBackground(Void... voids){
            Cow cow = new Cow("Cowla", "Black/White", "2018-07-23", 0, "61.3 lbs", "/storage/emulated/0/cow_img_cowla.jpg");
            cowDao.insert(cow);
            cow = new Cow("Cowlisle", "Brown", "2016-02-21", 1, "59.3 lbs", "/storage/emulated/0/cow_img_cowlisle.jpg");
            cowDao.insert(cow);
            cow = new Cow("Sir Loin", "Black/White", "2017-01-15", 0, "63.3 lbs", "/storage/emulated/0/cow_img_sir_loin.jpg");
            cowDao.insert(cow);
            cow = new Cow("Hugh Heifer", "Black/Brown", "2015-12-20", 1, "60.5 lbs", "/storage/emulated/0/cow_img_hugh_heifer.jpg");
            cowDao.insert(cow);
            return null;
        }
    }
}
