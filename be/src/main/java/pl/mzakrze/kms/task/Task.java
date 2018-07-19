package pl.mzakrze.kms.task;

import pl.mzakrze.kms.user.UserProfile;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.*;

@Entity
@Table(name = "task")
public class Task implements Serializable {
    private Integer id;
    private UserProfile userProfile;
    private String title;
    private String description;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "user_profile_id")
    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    @Column(name = "description", nullable = true)
    public String getDescription() {
        return description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
