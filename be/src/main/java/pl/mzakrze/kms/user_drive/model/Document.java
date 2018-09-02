package pl.mzakrze.kms.user_drive.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "document")
public class Document {
    private String gid;
    private String title;
    private byte[] content;
    private Folder parentFolder;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "gid", unique = true, nullable = false, updatable = false)
    public String getGid() {
        return gid;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    @Column(name = "content")
    public byte[] getContent() {
        return content;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "parent_folder_gid")
    public Folder getParentFolder() {
        return parentFolder;
    }


    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }
}
