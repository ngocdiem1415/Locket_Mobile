package vn.edu.hcumuaf.locket.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.java.Log;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcumuaf.locket.model.entity.UploadImageResponse;
import vn.edu.hcumuaf.locket.service.AuthService;
import vn.edu.hcumuaf.locket.service.ImageService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(
            @RequestHeader("userId") String uid,
            @RequestHeader("Authorization") String authHeader,
            @RequestPart("image") MultipartFile file) throws IOException, FirebaseAuthException {
        return imageService.uploadImage(uid, authHeader, file);
    }
}
