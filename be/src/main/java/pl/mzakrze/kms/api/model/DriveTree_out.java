package pl.mzakrze.kms.api.model;

import java.util.List;

public class DriveTree_out {

    public Folder root;
    public String ownerGid;

    class Document {
        public final String type = "document";
        public String name;
        public String gid;
        public String lastViewedTs;
        public String lastEditedTs;
    }

    class Folder {
        public final String type = "folder";
        public String name;
        public String gid;
        List<Document> documents;
        List<Blob> blobs;
    }

    class Blob {
        public final String type = "blob";
        public String name;
        public String gid;
    }
}
