package com.clonecoding.pinterest.global.S3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.clonecoding.pinterest.global.S3.entity.Image;
import com.clonecoding.pinterest.global.S3.repository.S3ImageRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    @Value("${application.bucket.name}")
    private String bucketName;

    @NonNull
    private S3ImageRepository s3ImageRepository;

    @NonNull
    private AmazonS3 s3Client;

    @Value("${myaws.bucket.url}")
    private String bucketUrl;
    public List<String> listAllObjects(){

        // s3에서 가져오기
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withPrefix("flowers/");
        ListObjectsV2Result result;
        List<String> urls = new ArrayList<>();
        do{
            result = s3Client.listObjectsV2(req);
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                String key = objectSummary.getKey();
                String url = bucketUrl  + key;
                urls.add(url);
            }
            String token = result.getNextContinuationToken();
            req.setContinuationToken(token);
        }while(result.isTruncated() == true);
        return urls;
    }

    @Transactional
    public void saveUrlsToDatabase() {
        List<String> urls = listAllObjects();

        for (String url : urls) {
            Image image = new Image(url);
            s3ImageRepository.save(image);
        }
    }

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();

        String fileUrl = "https://" + "kh-myawsbucket" + ".s3." + "ap-northeast-2" + ".amazonaws.com/" + fileName;

        return fileUrl;
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}