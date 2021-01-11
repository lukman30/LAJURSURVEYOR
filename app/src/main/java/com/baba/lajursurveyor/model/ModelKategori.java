package com.baba.lajursurveyor.model;

import java.util.ArrayList;

public class ModelKategori {
    String id, judul;
    ArrayList<ModelIsiKategori> listisikategori;
    boolean terbuka;

    public ArrayList<ModelIsiKategori> getListisikategori() {
        return listisikategori;
    }

    public void setListisikategori(ArrayList<ModelIsiKategori> listisikategori) {
        this.listisikategori = listisikategori;
    }

    public boolean getTerbuka() {
        return terbuka;
    }

    public void setTerbuka(boolean terbuka) {
        this.terbuka = terbuka;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
