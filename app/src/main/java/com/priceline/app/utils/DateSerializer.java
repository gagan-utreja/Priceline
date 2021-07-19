package com.priceline.app.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gagan
 */
public class DateSerializer implements JsonSerializer<Date>
{
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateFormat.format(src));
    }
}