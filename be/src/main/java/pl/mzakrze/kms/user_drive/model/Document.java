package pl.mzakrze.kms.user_drive.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "document")
public class Document {
    private String gid;
    private String title;
    private String content;
    private Folder folder;

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

    @Column(name = "content", length = 32768)
    public String getContent() {
        return content;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "folder_gid")
    public Folder getFolder() {
        return folder;
    }


    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }
}
