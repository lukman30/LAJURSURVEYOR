package com.baba.lajursurveyor.model;

public class ModelProduk {
    String nama;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    String foto;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
    String merk;

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    String bb;
    String deskripsi;
    String durasicicilan;
    String idnya;
    String idpengguna;
    String jampublikasi;
    String jaraktempuh;
    String kabupaten;
    String kapasitasmesin;
    String kota;
    String merek;
    String model;
    String penjelasansingkat;
    String provinsi;
    int tahunmobil;
    String tglpublikasi;
    String tipebody;
    String tipepenjual;
    String transmisi;
    String varian;
    String warna;

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    String tgl;
    int harga, hargacicilan;

    public ModelProduk() {
    }

    public ModelProduk(String bb, String deskripsi, String durasicicilan, String idnya, String idpengguna, String jampublikasi, String jaraktempuh, String kabupaten, String kapasitasmesin, String kota, String merek, String model, String penjelasansingkat, String provinsi, int tahunmobil, String tglpublikasi, String tipebody, String tipepenjual, String transmisi, String varian, String warna, int harga, int hargacicilan) {
        this.bb = bb;
        this.deskripsi = deskripsi;
        this.durasicicilan = durasicicilan;
        this.idnya = idnya;
        this.idpengguna = idpengguna;
        this.jampublikasi = jampublikasi;
        this.jaraktempuh = jaraktempuh;
        this.kabupaten = kabupaten;
        this.kapasitasmesin = kapasitasmesin;
        this.kota = kota;
        this.merek = merek;
        this.model = model;
        this.penjelasansingkat = penjelasansingkat;
        this.provinsi = provinsi;
        this.tahunmobil = tahunmobil;
        this.tglpublikasi = tglpublikasi;
        this.tipebody = tipebody;
        this.tipepenjual = tipepenjual;
        this.transmisi = transmisi;
        this.varian = varian;
        this.warna = warna;
        this.harga = harga;
        this.hargacicilan = hargacicilan;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDurasicicilan() {
        return durasicicilan;
    }

    public void setDurasicicilan(String durasicicilan) {
        this.durasicicilan = durasicicilan;
    }

    public String getIdnya() {
        return idnya;
    }

    public void setIdnya(String idnya) {
        this.idnya = idnya;
    }

    public String getIdpengguna() {
        return idpengguna;
    }

    public void setIdpengguna(String idpengguna) {
        this.idpengguna = idpengguna;
    }

    public String getJampublikasi() {
        return jampublikasi;
    }

    public void setJampublikasi(String jampublikasi) {
        this.jampublikasi = jampublikasi;
    }

    public String getJaraktempuh() {
        return jaraktempuh;
    }

    public void setJaraktempuh(String jaraktempuh) {
        this.jaraktempuh = jaraktempuh;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getKapasitasmesin() {
        return kapasitasmesin;
    }

    public void setKapasitasmesin(String kapasitasmesin) {
        this.kapasitasmesin = kapasitasmesin;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getMerek() {
        return merek;
    }

    public void setMerek(String merek) {
        this.merek = merek;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPenjelasansingkat() {
        return penjelasansingkat;
    }

    public void setPenjelasansingkat(String penjelasansingkat) {
        this.penjelasansingkat = penjelasansingkat;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public int getTahunmobil() {
        return tahunmobil;
    }

    public void setTahunmobil(int tahunmobil) {
        this.tahunmobil = tahunmobil;
    }

    public String getTglpublikasi() {
        return tglpublikasi;
    }

    public void setTglpublikasi(String tglpublikasi) {
        this.tglpublikasi = tglpublikasi;
    }

    public String getTipebody() {
        return tipebody;
    }

    public void setTipebody(String tipebody) {
        this.tipebody = tipebody;
    }

    public String getTipepenjual() {
        return tipepenjual;
    }

    public void setTipepenjual(String tipepenjual) {
        this.tipepenjual = tipepenjual;
    }

    public String getTransmisi() {
        return transmisi;
    }

    public void setTransmisi(String transmisi) {
        this.transmisi = transmisi;
    }

    public String getVarian() {
        return varian;
    }

    public void setVarian(String varian) {
        this.varian = varian;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getHargacicilan() {
        return hargacicilan;
    }

    public void setHargacicilan(int hargacicilan) {
        this.hargacicilan = hargacicilan;
    }
}
