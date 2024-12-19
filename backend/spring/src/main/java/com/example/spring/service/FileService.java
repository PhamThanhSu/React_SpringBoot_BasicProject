package com.example.spring.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    public static final String IMAGE_DIRECTORY = "D:\\TuHoc_SpringBoot\\Spring_Beginner\\backend\\spring\\src\\main\\resources\\static\\uploads\\images";

    public String saveImageToFile(MultipartFile image) throws IOException {
        String imageName = image.getOriginalFilename();
        Path imagePath = Paths.get(IMAGE_DIRECTORY, imageName);
        Files.write(imagePath, image.getBytes());
        return imageName;
    }

    public String saveImageToDatabase(MultipartFile image) throws IOException {
        // Đọc nội dung ảnh và chuyển thành byte[]
        byte[] imageBytes = image.getBytes();

        // Chuyển byte[] thành chuỗi Base64
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

        // Lưu chuỗi Base64 vào database
        // Giả sử bạn đã có logic để lưu encodedImage vào cơ sở dữ liệu (Ví dụ:
        // user.setImage(encodedImage))

        return encodedImage; // trả về Base64 để có thể lưu vào database
    }
}
