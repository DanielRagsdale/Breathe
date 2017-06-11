package io.studiodan.breathe.models.routines;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by dan on 6/6/17.
 */

public class ITimerJsonAdapter implements JsonSerializer<ITimer>, JsonDeserializer<ITimer>
{
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE  = "INSTANCE";

    @Override
    public JsonElement serialize(ITimer src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject retValue = new JsonObject();
        String className = src.getClass().getName();
        retValue.addProperty(CLASSNAME, className);
        JsonElement elem = context.serialize(src);
        retValue.add(INSTANCE, elem);

        return retValue;
    }

    @Override
    public ITimer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        //Log.d("Breathe", "Deserialization attempted. ************************************");

        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();

        Class<?> klass = null;
        try
        {
            klass = Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }

        return context.deserialize(jsonObject.get(INSTANCE), klass);
    }
}








