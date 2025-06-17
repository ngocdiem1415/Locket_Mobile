package com.hucmuaf.locket_mobile.activity.auth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.activity.TakeActivity;
import com.hucmuaf.locket_mobile.service.FirebaseService;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edEmail, edPassword;
    private FirebaseAuth mAuth;

    private String password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.edemailLg);
        edPassword = findViewById(R.id.edpasswordLg);
        TextView txtSignup = findViewById(R.id.txtSignup);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txtForgotPass = findViewById(R.id.txtForgotPass);
        ImageView imgBack = findViewById(R.id.arrowBack);

        mAuth = FirebaseService.getInstance().getAuth();

        // Nhận dữ liệu đăng nhập từ intent nếu có
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            edEmail.setText(extras.getString("email", ""));
            edPassword.setText(extras.getString("password", ""));
        }

        // Ban đầu disable nút login và set màu nhạt
        btnLogin.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email = edEmail.getText().toString().trim();
                password = edPassword.getText().toString().trim();

                // Kiểm tra nếu cả email và password đều không rỗng
                boolean isValid = !email.isEmpty() && !password.isEmpty();
                btnLogin .setEnabled(isValid);

                int backgroundId = isValid ? R.drawable.radius_border : R.drawable.radius_border_disable;
                btnLogin.setBackground(ContextCompat.getDrawable(LoginActivity.this, backgroundId));

            }
        };
        edEmail.addTextChangedListener(textWatcher);
        edPassword.addTextChangedListener(textWatcher);

        btnLogin.setOnClickListener(view -> attemptLogin());

        imgBack.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, ChoiceLoginActivity.class))
        );

        txtSignup.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, SignupActivity.class))
        );

        txtForgotPass.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class))
        );


    }

    private void attemptLogin() {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Không được bỏ trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String userId = firebaseUser != null ? firebaseUser.getUid() : null;
                        if (userId == null) {
                            Toast.makeText(this, "Không thể lấy UID người dùng.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(LoginActivity.this, TakeActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
