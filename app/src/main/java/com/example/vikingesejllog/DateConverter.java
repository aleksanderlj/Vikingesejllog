package com.example.vikingesejllog;

import java.util.Date;

public class DateConverter {

    public static Date toDate(Long value){
        return new Date(value);
    }

    public static Long fromDate(Date date){
        return date.getTime();
    }
}
