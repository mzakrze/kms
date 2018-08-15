package pl.mzakrze.kms.user_drive.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "file")
public class File {
    private String gid;
    private String name;
    private Folder folder;
    private Blob content;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "gid", unique = true, nullable = false, updatable = false)
    public String getGid() {
        return gid;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "folder_gid")
    public Folder getFolder() {
        return folder;
    }

    @Column(name = "content")
    @Lob @Basic(fetch = FetchType.EAGER)
    public Blob getContent() {
        return content;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public void setContent(Blob content) {
        this.content = content;
    }
}