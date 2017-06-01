package com.megalabs;

import com.turbo.repository.util.IdGenerator;
import com.turbo.service.HashIdService;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ermolaev on 5/28/17.
 */
public class HashGenTest {

    private HashIdService hashIdService = new HashIdService("Test salt");

    @Test
    public void hashIdWork() {
        long id = IdGenerator.generateRandomId();

        String encodedId = hashIdService.encodeHashId(id);
        long decodedId = hashIdService.decodeHashId(encodedId);

        assertThat(decodedId).isEqualTo(id);
    }
}
