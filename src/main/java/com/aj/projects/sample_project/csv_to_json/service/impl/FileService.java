package com.aj.projects.sample_project.csv_to_json.service.impl;

import com.aj.projects.sample_project.csv_to_json.dto.UploadFileResponse;
import com.aj.projects.sample_project.csv_to_json.entity.Upload;
import com.aj.projects.sample_project.csv_to_json.repository.UploadRepository;
import com.aj.projects.sample_project.csv_to_json.service.IFileService;
import com.aj.projects.sample_project.csv_to_json.util.Constants;
import com.aj.projects.sample_project.csv_to_json.util.CsvToJsonUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class FileService implements IFileService {

    @Value("${project.file-location}")
    String fileLoc;
    private UploadRepository uploadRepository;
    private CsvToJsonUtility csvToJsonUtility;

    public FileService(UploadRepository uploadRepository, CsvToJsonUtility csvToJsonUtility) {
        this.uploadRepository = uploadRepository;
        this.csvToJsonUtility = csvToJsonUtility;
    }

    @Transactional
    @Override
    public UploadFileResponse uploadFile(MultipartFile multipartFile) {
        log.info("Initiating multipartFile scan");
        Optional<File> savedFile = saveFileToDisc(multipartFile);
        if(savedFile.isEmpty()) {
            log.error("Error while saving file to disc");
            return new UploadFileResponse();
        }
        Upload uploadEntity = uploadRepository.save(getUploadEntity(savedFile.get()));
        UploadFileResponse response = UploadFileResponse.builder()
                .uploadId(uploadEntity.getId())
                .build();
        if(StringUtils.equals(FilenameUtils.getExtension(savedFile.get().getName()), Constants.CSV)) {
            response.setCsvJson(csvToJsonUtility.csvToJsonNode(savedFile.get()));
        }
        return response;
    }

    protected Optional<File> saveFileToDisc(MultipartFile multipartFile) {
        try {
            String fileName = getFileName(multipartFile);
            File file = new File(fileLoc + File.separator + fileName);
            multipartFile.transferTo(file);
            return Optional.of(file);
        } catch (Exception e) {
            log.error("Error while saving file to Disc");
        }
        return Optional.empty();
    }

    private String getFileName(MultipartFile file) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String timestamp = format.format(new Date());
        return FilenameUtils.getBaseName(file.getOriginalFilename()) + timestamp + "." + FilenameUtils.getExtension(file.getOriginalFilename());
    }

    protected Upload getUploadEntity(File savedFile) {
        return Upload.builder()
                .fileUrl(fileLoc + savedFile.getName())
                .fileName(savedFile.getName())
                .build();
    }

}
