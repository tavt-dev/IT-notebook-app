package com.tien.it_notebook_app.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class Converters {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromStringArray(String[] array) {
        if (array == null) return null;
        return gson.toJson(array);
    }

    @TypeConverter
    public static String[] toStringArray(String json) {
        if (json == null) return null;
        Type type = new TypeToken<String[]>() {}.getType();
        return gson.fromJson(json, type);
    }
}
