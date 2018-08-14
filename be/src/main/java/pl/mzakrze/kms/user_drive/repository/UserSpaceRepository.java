package pl.mzakrze.kms.user_drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mzakrze.kms.user_drive.model.UserSpace;

public interface UserSpaceRepository extends JpaRepository<UserSpace, String> {
}
