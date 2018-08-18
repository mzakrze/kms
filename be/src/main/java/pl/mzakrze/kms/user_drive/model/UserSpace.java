package pl.mzakrze.kms.user_drive.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;
import pl.mzakrze.kms.user.UserProfile;

import javax.persistence.*;

@Entity
@Table(name = "user_space")
public class UserSpace {
    private String gid;
    private UserProfile userProfile;
    private String name;
    private String password;
    private Folder rootFolder;
    private Boolean isCurrent;

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
    @PrimaryKeyJoinColumn(name = "user_profile_gid")
    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "password", length = 1024)
    public String getPassword() {
        return password;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true) // TODO - zmienić na optional = false
    @PrimaryKeyJoinColumn(name = "root_folder_gid")
    public Folder getRootFolder() {
        return rootFolder;
    }

    @Column(name = "is_current", nullable = false)
    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRootFolder(Folder rootFolder) {
        this.rootFolder = rootFolder;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }
}
