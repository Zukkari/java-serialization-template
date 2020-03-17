package io.github.zukkari.examples.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class KasutajaSerializer implements JsonSerializer<Kasutaja> {
    @Override
    public JsonElement serialize(Kasutaja kasutaja, Type type, JsonSerializationContext jsonSerializationContext) {
        // Create new JSON object which will act
        // as a root to our tree (this represents Kasutaja object itself)
        JsonObject jsonObject = new JsonObject();

        // Add username property
        jsonObject.addProperty("username", kasutaja.getKasutajanimi());

        // Add user status
        jsonObject.addProperty("userstatus", kasutaja.getKasutajaStaatus());

        // Add active user poperty
        jsonObject.addProperty("isActiveUser", kasutaja.isAktiivneKasutaja());

        return jsonObject;
    }
}
