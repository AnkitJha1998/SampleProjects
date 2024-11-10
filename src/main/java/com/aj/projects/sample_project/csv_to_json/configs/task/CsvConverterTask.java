package com.aj.projects.sample_project.csv_to_json.configs.task;

import com.aj.projects.sample_project.csv_to_json.util.CsvToJsonUtility;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
@Slf4j
public class CsvConverterTask implements Callable<String> {

    private String assetId;
    private CsvToJsonUtility utility;

    public CsvConverterTask(String assetId, CsvToJsonUtility utility) {
        this.assetId = assetId;
        this.utility = utility;
    }

    @Override
    public String call() throws Exception {
        log.info("Initiating Call for assetId: {}", assetId);
        for(int i = 0; i<5; i++) {
            utility.sampleConvertCsvToJson(assetId + ":" + i);
            Thread.sleep(1000);
        }
        return "Response={assetId: " + assetId + "}, done";
    }

    public String getAssetId() { return assetId;}
}
