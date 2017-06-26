package com.megalabs;

import com.turbo.Application;
import com.turbo.model.User;
import com.turbo.repository.aerospike.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ermolaev on 6/26/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AerospikeTest {

    @Autowired
    UserRepository userRepository;


    @Test
    public void getMultiData() {
        List<User> users = userRepository.bulkGet(new String[]{"test3", "test1", "test2"});
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getName()).isEqualTo("test3");
        assertThat(users.get(1).getName()).isEqualTo("test1");
        assertThat(users.get(2).getName()).isEqualTo("test2");
    }
}
