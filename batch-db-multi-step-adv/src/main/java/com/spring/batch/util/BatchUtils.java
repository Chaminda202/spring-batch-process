package com.spring.batch.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BatchUtils {
    private BatchUtils() {
    }

    public static String calculateCheckSumMD5(String filePath) throws IOException, NoSuchAlgorithmException {
        byte[] data = Files.readAllBytes(Paths.get(filePath));
        byte[] hash = MessageDigest.getInstance("MD5").digest(data);
        String checksum = new BigInteger(1, hash).toString(16);
        System.out.println("File Path " + filePath +" Check Sum MD5 " + checksum);
        return checksum;
    }

    public static boolean areFilesIdenticalMemoryMapped(final String inoutPath, final String outputPath) throws IOException {
        Path a = new File(inoutPath).toPath();
        Path b = new File(outputPath).toPath();

        try (final FileChannel fca = FileChannel.open(a, StandardOpenOption.READ);
             final FileChannel fcb = FileChannel.open(b, StandardOpenOption.READ)) {
            final MappedByteBuffer mbba = fca.map(FileChannel.MapMode.READ_ONLY, 0, fca.size());
            final MappedByteBuffer mbbb = fcb.map(FileChannel.MapMode.READ_ONLY, 0, fcb.size());
            return mbba.equals(mbbb);
        }
    }

    public static String checksum(String filepath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        // DigestInputStream is better, but you also can hash file like this.
        try (InputStream fis = new FileInputStream(filepath)) {
            byte[] buffer = new byte[1024];
            int nread;
            while ((nread = fis.read(buffer)) != -1) {
                md.update(buffer, 0, nread);
            }
        }

        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();

    }
}
