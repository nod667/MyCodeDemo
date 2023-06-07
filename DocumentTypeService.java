package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    @Transactional
    public List<DocumentType> getAllDocumentTypes() {
        return documentTypeRepository.findAll(Sort.by("id").descending());
    }

    @Transactional
    public Optional<DocumentType> getDocumentTypeById(Long id) {
        return documentTypeRepository.findById(id);
    }

    @Transactional
    public DocumentType saveDocumentType(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }

    @Transactional
    public void deleteDocumentTypeById(Long id) {
        documentTypeRepository.deleteById(id);
    }

    @Transactional
    public DocumentType updateDocumentType(DocumentType documentType, Long id) {
        DocumentType valueToUpdate = documentTypeRepository.findById(id)
                .map(p -> {
                    p.setDocumentTypeCode(documentType.getDocumentTypeCode());
                    p.setDescriptionDocumentTypeCode(documentType.getDescriptionDocumentTypeCode());
                    return p;
                }).orElseThrow();
        return documentTypeRepository.save(valueToUpdate);
    }

    @Transactional
    public boolean existsDocumentTypeByCode(String code) {
        return documentTypeRepository.existsByDocumentTypeCode(code);
    }

    public boolean canUpdateDocumentType(DocumentType DocumentType, Long id) {
        DocumentType documentTypeFromDB = documentTypeRepository.findByDocumentTypeCode(DocumentType.getDocumentTypeCode()).orElse(null);
        return documentTypeFromDB == null || Objects.equals(documentTypeFromDB.getId(), id);
    }

    public void fillDocumentTypesInApplication(CommonDocument document) {
        MetaInformation metaInformation = Optional.ofNullable(document)
                .map(document::getMetaInformation)
                .orElse(new MetaInformation());

        List<DocumentType> documentTypeList = metaInformation.getDocumentTypes();

        if (!CollectionUtils.isEmpty(documentTypeList)) {
            Map<String, DocumentType> documentTypeCatalogMap = new HashMap<>();
            documentTypeRepository.findAll()
                    .forEach(documentType -> documentTypeCatalogMap.put(documentType.getDocumentTypeCode(), documentType));

            documentTypeList = documentTypeList.stream()
                    .peek(documentType -> {
                        String documentTypeCode = documentType.getDocumentTypeCode();
                        if (documentTypeCatalogMap.containsKey(documentTypeCode)) {
                            documentType.setDescriptionDocumentTypeCode(documentTypeCatalogMap.get(documentTypeCode).getDescriptionDocumentTypeCode());
                        }
                    })
                    .toList();

            metaInformation.setDocumentTypes(documentTypeList);
        }
    }

}
