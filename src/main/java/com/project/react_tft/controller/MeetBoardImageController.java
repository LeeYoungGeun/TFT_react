package com.project.react_tft.controller;

import com.project.react_tft.dto.image.ImageResultDTO;
import io.swagger.v3.oas.annotations.Operation;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Controller
@Log4j2
@RequestMapping("/api/files")
public class MeetBoardImageController {


        @Value("C:\\upload") // 경로를 외부 설정으로 빼기 (환경 설정 파일에서 관리)
        private String uploadPath;

        @Operation(summary = "파일 업로드", description = "POST 방식으로 파일을 업로드합니다.")
        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<List<ImageResultDTO>> upload(@RequestParam("files") List<MultipartFile> files) {

            if (files == null || files.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            List<ImageResultDTO> list = new ArrayList<>();

            files.forEach(multipartFile -> {
                String originalName = multipartFile.getOriginalFilename();
                log.info("Original file name: {}", originalName);

                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);
                boolean isImage = false;

                try {
                    multipartFile.transferTo(savePath);

                    // 이미지 파일일 경우 썸네일 생성
                    if (Files.probeContentType(savePath).startsWith("image")) {
                        isImage = true;
                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                    }
                } catch (IOException e) {
                    log.error("Failed to upload file", e);
                    return;
                }

                list.add(ImageResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalName)
                        .img(isImage).build());
            });

            return ResponseEntity.ok(list);
        }

        @Operation(summary = "파일 조회", description = "GET 방식으로 파일을 조회합니다.")
        @GetMapping("/view/{fileName}")
        public ResponseEntity<org.springframework.core.io.Resource> viewFile(@PathVariable String fileName) {

            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            try {
                headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(resource.getFile().toPath()));
            } catch (IOException e) {
                log.error("Failed to determine file type", e);
                return ResponseEntity.internalServerError().build();
            }

            return ResponseEntity.ok().headers(headers).body(resource);
        }

        @Operation(summary = "파일 삭제", description = "DELETE 방식으로 파일을 삭제합니다.")
        @DeleteMapping("/{fileName}")
        public ResponseEntity<Map<String, Boolean>> removeFile(@PathVariable String fileName) {

            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            Map<String, Boolean> resultMap = new HashMap<>();
            boolean removed = false;

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                removed = resource.getFile().delete();

                // 이미지 파일일 경우 썸네일도 삭제
                if (removed && contentType != null && contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                }

            } catch (IOException e) {
                log.error("Failed to delete file", e);
            }

            resultMap.put("result", removed);
            return ResponseEntity.ok(resultMap);
        }
    }


