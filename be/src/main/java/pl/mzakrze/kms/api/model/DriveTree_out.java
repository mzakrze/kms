package pl.mzakrze.kms.api.model;

import java.util.List;

public class DriveTree_out {

    public Folder root;
    public String ownerGid;

    public static class Document {
        public final String type = "document";
        public String name;
        public String gid;
        public String lastViewedTs;
        public String lastEditedTs;
    }

    public static class Folder {
        public final String type = "folder";
        public String name;
        public String gid;
        public List<Document> documents;
        public List<Folder> folders;
        public List<Blob> blobs;
    }

    public static class Blob {
        public final String type = "blob";
        public String name;
        public String gid;
    }
}
