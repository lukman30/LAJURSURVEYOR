package com.baba.lajursurveyor.model;

public class ModelUser {
    String email, foto, nama, status, id;

    public ModelUser(String email, String foto, String nama, String status) {
        this.email = email;
        this.foto = foto;
        this.nama = nama;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getFoto() {
        return foto;
    }

    public String getNama() {
        return nama;
    }

    public String getStatus() {
        return status;
    }
}
