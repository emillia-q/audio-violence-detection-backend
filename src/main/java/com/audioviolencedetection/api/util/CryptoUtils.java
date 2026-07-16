package com.audioviolencedetection.api.util;

import com.audioviolencedetection.api.exception.CryptoException;
import org.apache.tomcat.util.buf.HexUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class CryptoUtils {

    private CryptoUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String hashDeviceSecret(String secret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
            return HexUtils.toHexString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("SHA-256 algorithm not available", e);
        }
    }
}
