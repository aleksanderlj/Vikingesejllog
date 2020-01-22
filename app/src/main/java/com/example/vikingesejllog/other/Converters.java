package com.example.vikingesejllog.other;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {

    @TypeConverter
    public static Date toDate(Long value){
        return new Date(value);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date.getTime();
    }

    @TypeConverter
    public static List<String> stringListToString(String string){
        Gson gson = new Gson();
        return gson.fromJson(string, new TypeToken<List<String>>(){}.getType());
    }

    @TypeConverter
    public static String stringToStringList(List<String> list){
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Long> longArrayToLongList(long[] array){
        List<Long> list = new ArrayList<>();
        for(long i : array){
            list.add(i);
        }
        return list;
    }

    @TypeConverter
    public static long[] longListToLongArray(List<Long> list){
        long[] array = new long[list.size()];
        for(int n = 0; n<list.size() ; n++){
            array[n] = list.get(n);
        }
        return array;
    }

    @TypeConverter
    public static List<Long> stringToLongList(String string){
        Gson gson = new Gson();
        return gson.fromJson(string, new TypeToken<List<Long>>(){}.getType());
    }

    @TypeConverter
    public static String longListToString(List<Long> list){
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
