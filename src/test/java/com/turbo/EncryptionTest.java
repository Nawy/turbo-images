package com.turbo;

import com.turbo.util.EncryptionService;
import com.turbo.util.IdGenerator;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ermolaev on 5/28/17.
 */
public class EncryptionTest {

    @Test
    public void hashIdWork() {
        long id = IdGenerator.generateRandomId();

        String encodedId = EncryptionService.encodeHashId(id);
        long decodedId = EncryptionService.decodeHashId(encodedId);

        assertThat(decodedId).isEqualTo(id);
    }

    @Test
    public void aesEncryptionWork() {
        testAEC("Hello man!!!");
    }

    @Test
    public void aesEncryptionWorkLongTestValue() {
        testAEC("Tumba umba VAKA VAKA VAKA VAKA 123415r235235r2456457");
    }

    private void testAEC(String testValue) {
        String encryptedValue = EncryptionService.encryptAES(testValue);
        String decryptedValue = EncryptionService.decryptAES(encryptedValue);

        assertThat(testValue).isNotEqualTo(encryptedValue);
        assertThat(testValue).isEqualTo(decryptedValue);
    }
}
