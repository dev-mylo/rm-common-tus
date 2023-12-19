package com.rm.process.rest.service;

import com.rm.common.core.exception.RmCommonException;
import com.rm.process.rest.configuration.TusConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class TusUpload {
    TusConfig tusConfig = new TusConfig();

    // Process a tus upload request
    public ResponseEntity<Object> process(HttpServletRequest request, HttpServletResponse response, String filePath, String tusStoragePath, Long tusExpirationPeriod, String uploadUri) {
        try {

            tusConfig.setTusConfig(tusStoragePath, tusExpirationPeriod, uploadUri);

            TusFileUploadService tusFileUploadService = tusConfig.tus();

            // Process a tus upload request
            tusFileUploadService.process(request, response);

            // Get upload information
            UploadInfo uploadInfo = tusFileUploadService.getUploadInfo(request.getRequestURI());

            if (uploadInfo != null && !uploadInfo.isUploadInProgress()) {
                // Progress status is successful: Create file
                createFile(tusFileUploadService.getUploadedBytes(request.getRequestURI()), uploadInfo.getFileName(), filePath);

                // Delete an upload associated with the given upload url
                tusFileUploadService.deleteUpload(request.getRequestURI());
                return httpOkStatus(uploadInfo.getFileName());
            }
        } catch (IOException | TusException e) {
            throw new RmCommonException(e);
        }

        // Generate HTTP Response Headers
        return httpOkStatus();
    }

    // 업로드 성공 후 filePath 경로에 파일 복사
    private void createFile(InputStream is, String filename, String filePath) throws IOException {
        File file = new File(filePath, filename);

        FileUtils.copyInputStreamToFile(is, file);
    }

    // 업로드 진행 중인 경우 header만 반환
    private static ResponseEntity<Object> httpOkStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .build();
    }

    // 업로드 성공 시 filename 반환
    private static ResponseEntity<Object> httpOkStatus(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(filename);
    }
}
