package pl.mzakrze.kms;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableJpaRepositories(basePackages = "pl.mzakrze.kms")
@PropertySource("application.properties")
@EnableTransactionManagement
@Transactional
public class DummyDatabaseTest {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    public void insertSomeA(){
        this.assertUserProfileRepositoryEmpty();
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("email");
        userProfile.setPassword("password");

        userProfile = userProfileRepository.save(userProfile);
        assert userProfile.getGid() != null;
        assertFalse(userProfile.getGid().isEmpty());
        System.out.println("user_profile.gid: " + userProfile.getGid());

        List<UserProfile> all = userProfileRepository.findAll();
        assert all.size() == 1;
        assertEquals("email", all.get(0).getEmail());
    }

    @Test
    public void insertSomeB(){
        this.assertUserProfileRepositoryEmpty();
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("email2");
        userProfile.setPassword("password2");

        userProfile = userProfileRepository.save(userProfile);
        assert userProfile.getGid() != null;
        assertFalse(userProfile.getGid().isEmpty());
        System.out.println("user_profile.gid: " + userProfile.getGid());

        List<UserProfile> all = userProfileRepository.findAll();
        assert all.size() == 1;
        assertEquals("email2", all.get(0).getEmail());
    }

    public void assertUserProfileRepositoryEmpty(){
        assert userProfileRepository.findAll().isEmpty() : "Empty repository is NOT empty";
    }
}
