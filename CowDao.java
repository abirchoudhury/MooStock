package com.moo.moostockm;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CowDao {


    @Insert
    void insert(Cow cow);

    @Update
    void update(Cow cow);

    @Delete
    void delete(Cow cow);

    @Query("DELETE FROM cow_table")
    void deleteAllCows();

    @Query("SELECT * FROM cow_table")
    LiveData<List<Cow>> getAllCows();



}
