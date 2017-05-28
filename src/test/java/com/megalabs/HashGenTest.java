package com.megalabs;

import org.hashids.Hashids;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ermolaev on 5/28/17.
 */
public class HashGenTest {

    @Test
    public void test_simpleHash() {
        final Hashids hashids = new Hashids("this is my salt");


        Random rnd = new Random();
        Map<String, Long> values = new HashMap<>();

        for(int i = 0; i < 10; i++) {
            final long value = Math.abs(rnd.nextInt()); // only positive values
            final String hashCode = hashids.encode(value);

            values.put(hashCode, value);
        }

        values.entrySet().stream().forEach(
                set -> {
                    final long decodedValue = hashids.decode(set.getKey())[0];
                    assertThat(decodedValue).isEqualTo(set.getValue());
                }
        );
    }
}
