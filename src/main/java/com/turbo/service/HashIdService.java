package com.turbo.service;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by rakhmetov on 01.06.17.
 */
public class HashIdService {

    //make immutable from properties becose if someone change sault everything will goes to hell :D
    private static final Hashids hashids = new Hashids("Kibana_ZAuruzsxca_12437r!#^");

    public static String encodeHashId(Long id) {
        if (id == null) return null;
        return hashids.encode(id);
    }

    public static long decodeHashId(String id) {
        return hashids.decode(id)[0];
    }
}
