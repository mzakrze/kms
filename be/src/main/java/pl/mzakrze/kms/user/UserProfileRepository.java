package pl.mzakrze.kms.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    UserProfile getByLoginToken(String loginToken);
    UserProfile getByEmail(String email);
}
