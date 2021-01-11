package com.baba.lajursurveyor.kategori;

import com.baba.lajursurveyor.helper.Fungsi;

import java.util.ArrayList;

public class Kategori {
    ArrayList<String> arr ;
    public Kategori(ArrayList<String> arr) {

       this.arr=arr;
    }

    public String cut(int a){
        return arr.get(a);

    }

    public void setArr(){

    }





}
