package example.Model;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class FBInitialize {

    @PostConstruct
    public void initialize() {
        try {
            String credPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            InputStream serviceAccount;

            if (credPath != null && !credPath.isEmpty()) {
                serviceAccount = new FileInputStream(credPath);
            } else {
                serviceAccount = this.getClass().getResourceAsStream("/firebase-service-account.json");
                if (serviceAccount == null) {
                    throw new RuntimeException("Firebase credentials not found in env var or resources.");
                }
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://intellipay-jh.firebaseio.com")  // ← CHANGE THIS
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            System.out.println("✅ Firestore initialized successfully");
        } catch (Exception e) {
            System.err.println("❌ Firebase initialization failed");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
