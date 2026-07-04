package com.madaporc.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImportExcelDTO {

    private MultipartFile file;
    private String module;
    private Long utilisateurId;
}
