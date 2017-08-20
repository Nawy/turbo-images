package com.turbo;

import com.turbo.util.EncryptionService;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashidsTest {

    @Test
    public void testMoreThanMaxValues() throws Exception {
        final long value = 9607199254740992L;
        final String hashCode = EncryptionService.encodeHashId(value);
        final long decodedValue = EncryptionService.decodeHashId(hashCode);

        assertThat(value).isEqualTo(decodedValue);
    }

    @Test
    public void testCountWithManyZeros() throws Exception {
        final long value = 960719000000001L;
        final String hashCode = EncryptionService.encodeHashId(value);
        final long decodedValue = EncryptionService.decodeHashId(hashCode);

        assertThat(value).isEqualTo(decodedValue);
    }

    @Test
    public void testCountWithLastZero() throws Exception {
        final long value = 960719000000000L;
        final String hashCode = EncryptionService.encodeHashId(value);
        final long decodedValue = EncryptionService.decodeHashId(hashCode);

        assertThat(value).isEqualTo(decodedValue);
    }
}
