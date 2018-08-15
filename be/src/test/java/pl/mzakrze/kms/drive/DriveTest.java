package pl.mzakrze.kms.drive;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import pl.mzakrze.kms.api.model.RegisterAttempt_in;
import pl.mzakrze.kms.config.SecurityConstants;
import pl.mzakrze.kms.model.CurrentUser_out;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;
import pl.mzakrze.kms.user_drive.model.Document;
import pl.mzakrze.kms.user_drive.model.Folder;
import pl.mzakrze.kms.user_drive.model.UserSpace;
import pl.mzakrze.kms.user_drive.repository.DocumentRepository;
import pl.mzakrze.kms.user_drive.repository.FolderRepository;
import pl.mzakrze.kms.user_drive.repository.UserSpaceRepository;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableJpaRepositories(basePackages = "pl.mzakrze.kms")
@PropertySource("application.properties") // FIXME - inna baza danych na potrzeby testów
@EnableTransactionManagement
@Rollback
@Transactional
public class DriveTest {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private UserProfileRepository userProfileRepository;
    @Autowired private UserSpaceRepository userSpaceRepository;
    @Autowired private DocumentRepository documentRepository;
    @Autowired private FolderRepository folderRepository;

    private void cleanWholeDb() {
        documentRepository.deleteAll();
        folderRepository.deleteAll();
        userSpaceRepository.deleteAll();
        userProfileRepository.deleteAll();
    }


    @Test
    public void registeredUserShouldHaveDrive() throws Exception {
        // given
        cleanWholeDb();

        // when
        RegisterAttempt_in req = new RegisterAttempt_in();
        req.email = "test@gmail.com";
        req.password = "123456";
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("/api/user/register", req, String.class);

        // then
        UserProfile userProfile = userProfileRepository.getByEmail("test@gmail.com");
        assertNotNull(userProfile);
        List<UserSpace> usersSpaces = userSpaceRepository.findByUserProfile(userProfile);
        assertEquals("Registered user has weird number of spaces", 1L, usersSpaces.size());
        Folder rootFolderInDefaultSpace = usersSpaces.get(0).getRootFolder();
        assertNotNull(rootFolderInDefaultSpace);
        List<Document> documents = documentRepository.findByParentFolder(rootFolderInDefaultSpace);
        assertEquals("Registered user should have <welcome> document", 1L, documents.size());
    }

    @Test
    public void closingAccountClearsDrive() throws Exception {
        // given
        cleanWholeDb();

        RegisterAttempt_in req = new RegisterAttempt_in();
        req.email = "test@gmail.com";
        req.password = "123456";
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("/api/user/register", req, String.class);

        List<String> authentication = responseEntity.getHeaders().get(SecurityConstants.AUTHENTICATION_HEADER);
        String jwToken = authentication.get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.set(SecurityConstants.AUTHENTICATION_HEADER, jwToken);
        HttpEntity entity = new HttpEntity(headers);

        // when
        ResponseEntity<String> closingAccountResponse = restTemplate.exchange("/api/user/close_account", HttpMethod.POST, entity, String.class, "");

        // then
        assertEquals(HttpStatus.OK, closingAccountResponse.getStatusCode());
        assertTrue( documentRepository.findAll().isEmpty());
        assertTrue( folderRepository.findAll().isEmpty());
        assertTrue( userSpaceRepository.findAll().isEmpty());
        assertTrue( userProfileRepository.findAll().isEmpty());
    }

}

