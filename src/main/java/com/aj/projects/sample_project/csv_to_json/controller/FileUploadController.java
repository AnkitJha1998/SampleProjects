package com.aj.projects.sample_project.csv_to_json.controller;

import com.aj.projects.sample_project.csv_to_json.dto.UploadFileResponse;
import com.aj.projects.sample_project.csv_to_json.service.impl.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file")
public class FileUploadController {

    FileService fileService;

    public FileUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestPart("file")MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadFile(file));
    }

}