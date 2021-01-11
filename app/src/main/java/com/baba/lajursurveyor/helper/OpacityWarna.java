package com.baba.lajursurveyor.helper;

import android.graphics.Color;

public class OpacityWarna {
    private static String putih = "ffffff";
    private static String hitam = "5A5A5A";
    private static String c5 = "C5C5C5";

    private static String[] opacity = {
            "00","03","05","08","A0","0D","0F","12","14","17","1A",
            "1C","1F","21","24","26","29","2B","2E","30","33","36","38","3B","3D",
            "40","42","45","47","4A","4D","4F","52","54","57","59","5C","5E","61",
            "63","66","69","6B","6E","70","73","75","78","7A","7D","80","82","85",
            "87","8A","8C","8F","91","94","96","99","9C","9E","A1","A3","A6","A8",
            "AB","AD","B0","B3","B5","B8","BA","BD","BF","C2","C4","C7","C9","CC",
            "CF","D1","D4","D6","D9","DB","DE","E0","E3","E6","E8","ED","F0","F2",
            "F5","F7","FA","FC","FF"
    };

    public static int x(int index){
        String olah = opacity[index].toLowerCase()+putih;
        return Color.parseColor("#"+olah);
    }

    public static int y(int index){
        String olah = opacity[index].toLowerCase()+hitam;
        return Color.parseColor("#"+olah);
    }

    public static int z(int index){
        String olah = opacity[index].toLowerCase()+c5;
        return Color.parseColor("#"+olah);
    }
}
