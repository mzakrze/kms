package pl.mzakrze.kms.drive;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import pl.mzakrze.kms.config.SecurityConstants;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user.UserProfileRepository;
import pl.mzakrze.kms.user_drive.repository.DocumentRepository;
import pl.mzakrze.kms.user_drive.repository.FolderRepository;
import pl.mzakrze.kms.user_drive.repository.UserSpaceRepository;

public class TestHelper {

    final UserProfileRepository userProfileRepository;
    final DocumentRepository documentRepository;
    final FolderRepository folderRepository;
    final UserSpaceRepository userSpaceRepository;

    public TestHelper(UserProfileRepository userProfileRepository,
                      DocumentRepository documentRepository,
                      FolderRepository folderRepository,
                      UserSpaceRepository userSpaceRepository) {
        this.userProfileRepository = userProfileRepository;
        this.documentRepository = documentRepository;
        this.folderRepository = folderRepository;
        this.userSpaceRepository = userSpaceRepository;
    }

    public void initWithUser(){
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("email1");
        userProfile.setPassword("password1");
        userProfile.setLoginToken("DjAtjsHAGCJdLlcCODJp");
        userProfile = userProfileRepository.save(userProfile);
        //userProfileRepository.

    }

    public void addAuthenticationHeader(HttpHeaders httpHeaders){
        httpHeaders.set(SecurityConstants.AUTHENTICATION_HEADER, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEakF0anNIQUdDSmRMbGNDT0RKcCIsImV4cCI6MTUzNDU0MDI0Mn0.XZODjaGidF9bcpokVcF1-sRn6xLCbrwOmCe55XucyBK586L2SBfj_23SriDW28yBusUnSTx_P0dfPWKaRvAGew"); // TODO encode
    }

}
