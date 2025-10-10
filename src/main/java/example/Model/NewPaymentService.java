package example.Model;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class NewPaymentService {
    public String addNewPayment(User user, MerchantUser merchantUser, Payment payment) {
    try {
        Firestore db = FirestoreClient.getFirestore();

        // Example: store in Payments -> userId -> payments subcollection
        DocumentReference userDoc = db.collection("Payments").document(user.getId());

        // Add the payment object to a subcollection
        userDoc.collection("payments").add(payment);

        // Optionally, also store in MerchantPayments
        DocumentReference merchantDoc = db.collection("MerchantPayments").document(merchantUser.getEmail());
        merchantDoc.collection("payments").add(payment);

        return "OK";
    } catch (Exception e) {
        e.printStackTrace();
        return "ERROR";
    }
}

}