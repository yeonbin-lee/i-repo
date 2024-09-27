package com.example.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class FileServiceImpl implements FileService{

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<String> saveTempImages(List<MultipartFile> files){
        List<String> tempImageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            // 파일 이름 생성 (타임스탬프와 원래 파일 이름을 조합)
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destinationFile = new File(uploadDir, fileName);

            // 파일 저장
            try {
                file.transferTo(destinationFile);
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 중 오류 발생");
            }

            // URL 형식으로 추가 (로컬 파일 경로를 URL 형식으로 변환)
            tempImageUrls.add("/files/" + fileName);
        }

        return tempImageUrls;
    }
}
