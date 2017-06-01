package com.turbo.service;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by rakhmetov on 01.06.17.
 */
@Service
public class GeneratorService {

    //ATTENTION!!! DON"T CHANGE THAT OR EVERYTHING WILL CRASH!!! When try to id generate
    private final static long CREATION_YEAR = 2017;
    private final static ThreadLocalRandom random = ThreadLocalRandom.current();
    private final Hashids hashids;

    @Autowired
    public GeneratorService(@Value("${hash.id-salt}") String salt) {
        hashids = new Hashids(salt);
    }

    /**
     * generate time based UUID like YY_DDD_RRR_RRR
     * where
     * YY - year (could absent) now - 2017
     * DDD - days in 365 format
     * RRR_RRR is random numbers
     *
     * @return 9_153_647387
     */
    public long generateId() {
        LocalDate now = LocalDate.now().minusYears(CREATION_YEAR);
        // if year == 0 then return empty string
        String year = String.valueOf(now.getYear() == 0 ? "" : now.getYear());
        String day = String.valueOf(now.getDayOfYear());
        // nextInt with bound is always positive and have positive upper bound
        String randomNum = String.valueOf(random.nextInt(999_999));
        return Long.valueOf(year + day + randomNum);
    }

    public String encodeHashId(long id) {
        return hashids.encode(id);
    }

    public long decodeHashId(String id) {
        return hashids.decode(id)[0];
    }
}
