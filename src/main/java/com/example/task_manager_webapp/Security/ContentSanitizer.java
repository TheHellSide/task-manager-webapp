package com.example.task_manager_webapp.Security;

import org.apache.commons.text.StringEscapeUtils;

public class ContentSanitizer {
    public static String toSafeHtml(String unsafeContent) {
        if (unsafeContent == null) {
            return null;
        }
        return StringEscapeUtils.escapeHtml4(unsafeContent);
    }
}
