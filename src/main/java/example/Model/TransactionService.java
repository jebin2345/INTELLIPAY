package example.Model;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class TransactionService {

    public void saveTransaction(Transaction transaction) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("Transactions").document(transaction.getTransactionId()).set(transaction);
        System.out.println("Transaction saved: " + transaction.getTransactionId());
    }

    public List<Transaction> getMerchantTransactions(String merchantEmail) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("Transactions")
                .whereEqualTo("merchantEmail", merchantEmail)
                .get();
        
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Transaction> transactions = new ArrayList<>();
        
        for (QueryDocumentSnapshot document : documents) {
            transactions.add(document.toObject(Transaction.class));
        }
        
        return transactions;
    }

    public void updateTransactionStatus(String transactionId, String status) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("Transactions")
                .document(transactionId)
                .update("status", status);
        System.out.println("Transaction status updated: " + transactionId + " -> " + status);
    }
}
