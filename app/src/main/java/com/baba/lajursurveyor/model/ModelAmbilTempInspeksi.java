package com.baba.lajursurveyor.model;

public class ModelAmbilTempInspeksi {
    String foto, keterangan;

    public ModelAmbilTempInspeksi() {
    }

    public ModelAmbilTempInspeksi(String foto, String keterangan) {
        this.foto = foto;
        this.keterangan = keterangan;
    }

    public String getFoto() {
        return foto;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
