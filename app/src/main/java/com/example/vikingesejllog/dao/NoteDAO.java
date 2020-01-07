package com.example.vikingesejllog.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vikingesejllog.model.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Query("SELECT * FROM Note")
    List<Note> getAll();

    @Query("SELECT * FROM Note WHERE note_id IN (:note_ids)")
    List<Note> getAllByIds(List<Long> note_ids);

    @Query("SELECT * FROM Note WHERE note_id = :note_id")
    Note getById(long note_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Note note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Note> notes);

    @Update
    void update(Note note);

    @Update
    void updateAll(List<Note> notes);

    @Delete
    void delete(Note note);

    @Delete
    void deleteAll(List<Note> notes);
}
