package example.Controller;

import com.google.zxing.WriterException;
import example.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin
public class UPIPaymentController {

    @Autowired
    QRCodeGenerator qrCodeGenerator;

    @Autowired
    TransactionService transactionService;

    @PostMapping(value = "/generateUPIQR")
    public byte[] generateUPIQR(@RequestBody UPIPaymentRequest upiPaymentRequest) throws IOException, WriterException, ExecutionException, InterruptedException {
        System.out.println("UPI QR generation initiated");
        
        // Create UPI payment string in the format: upi://pay?pa=<UPI_ID>&pn=<MERCHANT_NAME>&am=<AMOUNT>&cu=<CURRENCY>&tn=<TRANSACTION_NOTE>
        String upiPaymentString = String.format("upi://pay?pa=%s&pn=%s&am=%s&cu=%s&tn=%s", 
            upiPaymentRequest.getUpiId(),
            upiPaymentRequest.getMerchantName(),
            upiPaymentRequest.getAmount(),
            upiPaymentRequest.getCurrency(),
            upiPaymentRequest.getTransactionNote()
        );
        
        System.out.println("UPI Payment String: " + upiPaymentString);
        
        // Create and save transaction record
        Transaction transaction = new Transaction();
        transaction.setMerchantEmail(upiPaymentRequest.getMerchantEmail());
        transaction.setMerchantName(upiPaymentRequest.getMerchantName());
        transaction.setUpiId(upiPaymentRequest.getUpiId());
        transaction.setAmount(Double.parseDouble(upiPaymentRequest.getAmount()));
        transaction.setCurrency(upiPaymentRequest.getCurrency());
        transaction.setTransactionNote(upiPaymentRequest.getTransactionNote());
        transaction.setQrCodeData(upiPaymentString);
        
        transactionService.saveTransaction(transaction);
        
        return qrCodeGenerator.getQRCodeImage(upiPaymentString);
    }
}
