package com.clonecoding.pinterest.global.S3.controller;

import com.clonecoding.pinterest.global.S3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "http://locallhost:3000", allowedHeaders = { "Authorization", "Cache-Control", "Content-Type" }, exposedHeaders = "Authorization")
public class S3Controller {

    @Autowired
    private S3Service service;

    @Operation(summary = "단일 파일 업로드 후 url 리턴")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }

//    @Operation(summary = "DB생성용")
//    @GetMapping("/save")
//    public String saveUrlsToDatabase() {
//        service.saveUrlsToDatabase();
//        return "Saved all URLs to database";
//    }

    @Operation(summary = "733개의 이미지 URL 전부 GET <-페이징 구현시 한 페이지당 x개 리턴될 예정 <- 무한스크롤 시 페이징 활용")
    @GetMapping("/page/all")
    public ResponseEntity<List<String>> listAllObjects() {
        return new ResponseEntity<>(service.listAllObjects(), HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

//    @DeleteMapping("/delete/{fileName}")
//    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
//        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
//    }
}