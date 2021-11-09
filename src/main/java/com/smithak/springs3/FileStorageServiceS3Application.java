package com.smithak.springs3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//For upload from Postman http://localhost:9090/file/upload 
// Then in the Body, form-data , key = file, select File option and select a file from C:/User/Smithak/Postman/File and click Send
// Will get uploaded to Amazon S3

// To download from browser or postman http://localhost:9090/file/download/1635939920835_next.png fileName and it will download the file
@SpringBootApplication
public class FileStorageServiceS3Application {

	public static void main(String[] args) {
		SpringApplication.run(FileStorageServiceS3Application.class, args);
	}

}
