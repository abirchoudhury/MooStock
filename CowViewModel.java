package com.moo.moostockm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class CowViewModel extends AndroidViewModel {

    private CowRepository repository;
    private LiveData<List<Cow>> allCows;

    public CowViewModel(@NonNull Application application) {
        super(application);
        repository = new CowRepository(application);
        allCows = repository.getAllCows();
    }

    public void insert(Cow cow){
        repository.insert(cow);
    }

    public void update(Cow cow){
        repository.update(cow);
    }

    public void delete(Cow cow){
        repository.delete(cow);
    }

    public void deleteAllCows(){
        repository.deleteAllCows();
    }

    public LiveData<List<Cow>> getAllCows(){
        return allCows;
    }


}
