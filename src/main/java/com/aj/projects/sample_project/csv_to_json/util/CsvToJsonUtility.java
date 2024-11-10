package com.aj.projects.sample_project.csv_to_json.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CsvToJsonUtility {

    public void sampleConvertCsvToJson(String assetId) {
        System.out.println(Constants.CSV_TO_JSON_SAMPLE_STR + "assetId:" + assetId);
    }

    public JsonNode csvToJsonNode(File csvFile) {

        CsvSchema schema = CsvSchema.builder().setNullValue("").build().withHeader();
        CsvMapper mapper = new CsvMapper();
        try {
            MappingIterator<LinkedHashMap<String, Object>> mappingIterator = mapper.reader()
                    .forType(LinkedHashMap.class)
                    .with(schema)
                    .readValues(csvFile);
            List<LinkedHashMap<String, Object>> listOfCsvRows = mappingIterator.readAll();
            if(listOfCsvRows.size() == 0) {
                log.info("No Rows Detected");
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            mappingIterator.close();
            List<JsonNode> listOfNodes = new ArrayList<>();
            for(LinkedHashMap<String, Object> map: listOfCsvRows) {
                Map<String, Object> unflatten = JsonUnflattener.unflattenAsMap(map);
                listOfNodes.add(mapper.valueToTree(unflatten));
            }
            return objectMapper.convertValue(listOfNodes, JsonNode.class);
        } catch (Exception e) {
            log.error("Error while converting csv to JSON", e);
        }
        return null;
    }

}
