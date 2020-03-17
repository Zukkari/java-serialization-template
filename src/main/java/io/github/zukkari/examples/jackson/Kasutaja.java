package io.github.zukkari.examples.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Kasutaja {
    @JsonProperty(value = "username")
    private String kasutajanimi;

    @JsonIgnore
    private String parool;

    @JsonProperty(value = "userstatus")
    private int kasutajaStaatus;

    @JsonProperty(value = "isActiveUser")
    private boolean aktiivneKasutaja;

    public String getKasutajanimi() {
        return kasutajanimi;
    }

    public void setKasutajanimi(String kasutajanimi) {
        this.kasutajanimi = kasutajanimi;
    }

    public String getParool() {
        return parool;
    }

    public void setParool(String parool) {
        this.parool = parool;
    }

    public int getKasutajaStaatus() {
        return kasutajaStaatus;
    }

    public void setKasutajaStaatus(int kasutajaStaatus) {
        this.kasutajaStaatus = kasutajaStaatus;
    }

    public boolean isAktiivneKasutaja() {
        return aktiivneKasutaja;
    }

    public void setAktiivneKasutaja(boolean aktiivneKasutaja) {
        this.aktiivneKasutaja = aktiivneKasutaja;
    }
}
