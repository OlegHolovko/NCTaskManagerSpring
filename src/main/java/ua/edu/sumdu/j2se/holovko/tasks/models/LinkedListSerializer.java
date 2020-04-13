package ua.edu.sumdu.j2se.holovko.tasks.models;

import com.google.gson.*;

import java.lang.reflect.Type;

public class LinkedListSerializer implements JsonSerializer<LinkedTaskList> {
    @Override
    public JsonElement serialize(LinkedTaskList tasks, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jObj = (JsonObject)new GsonBuilder().create().toJsonTree(tasks);
        jObj.remove("it");
        return jObj;
    }
}
