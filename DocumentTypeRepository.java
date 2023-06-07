package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

    Optional<DocumentType> findByDocumentTypeCode(String DocumentTypeCode);

    boolean existsByDocumentTypeCode(String DocumentTypeCode);
}
