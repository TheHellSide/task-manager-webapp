package com.example.task_manager_webapp.security;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;

@Configuration
public class Security {
    public static String toSafeHtml(String unsafeContent) {
        if (unsafeContent == null) {
            return null;
        }
        return StringEscapeUtils.escapeHtml4(unsafeContent);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String sanitizeUsername(String value) {
        if (value == null) return null;
        return value
                .replaceAll("[^a-zA-Z0-9_]", "")
                .substring(0, Math.min(value.replaceAll("[^a-zA-Z0-9_]", "").length(), 32))
                .trim();
    }

    public static String sanitizeEmail(String value) {
        if (value == null) return null;
        String cleaned = value.replaceAll("[^a-zA-Z0-9._%+\\-@]", "");
        return cleaned.substring(0, Math.min(cleaned.length(), 254)).trim();
    }

    public static String sanitizePassword(String value) {
        if (value == null) return null;
        String cleaned = value
                .replaceAll("[\\x00-\\x1F\\x7F]", "")
                .replace("<", "")
                .replace(">", "");
        return cleaned.substring(0, Math.min(cleaned.length(), 128));
    }

    public static String sanitizeText(String value) {
        if (value == null) return null;
        String cleaned = value
                .replaceAll("[^a-zA-Z0-9\\s.,!?@()\\-'\";\\'{}\\[\\]+*/=<>~^%&|\\\\]", "")
                .replaceAll("\\s{2,}", " ");
        return cleaned.substring(0, Math.min(cleaned.length(), 255)).trim();
    }
}
