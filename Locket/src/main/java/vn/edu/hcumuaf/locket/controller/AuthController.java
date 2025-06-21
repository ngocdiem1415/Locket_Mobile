package vn.edu.hcumuaf.locket.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcumuaf.locket.dto.SignupRequest;
import vn.edu.hcumuaf.locket.responsitory.UserDao;
import vn.edu.hcumuaf.locket.service.AuthService;
import vn.edu.hcumuaf.locket.service.MessageService;
import vn.edu.hcumuaf.locket.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")     // Đăng ký người dùng mới
    public ResponseEntity<?> register(@RequestBody SignupRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")     // Đăng nhập người dùng mới
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        return authService.login(body);
    }
}
