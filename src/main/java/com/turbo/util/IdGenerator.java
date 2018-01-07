package com.turbo.util;

import com.turbo.model.exception.InternalServerErrorHttpException;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by rakhmetov on 01.06.17.
 */
public class IdGenerator {

    //ATTENTION!!! DON'T CHANGE THAT OR EVERYTHING WILL CRASH!!! When try to id generate
    private final static long CREATION_YEAR = 2017;
    private final static int MAX_LONG_LENGTH = String.valueOf(Long.MAX_VALUE).length();
    public final static int ITERATIONS_TO_GENERATE_ID = 10;


    public static long generateRandomId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        LocalDateTime now = LocalDateTime.now().minusYears(CREATION_YEAR);
        // if year == 0 then return empty string
        String year = String.valueOf(now.getYear() == 0 ? "" : now.getYear());
        String day = String.valueOf(now.getDayOfYear());
        String nanos = String.valueOf(now.getNano() / 1_000_000);
        // nextInt with bound is always positive and have positive upper bound
        String randomNum = String.valueOf(random.nextInt(99));
        return Long.valueOf(year + day + nanos + randomNum);
    }

    public static long generateRandomId(long userId) {
        String userBasedRandomId = String.valueOf(generateRandomId()) + String.valueOf(userId);
        return Long.valueOf(
                userBasedRandomId.substring(
                        0,
                        Integer.min(userBasedRandomId.length(), MAX_LONG_LENGTH)
                )
        );
    }

    public static String generateStringRandomId() {
        return String.valueOf(generateRandomId());
    }
}
