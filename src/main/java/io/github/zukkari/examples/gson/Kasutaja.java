package io.github.zukkari.examples.gson;

public class Kasutaja {
    private String kasutajanimi;
    private String parool;
    private int kasutajaStaatus;
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
