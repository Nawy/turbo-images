package com.turbo.repository.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by ermolaev on 5/20/17.
 */
public abstract class UserIdGenerator {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddN");

    public static String generateCommentKey(String userId) {
        return String.format("%s-%s", LocalDateTime.now().format(formatter), userId);
    }
}
