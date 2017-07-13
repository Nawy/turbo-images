package com.turbo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by rakhmetov on 01.06.17.
 */
public class IdGenerator {

    //ATTENTION!!! DON"T CHANGE THAT OR EVERYTHING WILL CRASH!!! When try to id generate
    private final static long CREATION_YEAR = 2017;
    private final static ThreadLocalRandom random = ThreadLocalRandom.current();

    /**
     * generate time based UUID like YY_DDD_RRR_RRR
     * where
     * YY - year (could absent) now - 2017
     * DDD - days in 365 format
     * RRR_RRR is random numbers
     *
     * @return 9_153_647387
     */
    // TODO WRONG incorrect!
    public static long generateRandomId() {
//        LocalDate now = LocalDate.now().minusYears(CREATION_YEAR);
//        // if year == 0 then return empty string
//        String year = String.valueOf(now.getYear() == 0 ? "" : now.getYear());
//        String day = String.valueOf(now.getDayOfYear());
//        // nextInt with bound is always positive and have positive upper bound
//        String randomNum = String.valueOf(random.nextInt(999_999));
//        return Long.valueOf(year + day + randomNum);
        return generateRandomId2();
    }

    public static long generateRandomId2() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        LocalDateTime now = LocalDateTime.now().minusYears(CREATION_YEAR);
        // if year == 0 then return empty string
        String year = String.valueOf(now.getYear() == 0 ? "" : now.getYear());
        String day = String.valueOf(now.getDayOfYear());
        String nanos = String.valueOf((1000 + now.getNano()) % 10000);
        // nextInt with bound is always positive and have positive upper bound
        String randomNum = String.valueOf(random.nextInt(99));
        return Long.valueOf(year + day + nanos + randomNum);
    }

    public static String generateStringRandomId(){
        return String.valueOf(generateRandomId());
    }
}
