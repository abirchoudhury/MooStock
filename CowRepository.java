package com.moo.moostockm;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class CowRepository {
    private CowDao cowDao;
    private LiveData<List<Cow>> allCows;

    public CowRepository(Application application) {
        CowDatabase database = CowDatabase.getInstance(application);
        cowDao = database.cowDao();
        allCows = cowDao.getAllCows();
    }

    public void insert(Cow cow) {
        new InsertCowAsyncTask(cowDao).execute(cow);

    }

    public void update(Cow cow) {
        new UpdateCowAsyncTask(cowDao).execute(cow);
    }

    public void delete(Cow cow) {
        new DeleteCowAsyncTask(cowDao).execute(cow);

    }

    public void deleteAllCows() {
        new DeleteAllCowsAsyncTask(cowDao).execute();
    }

    public LiveData<List<Cow>> getAllCows() {
        return allCows;
    }

    private static class InsertCowAsyncTask extends AsyncTask<Cow, Void, Void> {

        private CowDao cowDao;

        private InsertCowAsyncTask(CowDao cowDao) {
            this.cowDao = cowDao;
        }

        @Override
        protected Void doInBackground(Cow... cows) {
            cowDao.insert(cows[0]);
            return null;
        }
    }

    private static class UpdateCowAsyncTask extends AsyncTask<Cow, Void, Void> {

        private CowDao cowDao;

        private UpdateCowAsyncTask(CowDao cowDao) {
            this.cowDao = cowDao;
        }

        @Override
        protected Void doInBackground(Cow... cows) {
            cowDao.update(cows[0]);
            return null;
        }
    }

    private static class DeleteCowAsyncTask extends AsyncTask<Cow, Void, Void> {
        private CowDao cowDao;

        private DeleteCowAsyncTask(CowDao cowDao) {
            this.cowDao = cowDao;
        }

        @Override
        protected Void doInBackground(Cow... cows) {
            cowDao.delete(cows[0]);
            return null;
        }
    }

    private static class DeleteAllCowsAsyncTask extends AsyncTask<Void, Void, Void> {
        private CowDao cowDao;

        private DeleteAllCowsAsyncTask(CowDao cowDao) {
            this.cowDao = cowDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cowDao.deleteAllCows();
            return null;
        }
    }
}
