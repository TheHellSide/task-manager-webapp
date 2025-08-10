package com.example.to_do_list.Security;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.apache.commons.text.StringEscapeUtils;

public class ContentSanitizer {
    public static String toSafeHtml(String unsafeContent) {
        if (unsafeContent == null) {
            return null;
        }
        return StringEscapeUtils.escapeHtml4(unsafeContent);
    }
}
