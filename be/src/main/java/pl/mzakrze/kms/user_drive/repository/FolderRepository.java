package pl.mzakrze.kms.user_drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mzakrze.kms.user_drive.model.Document;
import pl.mzakrze.kms.user_drive.model.Folder;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, String> {
    List<Folder> findByParentFolder(Folder folder);
}
