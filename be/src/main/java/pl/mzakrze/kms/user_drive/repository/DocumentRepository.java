package pl.mzakrze.kms.user_drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mzakrze.kms.user_drive.model.Document;

public interface DocumentRepository extends JpaRepository<Document, String> {
}
