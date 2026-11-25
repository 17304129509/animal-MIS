package com.campus.animal.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class Passwords {
    private static final String PREFIX = "{b}";
    private static final BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder();

    private Passwords() {}

    public static String encode(String raw) {
        return PREFIX + BCRYPT.encode(digest(raw));
    }

    public static boolean matches(String raw, String stored) {
        if (isLegacy(stored)) {
            return MessageDigest.isEqual(raw.getBytes(StandardCharsets.UTF_8), stored.getBytes(StandardCharsets.UTF_8));
        }
        return BCRYPT.matches(digest(raw), stored.substring(PREFIX.length()));
    }

    public static boolean isLegacy(String stored) {
        return !(stored.startsWith(PREFIX + "$2") && stored.length() == PREFIX.length() + 60);
    }

    // Pre-hash to fit BCrypt's 72-byte input limit for UTF-8 passwords.
    private static String digest(String raw) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(raw.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
