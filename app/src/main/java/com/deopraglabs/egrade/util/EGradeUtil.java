package com.deopraglabs.egrade.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EGradeUtil {

//    public static final String URL = "http://10.30.9.230:8080";
    public static final String URL = "http://192.168.1.2:8080";

    public static String formatCpf(String oldCpf) {
        return oldCpf.substring(0, 3) + '.' + oldCpf.substring(3, 6) + '.' + oldCpf.substring(6, 9) + '-' + oldCpf.substring(9);
    }

    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static String formatNumber(String numero) {
        String ddd = numero.substring(0, 2);
        String parte1 = "";
        String parte2 = "";
        if (numero.length() > 10) {
            parte1 = numero.substring(2, 7);
            parte2 = numero.substring(7);
        } else {
            parte1 = numero.substring(2, 6);
            parte2 = numero.substring(6);
        }

        return String.format("(%s) %s-%s", ddd, parte1, parte2);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static String bitmapToBase64(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
