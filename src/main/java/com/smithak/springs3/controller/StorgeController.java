package com.smithak.springs3.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.smithak.springs3.service.StorageService;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class StorgeController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        String result = storageService.uploadFile(file);
        return new ResponseEntity<String>(result, null, HttpStatus.SC_OK);
    }

    @GetMapping("/download/{fileName}")
    public void downloadFile(HttpServletResponse response, @PathVariable String fileName) {
        byte[] data = storageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try {
            StreamUtils.copy(data, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ResponseEntity<ByteArrayResource> return
        // ResponseEntity.ok().contentType(contentType(fileName)).contentLength(data.length)
        // .header("Content-Disposition", "attachment;filename=\"" + fileName +
        // "\"").body(resource);

    }

    private MediaType contentType(String fileName) {
        String[] arr = fileName.split("\\.");
        String type = arr[arr.length - 1];
        switch (type) {
        case "txt":
            return MediaType.TEXT_PLAIN;
        case "png":
            return MediaType.IMAGE_PNG;
        case "jpg":
            return MediaType.IMAGE_JPEG;
        default:
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @GetMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        storageService.deleteFile(fileName);
        return new ResponseEntity<>(fileName + " deleted.", null, HttpStatus.SC_OK);
    }

}
