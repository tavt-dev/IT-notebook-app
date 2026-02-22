package com.tien.it_notebook_app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tien.it_notebook_app.data.model.Topic;

import java.util.List;

@Dao
public interface TopicDao {

    @Query("SELECT * FROM topics ORDER BY name ASC")
    LiveData<List<Topic>> getAll();

    @Query("SELECT * FROM topics WHERE id = :id")
    LiveData<Topic> getById(int id);

    @Query("SELECT * FROM topics WHERE id = :id")
    Topic getByIdSync(int id);

    @Insert
    long insert(Topic topic);

    @Update
    void update(Topic topic);

    @Delete
    void delete(Topic topic);

    @Query("DELETE FROM topics WHERE id = :id")
    void deleteById(int id);
}
