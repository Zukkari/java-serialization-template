package io.github.zukkari.examples.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonAdvancedExample {
    public static void main(String[] args) {
        Kasutaja kasutaja = new Kasutaja();
        kasutaja.setKasutajanimi("Username1");
        kasutaja.setParool("hunter2");
        kasutaja.setKasutajaStaatus(3);
        kasutaja.setAktiivneKasutaja(true);

        Gson gson = new GsonBuilder()
                // Register our KasutajaSerializer class here
                .registerTypeAdapter(Kasutaja.class, new KasutajaSerializer())
                .create();

        String userAsJson = gson.toJson(kasutaja);

        System.out.println(userAsJson);
    }
}
