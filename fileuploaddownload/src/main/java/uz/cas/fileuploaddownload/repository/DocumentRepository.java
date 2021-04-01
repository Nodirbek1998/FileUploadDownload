package uz.cas.fileuploaddownload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.cas.fileuploaddownload.entity.Document;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {


    @Query("SELECT new Document (d.id, d.name, d.size) from Document d order by d.uploadTime desc")
    List<Document> findAllBy();
}
