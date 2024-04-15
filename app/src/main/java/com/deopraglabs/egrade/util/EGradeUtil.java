package com.deopraglabs.egrade.util;

import java.io.*;
import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class EGradeUtil {


    public static String formatCpf(String oldCpf) {
        return String.format(oldCpf, oldCpf.substring(0, 2)+ "." +  oldCpf.substring(3, 5) + "." + oldCpf.substring(6, 8) + "-" +  oldCpf.substring(9, 10));
    }

    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
