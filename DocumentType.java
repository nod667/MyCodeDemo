package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DocumentType extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String documentTypeCode;
    private String descriptionDocumentTypeCode;
}
