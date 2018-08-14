package pl.mzakrze.kms.user_drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mzakrze.kms.user_drive.model.Folder;

public interface FolderRepository extends JpaRepository<Folder, String> {
}
