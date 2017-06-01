package com.megalabs;

import com.turbo.service.GeneratorService;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ermolaev on 5/28/17.
 */
public class HashGenTest {

    private GeneratorService generatorService = new GeneratorService("Test salt");

    @Test
    public void test_simpleHash() {
        long id = generatorService.generateId();

        String encodedId = generatorService.encodeHashId(id);
        long decodedId = generatorService.decodeHashId(encodedId);

        assertThat(decodedId).isEqualTo(id);
    }
}
