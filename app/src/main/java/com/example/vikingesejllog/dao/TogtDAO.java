package com.example.vikingesejllog.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.vikingesejllog.model.Togt;

import java.util.List;

@Dao
public interface TogtDAO {

    @Transaction
    @Query ("SELECT * FROM Togt ORDER BY togt_id DESC LIMIT 1")
    Togt getLatestTogt();

    @Transaction
    @Query("SELECT * FROM Togt")
    List<Togt> getAll();

    @Transaction
    @Query("SELECT * FROM Togt WHERE togt_id IN (:togt_ids)")
    List<Togt> getAllByIds(List<Long> togt_ids);

    @Transaction
    @Query("SELECT * FROM Togt WHERE togt_id = :togt_id")
    Togt getById(long togt_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Togt togt);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Togt> togts);

    @Update
    void update(Togt togt);

    @Update
    void updateAll(List<Togt> togts);

    @Delete
    void delete(Togt togts);

    @Delete
    void deleteAll(List<Togt> togts);
}
