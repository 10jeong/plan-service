package com.yeoljeong.tripmate.application.port;

import org.springframework.web.multipart.MultipartFile;

public interface StorageReader {

  String upload(MultipartFile image, String fileName);
}
