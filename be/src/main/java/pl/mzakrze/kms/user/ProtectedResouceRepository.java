package pl.mzakrze.kms.user;

import java.util.ArrayList;

public class ProtectedResouceRepository {
    private static ProtectedResouceRepository instance = null;

    private ArrayList<ProtectedResource> database;

    private ProtectedResouceRepository() {
        this.database = new ArrayList<>();
    }

    public static ProtectedResouceRepository getInstance(){
        if(instance == null){
            instance = new ProtectedResouceRepository();
        }
        return instance;
    }

    public ProtectedResource byUserGid(String userGid){
        return database.stream()
                .filter(e -> e.userGid.equals(userGid))
                .findFirst().orElse(null);
    }

    public void put(String userGid, String newSecret) {
        ProtectedResource protectedResource = byUserGid(userGid);
        if(protectedResource == null){
            protectedResource = new ProtectedResource();
            protectedResource.secret = newSecret;
            protectedResource.userGid = userGid;
            database.add(protectedResource);
        } else {
            protectedResource.secret = newSecret;
        }
    }
}
