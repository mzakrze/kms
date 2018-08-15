package pl.mzakrze.kms.user_drive.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "folder")
public class Folder {
    private String gid;
    private String name;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "parent_folder_id")
    public Folder getParentFolder() {
        return parentFolder;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }


    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public void setName(String name) {
        this.name = name;
    }
}
