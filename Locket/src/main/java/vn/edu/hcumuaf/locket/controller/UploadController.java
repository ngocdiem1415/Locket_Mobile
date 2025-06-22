package vn.edu.hcumuaf.locket.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.java.Log;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcumuaf.locket.model.entity.UploadImageResponse;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            // Gửi tùy chọn folder khi upload
            Map options = ObjectUtils.asMap("folder", "Modis");

            // Kết quả trả về từ Cloudinary sau khi upload ảnh
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            // Trả về secure_url (link ảnh công khai)
            return ResponseEntity.ok(Map.of("url", uploadResult.get("secure_url")));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi upload: " + e.getMessage());
        }
    }


}
