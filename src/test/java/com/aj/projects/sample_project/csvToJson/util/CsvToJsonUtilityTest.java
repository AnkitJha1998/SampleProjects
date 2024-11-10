package com.aj.projects.sample_project.csvToJson.util;

import com.aj.projects.sample_project.csv_to_json.util.CsvToJsonUtility;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.File;

@ExtendWith(MockitoExtension.class)
public class CsvToJsonUtilityTest {

    @Autowired
    ResourceLoader resourceLoader;

    @Spy
    CsvToJsonUtility csvToJsonUtility;

    @Test
    public void testCsvToJsonNode() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("csv1.csv").toURI());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "[{\"name\":\"Ankit\",\"age\":\"26\",\"city\":\"Bangalore\"}]";
        JsonNode node = csvToJsonUtility.csvToJsonNode(file);
        Assertions.assertEquals(objectMapper.writeValueAsString(node), json);

    }

    @Test
    public void testCsvToJsonNodeWithNoRows() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("csv2.csv").toURI());
        JsonNode node = csvToJsonUtility.csvToJsonNode(file);
        Assertions.assertNull(node);
    }

    @Test
    public void testCsvToJsonNodeWithNullFile() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        JsonNode node = csvToJsonUtility.csvToJsonNode(null);
        Assertions.assertNull(node);
    }

}
