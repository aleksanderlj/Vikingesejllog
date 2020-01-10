package com.example.vikingesejllog.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.model.Note;

import java.util.List;

@Dao
public interface EtapeDAO {

    @Transaction
    @Query("SELECT * FROM Etape")
    List<EtapeWithNotes> getAll();

    @Transaction
    @Query("SELECT * FROM Etape WHERE etape_id IN (:etape_ids)")
    List<EtapeWithNotes> getAllByIds(List<Long> etape_ids);

    @Transaction
    @Query("SELECT * FROM Etape WHERE togt_parent_id = :togt_id")
    List<EtapeWithNotes> getAllByTogtId(long togt_id);

    @Transaction
    @Query("SELECT * FROM Etape WHERE etape_id = :etape_id")
    EtapeWithNotes getById(long etape_id);

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
