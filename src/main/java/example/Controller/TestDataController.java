package example.Controller;

import example.Model.MerchantUser;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class TestDataController {

    @PostMapping("/updateTestMerchant")
    public String updateTestMerchant() {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            
            // Update the test merchant with UPI ID
            MerchantUser testMerchant = new MerchantUser();
            testMerchant.setEmail("merchant@example.com");
            testMerchant.setPassword("merchant123");
            testMerchant.setName("Test Merchant");
            testMerchant.setCurrency("INR");
            testMerchant.setAccountNumber(123456);
            testMerchant.setBranchNumber(789);
            testMerchant.setBankID(1);
            testMerchant.setUpiId("testmerchant@upi"); // Add UPI ID
            
            // Update in Firestore
            dbFirestore.collection("Merchants").document("test-merchant").set(testMerchant);
            
            return "Test merchant updated with UPI ID successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error updating test merchant: " + e.getMessage();
        }
    }
}
