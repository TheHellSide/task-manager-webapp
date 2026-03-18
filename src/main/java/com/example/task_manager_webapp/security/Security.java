package com.example.task_manager_webapp.security;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
}
