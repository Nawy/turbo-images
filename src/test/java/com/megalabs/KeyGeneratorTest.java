package com.megalabs;

import com.turbo.repository.util.AerospikeUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ermolaev on 5/20/17.
 */
public class KeyGeneratorTest {

    @Test
    public void generateKey() {
        String key1 = AerospikeUtils.geterateCommentKey("1");
        String key2 = AerospikeUtils.geterateCommentKey("2");
        assertThat(key1).isNotEqualToIgnoringCase(key2);
    }
}
