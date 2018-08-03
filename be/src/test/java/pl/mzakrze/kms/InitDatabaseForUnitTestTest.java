package pl.mzakrze.kms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableJpaRepositories(basePackages = "pl.mzakrze.kms")
@PropertySource("application.properties")
@EnableTransactionManagement
@Transactional
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:before_test_InitDatabaseForUnitTestTest.sql") })
public class InitDatabaseForUnitTestTest {
    @Autowired private UserProfileRepository userProfileRepository;

    @Test
    public void testFirstTime(){
        doTest();
    }

    @Test
    public void testSecondTime(){
        doTest();
    }

    private void doTest(){
        List<UserProfile> all = userProfileRepository.findAll();
        assertEquals(1, all.size());
        assertEquals("email", all.get(0).getEmail());
        assertNotNull(all.get(0).getGid());
        assertFalse(all.get(0).getGid().isEmpty());
    }
}
