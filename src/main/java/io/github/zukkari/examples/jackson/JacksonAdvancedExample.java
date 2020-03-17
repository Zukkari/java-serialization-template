package io.github.zukkari.examples.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonAdvancedExample {
    public static void main(String[] args) throws Exception {
        Kasutaja kasutaja = new Kasutaja();
        kasutaja.setKasutajanimi("Username1");
        kasutaja.setParool("hunter2");
        kasutaja.setKasutajaStaatus(3);
        kasutaja.setAktiivneKasutaja(true);

        ObjectMapper objectMapper = new ObjectMapper();

        String userAsJson = objectMapper.writeValueAsString(kasutaja);

        System.out.println(userAsJson);
    }
}
