package pl.mzakrze.kms.user_drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mzakrze.kms.user_drive.model.Document;
import pl.mzakrze.kms.user_drive.model.Tag;

import java.util.Collection;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, String> {
    void deleteByDocument(Document document);

    List<Tag> findByDocument(Document document);
}
