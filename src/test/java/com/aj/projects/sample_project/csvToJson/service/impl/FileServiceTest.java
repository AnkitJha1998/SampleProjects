package com.aj.projects.sample_project.csvToJson.service.impl;

import com.aj.projects.sample_project.csv_to_json.dto.UploadFileResponse;
import com.aj.projects.sample_project.csv_to_json.entity.Upload;
import com.aj.projects.sample_project.csv_to_json.repository.UploadRepository;
import com.aj.projects.sample_project.csv_to_json.service.impl.FileService;
import com.aj.projects.sample_project.csv_to_json.util.CsvToJsonUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private UploadRepository uploadRepository;

    @Mock
    private CsvToJsonUtility csvToJsonUtility;

    @Spy
    @InjectMocks
    FileService fileService;

    @Test
    public void testFileWithoutCsv() {
        MultipartFile file = new MockMultipartFile("hello.pdf", "hello.pdf", "application/pdf", (byte[]) null);
        File savedFile = Mockito.mock(File.class);
        Mockito.when(savedFile.getName()).thenReturn("hello.pdf");

        Mockito.when(fileService.saveFileToDisc(file)).thenReturn(Optional.of(savedFile));
        Upload uploadObj = Upload.builder().id("id1234").fileName("hello.pdf").fileUrl("hello.pdf").build();
        Mockito.when(uploadRepository.save(Mockito.any(Upload.class))).thenReturn(uploadObj);
        UploadFileResponse response = fileService.uploadFile(file);

        Mockito.verify(uploadRepository).save(Mockito.any(Upload.class));
        Assertions.assertEquals(uploadObj.getId(), response.getUploadId());
        Assertions.assertNull(response.getCsvJson());
    }

    @Test
    public void testFileWithCsv() throws JsonProcessingException {
        MultipartFile file = new MockMultipartFile("hello.csv", "hello.csv", "application/csv", (byte[]) null);
        File savedFile = Mockito.mock(File.class);
        Mockito.when(savedFile.getName()).thenReturn("hello.csv");

        Mockito.when(fileService.saveFileToDisc(file)).thenReturn(Optional.of(savedFile));
        Upload uploadObj = Upload.builder().id("id1234").fileName("hello.csv").fileUrl("hello.csv").build();
        Mockito.when(uploadRepository.save(Mockito.any(Upload.class))).thenReturn(uploadObj);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree("{\"hello\":\"world\"}");
        Mockito.when(csvToJsonUtility.csvToJsonNode(savedFile)).thenReturn(node);
        UploadFileResponse response = fileService.uploadFile(file);

        Mockito.verify(uploadRepository).save(Mockito.any(Upload.class));
        Assertions.assertEquals(uploadObj.getId(), response.getUploadId());
        Assertions.assertEquals(response.getCsvJson(), node);
    }

    @Test
    public void testFileWithCsvToJsonError() throws JsonProcessingException {
        MultipartFile file = new MockMultipartFile("hello.csv", "hello.csv", "application/csv", (byte[]) null);
        File savedFile = Mockito.mock(File.class);
        Mockito.when(savedFile.getName()).thenReturn("hello.csv");

        Mockito.when(fileService.saveFileToDisc(file)).thenReturn(Optional.of(savedFile));
        Upload uploadObj = Upload.builder().id("id1234").fileName("hello.csv").fileUrl("hello.csv").build();
        Mockito.when(uploadRepository.save(Mockito.any(Upload.class))).thenReturn(uploadObj);
        Mockito.when(csvToJsonUtility.csvToJsonNode(savedFile)).thenReturn(null);
        UploadFileResponse response = fileService.uploadFile(file);

        Mockito.verify(uploadRepository).save(Mockito.any(Upload.class));
        Assertions.assertEquals(uploadObj.getId(), response.getUploadId());
        Assertions.assertNull(response.getCsvJson());
    }

    @Test
    public void testFileWithoutCsvAndErrorInFileSave() throws IOException {
        MultipartFile file = new MockMultipartFile("hello.pdf", "hello.pdf", "application/pdf", (byte[]) null);
        Mockito.when(fileService.saveFileToDisc(file)).thenReturn(Optional.empty());
        UploadFileResponse response = fileService.uploadFile(file);
        Assertions.assertNull(response.getUploadId());
        Assertions.assertNull(response.getCsvJson());
    }
}
