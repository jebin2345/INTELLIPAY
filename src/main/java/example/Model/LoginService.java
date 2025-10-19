package example.Model;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@Service
public class LoginService {
    public User logIn(String email , String pw) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("Users").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        HashMap<String, User> userHashMap = new HashMap<>();
        for (QueryDocumentSnapshot document : documents) {
            User user = document.toObject(User.class);
            userHashMap.put(user.getEmail().toLowerCase(Locale.ROOT), user);
        }
        if (userHashMap.containsKey(email.toLowerCase(Locale.ROOT))) {
            User known_user = userHashMap.get(email.toLowerCase(Locale.ROOT));
            if (known_user.getPassword().equals(pw)) {
                return known_user;
            }
        }
        return null;
    }

    public MerchantUser merchantLogIn(String email , String pw) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("Merchants").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        HashMap<String, MerchantUser> merchantHashMap = new HashMap<>();
        for (QueryDocumentSnapshot document : documents) {
            MerchantUser merchant = document.toObject(MerchantUser.class);
            merchantHashMap.put(merchant.getEmail().toLowerCase(Locale.ROOT), merchant);
        }
        if (merchantHashMap.containsKey(email.toLowerCase(Locale.ROOT))) {
            MerchantUser known_merchant = merchantHashMap.get(email.toLowerCase(Locale.ROOT));
            if (known_merchant.getPassword().equals(pw)) {
                return known_merchant;
            }
        }
        return null;
    }

    public void updateMerchantUPI(MerchantUser merchant) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        
        // Update the merchant's UPI ID in Firestore
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("Merchants")
            .whereEqualTo("email", merchant.getEmail())
            .get();
        
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (!documents.isEmpty()) {
            documents.get(0).getReference().update("upiId", merchant.getUpiId());
        }
    }
}