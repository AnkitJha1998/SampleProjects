package com.aj.projects.sample_project.csv_to_json.service;

import com.aj.projects.sample_project.csv_to_json.dto.UploadFileResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface IFileService {

    UploadFileResponse uploadFile(MultipartFile file);

}
