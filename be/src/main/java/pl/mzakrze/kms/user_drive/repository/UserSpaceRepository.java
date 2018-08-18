package pl.mzakrze.kms.user_drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mzakrze.kms.user.UserProfile;
import pl.mzakrze.kms.user_drive.model.UserSpace;

import java.util.List;

public interface UserSpaceRepository extends JpaRepository<UserSpace, String> {
    List<UserSpace> findByUserProfile(UserProfile userProfile);

    UserSpace findByUserProfileAndCurrent(UserProfile userProfile, boolean isCurrent);
}
