package com.aj.projects.sample_project.csv_to_json.configs.executor;

import com.aj.projects.sample_project.csv_to_json.configs.task.CsvConverterTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Component
public class CsvToJsonExecutor {

    public void execute(List<CsvConverterTask> list) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Map<String, Future<String>> resultMap = new HashMap<>();
        list.forEach(csvConverterTask -> resultMap.put(csvConverterTask.getAssetId(), executorService.submit(csvConverterTask)));
        resultMap.keySet().forEach(key -> {
            Future<String> res = resultMap.get(key);
            try{
                System.out.println(key + " : " + res.get());
            } catch (Exception e) {
                System.out.println("Error with assetId: "+ key + "\n" + ExceptionUtils.getStackTrace(e));
            }
        });
        executorService.shutdown();
    }

}