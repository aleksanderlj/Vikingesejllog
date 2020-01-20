package com.example.vikingesejllog;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.vikingesejllog.dao.EtapeDAO;
import com.example.vikingesejllog.dao.NoteDAO;
import com.example.vikingesejllog.dao.TogtDAO;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.other.Converters;

@Database(entities = {Note.class, Etape.class, Togt.class}, version = 6, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDAO noteDAO();
    public abstract EtapeDAO etapeDAO();
    public abstract TogtDAO togtDAO();
}

// Room guide used: https://developer.android.com/training/data-storage/room