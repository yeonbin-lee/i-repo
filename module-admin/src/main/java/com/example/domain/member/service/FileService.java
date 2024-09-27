package com.example.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface FileService {

    public List<String> saveTempImages(List<MultipartFile> files) throws IOException;
}
