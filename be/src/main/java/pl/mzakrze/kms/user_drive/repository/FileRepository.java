package pl.mzakrze.kms.user_drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mzakrze.kms.user_drive.model.File;
import pl.mzakrze.kms.user_drive.model.Folder;

import java.util.List;

public interface FileRepository extends JpaRepository<File, String> {

    List<File> findByParentFolder(Folder folder);
}
