//package com.example.domain.member.service;
//
//import com.example.domain.admin.controller.dto.request.CreateNoticeRequest;
//import com.example.domain.member.entity.Notice;
//import com.example.domain.member.repository.NoticeRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class NoticeServiceImpl implements NoticeService{
//
//    private final NoticeRepository noticeRepository;
//    private final FileService fileService;
//
//    public void createNotice(CreateNoticeRequest request, List<MultipartFile> imageFiles) throws IOException {
//        // 1. 이미지를 임시 경로에 업로드
//        List<String> tempImageUrls = fileService.saveTempImages(imageFiles);
//
//        // 2. 게시글 저장
//        Notice savedNotice = noticeRepository.save(convertToEntity(noticeDTO));
//
//        // 3. 게시글이 성공적으로 저장된 경우에만 이미지를 정식 경로에 저장
//        imageService.moveToPermanentStorage(savedNotice.getId(), tempImageUrls);
//    }
//}
