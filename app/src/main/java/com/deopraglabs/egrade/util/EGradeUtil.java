package com.deopraglabs.egrade.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.*;
import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class EGradeUtil {

    public static final String URL = "http://10.30.9.230:8080";
//    public static final String URL = "http://192.168.1.41:8080";
//    public static final String URL = "http://192.168.1.10:8080";

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

    public static Bitmap convertImageFromByte(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static FileInputStream convertFromBlob(Blob image) throws Exception {
        File tempFile = File.createTempFile("tempfile", null);
        try (InputStream inputStream = image.getBinaryStream();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return new FileInputStream(tempFile);
        } catch (Exception e) {
            tempFile.delete();
            throw e;
        }
    }

}
