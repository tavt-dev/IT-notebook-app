package com.tien.it_notebook_app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tien.it_notebook_app.data.model.Formula;

import java.util.List;

@Dao
public interface FormulaDao {

       @Query("SELECT * FROM formulas WHERE topicId = :topicId ORDER BY title ASC")
       LiveData<List<Formula>> getAllByTopic(int topicId);

       @Query("SELECT * FROM formulas WHERE topicId = :topicId ORDER BY lastViewedAt DESC")
       LiveData<List<Formula>> getAllByTopicRecent(int topicId);

       @Query("SELECT * FROM formulas WHERE isFavorite = 1 ORDER BY title ASC")
       LiveData<List<Formula>> getFavorites();

       @Query("SELECT * FROM formulas WHERE isFavorite = 1 AND topicId = :topicId ORDER BY title ASC")
       LiveData<List<Formula>> getFavoritesByTopic(int topicId);

       @Query("SELECT * FROM formulas WHERE lastViewedAt > 0 ORDER BY lastViewedAt DESC LIMIT 5")
       LiveData<List<Formula>> getRecentlyViewed();

       @Query("SELECT * FROM formulas WHERE id = :id")
       LiveData<Formula> getById(int id);

       @Query("SELECT * FROM formulas ORDER BY title ASC")
       LiveData<List<Formula>> getAll();

       @Query("SELECT * FROM formulas WHERE title LIKE '%' || :keyword || '%' " +
                     "OR content LIKE '%' || :keyword || '%' " +
                     "OR explanation LIKE '%' || :keyword || '%' " +
                     "ORDER BY title ASC")
       LiveData<List<Formula>> searchByKeyword(String keyword);

       @Query("SELECT * FROM formulas WHERE (title LIKE '%' || :keyword || '%' " +
                     "OR content LIKE '%' || :keyword || '%' " +
                     "OR explanation LIKE '%' || :keyword || '%') " +
                     "AND tags LIKE '%' || :tag || '%' " +
                     "ORDER BY title ASC")
       LiveData<List<Formula>> searchByKeywordAndTag(String keyword, String tag);

       @Query("SELECT * FROM formulas WHERE id = :id")
       Formula getByIdSync(int id);

       @Insert
       long insert(Formula formula);

       @Update
       void update(Formula formula);

       @Delete
       void delete(Formula formula);

       @Query("DELETE FROM formulas WHERE id = :id")
       void deleteById(int id);

       @Query("UPDATE formulas SET isFavorite = :isFavorite WHERE id = :id")
       void setFavorite(int id, boolean isFavorite);

       @Query("UPDATE formulas SET lastViewedAt = :timestamp WHERE id = :id")
       void updateLastViewedAt(int id, long timestamp);

       @Query("UPDATE formulas SET lastViewedAt = 0")
       void clearRecentlyViewed();

       @Query("SELECT COUNT(*) FROM formulas WHERE topicId = :topicId")
       int getFormulaCountByTopic(int topicId);
}
