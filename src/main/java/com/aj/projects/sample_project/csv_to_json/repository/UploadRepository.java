package com.aj.projects.sample_project.csv_to_json.repository;

import com.aj.projects.sample_project.csv_to_json.entity.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<Upload, String> {

}