package pl.mzakrze.kms.user;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mzakrze.kms.user.exceptions.EmailAlreadyExistsException;
import pl.mzakrze.kms.user.exceptions.IllegalPasswordException;
import pl.mzakrze.kms.user_drive.model.Document;
import pl.mzakrze.kms.user_drive.model.File;
import pl.mzakrze.kms.user_drive.model.Folder;
import pl.mzakrze.kms.user_drive.model.UserSpace;
import pl.mzakrze.kms.user_drive.repository.DocumentRepository;
import pl.mzakrze.kms.user_drive.repository.FileRepository;
import pl.mzakrze.kms.user_drive.repository.FolderRepository;
import pl.mzakrze.kms.user_drive.repository.UserSpaceRepository;

import java.util.List;

@Service
public class UserProfileFacade {
    @Autowired private UserProfileRepository userProfileRepository;
    @Autowired private UserSpaceRepository userSpaceRepository;
    @Autowired private FolderRepository folderRepository;
    @Autowired private FileRepository fileRepository;
    @Autowired private DocumentRepository documentRepository;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserProfile registerUser(String email, String password) throws EmailAlreadyExistsException, IllegalPasswordException {

        PasswordValidator.validatePassword(password);

        if(userProfileRepository.getByEmail(email) != null) {
            throw new EmailAlreadyExistsException();
        }

        String loginToken = RandomStringUtils.randomAlphabetic(20); // FIXME - safe / truly random
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        UserProfile userProfile = new UserProfile();
        userProfile.setPassword(encodedPassword);
        userProfile.setEmail(email);
        userProfile.setLoginToken(loginToken);
        userProfileRepository.save(userProfile);

        Document document = new Document();
        document.setContent("This is a welcome to document"); // TODO wynieść tworzenie gdzie indziej + uzupełnić
        document.setTitle("Welcome");

        Folder folder = new Folder();
        folder.setName("Root");
        folder.setParentFolder(null);
        folder = folderRepository.save(folder);
        document.setParentFolder(folder);
        documentRepository.save(document);

        UserSpace userSpace = new UserSpace();
        userSpace.setPassword(encodedPassword); // domyślna przestrzeń ma hasło takie jak konto
        userSpace.setName("default");
        userSpace.setRootFolder(folder);
        userSpace.setUserProfile(userProfile);

        userProfile.setCurrentUserSpace(userSpace);
        userProfileRepository.save(userProfile);

        return userProfile;
    }

    public void closeAccountPermanently(UserProfile userProfile) {
        List<UserSpace> userSpaces = userSpaceRepository.findByUserProfile(userProfile);
        userProfile.setCurrentUserSpace(null);
        userProfileRepository.save(userProfile);
        for (UserSpace userSpace : userSpaces) {
            Folder rootFolder = userSpace.getRootFolder();
            clearPermanentlyFolderRecursively(rootFolder);
            userSpace.setRootFolder(null);
            userSpaceRepository.save(userSpace);
            folderRepository.delete(rootFolder);
        }
        userSpaceRepository.delete(userSpaces);
        userProfileRepository.delete(userProfile);
    }

    private void clearPermanentlyFolderRecursively(Folder folder) {
        List<Document> documents = documentRepository.findByParentFolder(folder);
        List<Folder> folders = folderRepository.findByParentFolder(folder);
        List<File> files = fileRepository.findByParentFolder(folder);

        documentRepository.delete(documents);
        fileRepository.delete(files);

        for (Folder f : folders) {
            clearPermanentlyFolderRecursively(f);
            folderRepository.delete(f);
        }
    }
}
