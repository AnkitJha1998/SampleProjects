package com.aj.projects.sample_project.csvToJson.controller;

import com.aj.projects.sample_project.csv_to_json.controller.FileUploadController;
import com.aj.projects.sample_project.csv_to_json.dto.UploadFileResponse;
import com.aj.projects.sample_project.csv_to_json.service.impl.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Mock
    FileService fileService;

    @InjectMocks
    FileUploadController controller;

    @Test
    public void testSuccessControllerResponse() {
        MultipartFile multipartFile = new MockMultipartFile("file", (byte[]) null);
        Mockito.when(fileService.uploadFile(multipartFile)).thenReturn(UploadFileResponse.builder()
                .uploadId("1234id")
                .build());
        ResponseEntity<UploadFileResponse> response = controller.uploadFile(multipartFile);
        Assertions.assertEquals("1234id",response.getBody().getUploadId());
    }


}
