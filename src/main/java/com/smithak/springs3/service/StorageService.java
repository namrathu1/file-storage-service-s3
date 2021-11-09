package com.smithak.springs3.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultipartToFile(file);
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(file.getSize());
        metaData.setContentType(file.getContentType());

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            // s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metaData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileObj.delete();
        return "File uploaded :" + fileName;

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
        return fileName + " removed...";

    }

    public static File convertMultipartToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(convertedFile);
            fileOutputStream.write(file.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            // Log.error("FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertedFile;
    }

}
