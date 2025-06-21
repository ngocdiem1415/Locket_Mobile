package com.hucmuaf.locket_mobile.activity.auth;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.circleCrop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.repo.UploadResponse;
import com.hucmuaf.locket_mobile.service.ApiClient;
import com.hucmuaf.locket_mobile.service.ImageService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class InfoActivity extends AppCompatActivity {
    private TextInputEditText edName;
    private LottieAnimationView avatarLottie;
    private ImageView imgAvatar;
    private String imageUrl;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ImageView rollback = findViewById(R.id.back);
        imgAvatar = findViewById(R.id.imgAvatar);
        TextInputEditText signUp = findViewById(R.id.teName);

        rollback.setOnClickListener(v -> {
            startActivity(new Intent(InfoActivity.this, ChoiceLoginActivity.class));
            finish();
        });

        // Mở trình chọn ảnh
        avatarLottie = findViewById(R.id.avatarLottie);
        avatarLottie.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            //return reponse to onActivityResult
            imagePickerLauncher.launch(intent);

        });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        assert selectedImageUri != null;
                        Log.d("DEBUG_URI", selectedImageUri.toString());
                        // xử lý ảnh tại đây
                        uploadImage(selectedImageUri);
                    }
                }
        );

        signUp.setOnClickListener(v -> createProfile());
    }

    private void uploadImage(Uri url) {
        if (url == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
        }
        try {
            File imageFile = createTempFileFromUri(url);  // Chuyển Uri thành File thật
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            // Tạo MultipartBody.Part để gửi ảnh , dữ liệu nhận vào của api là part
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);

            // Gọi API upload ảnh
            ImageService imageService = ApiClient.getImageApiService();
            Call<UploadResponse> call = imageService.uploadImage(body);

            call.enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        imageUrl = response.body().getUrl();
                        Log.d("Upload", "URL ảnh: " + imageUrl);

                        Glide.with(InfoActivity.this)
                                .load(imageUrl)
                                .circleCrop()
                                .into(imgAvatar);

                        imgAvatar.setVisibility(View.VISIBLE);
                        avatarLottie.setVisibility(View.GONE);
                    } else {
                        Log.e("UploadResult", "Lỗi: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("UploadError", "Lỗi mạng hoặc server: " + t.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi xử lý ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private File createTempFileFromUri(Uri selectedImageUri) throws IOException {
        Log.d("UploadDebug", "Bắt đầu tạo file tạm từ Uri: " + selectedImageUri);

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = getContentResolver().openInputStream(selectedImageUri);
            int fileSizeInBytes = inputStream.available();
            double fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0);

            if (fileSizeInMB > 4.0) {
                Toast.makeText(this, "Ảnh vượt quá 4MB. Vui lòng chọn ảnh nhỏ hơn.", Toast.LENGTH_SHORT).show();
            }

            // Tạo tên file tạm thời
            String fileName = "temp_image_" + System.currentTimeMillis() + ".jpg";
            Log.d("UploadDebug", "Tên file tạm: " + fileName);

            // Tạo file trong thư mục cache của app
            File tempFile = new File(getCacheDir(), fileName);
            Log.d("UploadDebug", "Đường dẫn file tạm: " + tempFile.getAbsolutePath());

            outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            int totalBytes = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }

            Log.d("UploadDebug", "Tổng số byte đã ghi: " + totalBytes);
            return tempFile;

        } catch (IOException e) {
            Log.e("UploadDebug", "Lỗi khi tạo file tạm: " + e.getMessage(), e);
            throw e;
        }
    }


    private void createProfile() {
        

    }
}
