package pl.mzakrze.kms.user_drive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mzakrze.kms.api.model.DriveTree_out;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user_drive.model.Document;
import pl.mzakrze.kms.user_drive.model.File;
import pl.mzakrze.kms.user_drive.model.Folder;
import pl.mzakrze.kms.user_drive.model.UserSpace;
import pl.mzakrze.kms.user_drive.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDriveFacade {

    @Autowired private DocumentRepository documentRepository;
    @Autowired private FolderRepository folderRepository;
    @Autowired private FileRepository fileRepository;
    @Autowired private TagRepository tagRepository;
    @Autowired private UserSpaceRepository userSpaceRepository;




    public DriveTree_out.Folder getCurrentSpaceDrive(UserProfile userProfile) {
        UserSpace space = userSpaceRepository.findByUserProfileAndCurrent(userProfile, true);
        if(space == null){
            throw new IllegalStateException("User does not have set current_space");
        }

        DriveTree_out.Folder res = buildTreeRecursively(space.getRootFolder());

        return res;
    }

    private DriveTree_out.Folder buildTreeRecursively(Folder folderOr){
        DriveTree_out.Folder folder = new DriveTree_out.Folder();
        folder.gid = folderOr.getGid();
        folder.name = folderOr.getName();
        folder.blobs = new ArrayList<>();
        folder.documents = new ArrayList<>();
        folder.folders = new ArrayList<>();

        List<Document> documents = documentRepository.findByParentFolder(folderOr);
        List<Folder> folders = folderRepository.findByParentFolder(folderOr);
        List<File> files = fileRepository.findByParentFolder(folderOr);

        for (Document doc : documents) {
            DriveTree_out.Document document = new DriveTree_out.Document();
            document.gid = doc.getGid();
            document.lastEditedTs = "TODO";
            document.lastViewedTs = "TODO";
            document.name = doc.getTitle();
            folder.documents.add(document);
        }

        for (File file : files) {
            DriveTree_out.Blob blob = new DriveTree_out.Blob();
            blob.gid = file.getGid();
            blob.name = file.getName();
            folder.blobs.add(blob);
        }

        for (Folder childFolder : folders) {
            folder.folders.add(buildTreeRecursively(childFolder));
        }

        return folder;
    }

    public boolean isInSpace(UserSpace userSpace, Document document) {
        return isInSpace(userSpace, document.getParentFolder());
    }

    public boolean isInSpace(UserSpace userSpace, File file) {
        return isInSpace(userSpace, file.getParentFolder());
    }

    public boolean isInSpace(UserSpace userSpace, Folder folder) {
        String rootFolderGid = userSpace.getRootFolder().getGid();
        int saveGuard = 1000;
        while (--saveGuard > 0){
            if(folder == null){
                return false;
            }
            if(folder.getGid().equals(rootFolderGid)){
                return true;
            }
            folder = folder.getParentFolder();
        }
        return false;
    }

    public List<String> getTagsInUserSpace(UserSpace userSpace) {
        return buildTagsRecursively(userSpace.getRootFolder());
    }

    public List<String> buildTagsRecursively(Folder folder){
        List<String> res = new ArrayList<>();
        List<Document> documents = documentRepository.findByParentFolder(folder);
        for (Document document : documents) {
            res.addAll(tagRepository.findByDocument(document).stream().map(e -> e.getName()).collect(Collectors.toList()));
        }
        List<Folder> folders = folderRepository.findByParentFolder(folder);
        for (Folder f : folders) {
            res.addAll(buildTagsRecursively(f));
        }
        return res;
    }

    public UserSpace getCurrentSpace(UserProfile userProfile) {
        return userSpaceRepository.findByUserProfileAndCurrent(userProfile, true);
    }
}
