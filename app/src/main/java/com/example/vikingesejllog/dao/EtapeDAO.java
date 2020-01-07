package com.example.vikingesejllog.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Note;

import java.util.List;

@Dao
public interface EtapeDAO {

    @Query("SELECT * FROM Etape")
    List<Etape> getAll();

    @Query("SELECT * FROM Etape WHERE etape_id IN (:etape_ids)")
    List<Etape> getAllByIds(List<Long> etape_ids);

    @Query("SELECT * FROM Etape WHERE etape_id = :etape_id")
    Etape getById(long etape_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Etape etape);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Etape> etapes);

    @Update
    void update(Etape etape);

    @Update
    void updateAll(List<Etape> etapes);

    @Delete
    void delete(Etape etapes);

    @Delete
    void deleteAll(List<Etape> etapes);
}
