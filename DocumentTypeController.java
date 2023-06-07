package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/DocumentTypes")
public class DocumentTypeController {
    private final DocumentTypeService documentTypeService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('EDIT_CATALOGS')")
    public ResponseEntity<List<DocumentType>> getAllDocumentTypes() {
        try {
            return ResponseEntity.ok(documentTypeService.getAllDocumentTypes());
        } catch (RuntimeException e) {
            log.error("Error occurred", e);
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('EDIT_CATALOGS')")
    public ResponseEntity<DocumentType> getDocumentType(@PathVariable Long id) {
        try {
            return documentTypeService.getDocumentTypeById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (RuntimeException e) {
            log.error("Error occurred", e);
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('EDIT_CATALOGS')")
    public ResponseEntity<?> createDocumentType(@RequestBody DocumentType documentType) {
        if (documentTypeService.existsDocumentTypeByCode(documentType.getDocumentTypeCode())) {
            return new ResponseEntity<>("Тип документа с таким значением уже существует, Вы можете отредактировать имеющуюся", HttpStatus.METHOD_NOT_ALLOWED);
        }
        try {
            DocumentType result = documentTypeService.saveDocumentType(documentType);
            return new ResponseEntity<>(result.getId(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error("Error occurred", e);
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('EDIT_CATALOGS')")
    public ResponseEntity<?> updateDocumentType(@RequestBody DocumentType documentType, @PathVariable Long id) {
        try {
            if (documentTypeService.canUpdateDocumentType(documentType, id)) {
                return ResponseEntity.ok(documentTypeService.updateDocumentType(documentType, id));
            }
            return new ResponseEntity<>("Тип документа с таким кодом уже существует, Вы можете отредактировать имеющийся", HttpStatus.METHOD_NOT_ALLOWED);
        } catch (RuntimeException e) {
            log.error("Error occurred", e);
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('EDIT_CATALOGS')")
    public ResponseEntity<?> deleteDocumentTypeById(@PathVariable Long id) {
        try {
            documentTypeService.deleteDocumentTypeById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error occurred", e);
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

}
