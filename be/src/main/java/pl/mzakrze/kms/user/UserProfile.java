package pl.mzakrze.kms.user;

import org.hibernate.annotations.GenericGenerator;
import pl.mzakrze.kms.user_drive.model.UserSpace;

import javax.persistence.*;

@Entity
@Table(name = "user_profile")
public class UserProfile {
    private String gid;
    private String email;
    private String password;
    private String loginToken;
    private UserSpace currentUserSpace; // FIXME - zmienić optional na true (wymaga zrobienia w hibernacie deffered checka)

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "gid", unique = true, nullable = false, updatable = false)
    public String getGid() {
        return gid;
    }

    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    @Column(name = "login_token")
    public String getLoginToken() {
        return loginToken;
    }

    @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade=CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "current_user_space")
    public UserSpace getCurrentUserSpace() {
        return currentUserSpace;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCurrentUserSpace(UserSpace currentUserSpace) {
        this.currentUserSpace = currentUserSpace;
    }
}