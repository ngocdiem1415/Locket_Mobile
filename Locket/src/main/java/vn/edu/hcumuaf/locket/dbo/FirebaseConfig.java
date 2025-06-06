package vn.edu.hcumuaf.locket.dbo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseDatabase firebaseDatabase() throws IOException {
        System.out.println("Firebase database intializing");
        FileInputStream serviceAccount = new FileInputStream("D://DangTranTanLuc//modis-8f5f6-firebase-adminsdk-fbsvc-f76bd29f1f.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://modis-8f5f6-default-rtdb.firebaseio.com")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase app initialized successfully");
        }else{
            System.out.println("Firebase app initialization failed");
        }

        return FirebaseDatabase.getInstance();

    }

    public static void main(String[] args) throws IOException {
        FirebaseConfig  firebaseConfig = new FirebaseConfig();
        System.out.println(firebaseConfig.firebaseDatabase());
    }
}
