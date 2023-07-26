package com.clonecoding.pinterest.global.S3.controller;

import com.clonecoding.pinterest.global.S3.dto.ImageResponseDto;
import com.clonecoding.pinterest.global.S3.entity.Image;
import com.clonecoding.pinterest.global.S3.service.S3Service;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "http://locallhost:3000", allowedHeaders = { "Authorization", "Cache-Control", "Content-Type" }, exposedHeaders = "Authorization")
@RequiredArgsConstructor
@Tag(name = "이미지 기능 테스트 API")
public class S3Controller {

    @NonNull
    private S3Service service;

    @Hidden
    @Operation(summary = "단일 파일 업로드 후 url 리턴")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }

    @Hidden // Swagger에서 숨기기
    @Operation(summary = "백엔드 DB생성용")
    @PostMapping("/save")
    public String saveUrlsToDatabase() {
        service.saveUrlsToDatabase();
        return "Saved all URLs to database";
    }

    @Operation(summary = "GET: DB의 모든 이미지 URL (페이징 x, 무한스크롤 x)")
    @GetMapping("/all")
    public ResponseEntity<List<String>> listAllObjects() {
        return new ResponseEntity<>(service.listAllObjects(), HttpStatus.OK);
    }

    @Operation(summary="GET: DB에서 일정 개수의 이미지 URL (페이징 o, 무한스크롤 지원용)")
    @GetMapping("/images")
    public Slice<Image> getImages(@RequestParam(defaultValue = "0")
                                      @Parameter(description = "요청할 페이지(0부터 시작)") int page,
                                  @RequestParam(defaultValue = "63")
                                  @Parameter(description = "한 페이지에 몇 개 포함할지 지정") int size) {
        return service.getImages(page, size);
    }

    @Hidden
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
}