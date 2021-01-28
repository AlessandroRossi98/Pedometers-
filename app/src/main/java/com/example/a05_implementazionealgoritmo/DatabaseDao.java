package com.example.a05_implementazionealgoritmo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DatabaseDao {

    @Query("SELECT * FROM EntityTest")
    List<EntityTest> getAllTests();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTest(EntityTest new_test);

    @Query("DELETE FROM EntityTest WHERE test_id=:id")
    void deleteTest(Integer id);

    @Query("SELECT * FROM EntityTest WHERE test_id=:id")
    EntityTest getTestFromId(Integer id);

    @Query("SELECT * FROM EntityTest WHERE file_name=:file_name")
    EntityTest getTestFromPath(String  file_name);
}
