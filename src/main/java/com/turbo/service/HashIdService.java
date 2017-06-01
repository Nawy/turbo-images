package com.turbo.service;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by rakhmetov on 01.06.17.
 */
@Component
public class HashIdService {

    private final Hashids hashids;

    @Autowired
    public HashIdService(@Value("${hash.id-salt}") String salt) {
        hashids = new Hashids(salt);
    }

    public String encodeHashId(long id) {
        return hashids.encode(id);
    }

    public long decodeHashId(String id) {
        return hashids.decode(id)[0];
    }
}
