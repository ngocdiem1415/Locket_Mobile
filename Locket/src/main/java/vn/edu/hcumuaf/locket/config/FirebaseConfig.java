

package vn.edu.hcumuaf.locket.config;

import org.springframework.beans.factory.annotation.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {
    @Value("${firebase.database.url}")
    private String databaseUrl;

    @Value("${FIREBASE_CREDENTIALS_JSON}")
    private String firebaseCredentialsJson;

    @Bean
    public FirebaseDatabase firebaseDatabase() throws IOException {
        initializeFirebaseApp();
        return FirebaseDatabase.getInstance();
    }

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        initializeFirebaseApp();
        return FirebaseAuth.getInstance();
    }

    private void initializeFirebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            ByteArrayInputStream credentialsStream = new ByteArrayInputStream(
                    firebaseCredentialsJson.getBytes(StandardCharsets.UTF_8)
            );

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase app initialized successfully");
        }
    }

    // thực hiện các tác vụ bất đồng bộ (asynchronous)
    //Không chặn luồng hoạt động chính của app
//    @Bean(name = "taskExecutor")
//    public Executor taskExecutor() {
//        return Executors.newCachedThreadPool();
//    }

    public static void main(String[] args) throws IOException {
        FirebaseConfig firebaseConfig = new FirebaseConfig();
        System.out.println(firebaseConfig.firebaseDatabase());
    }
}
