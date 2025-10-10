package example.Controller;

import example.Model.MerchantUser;
import example.Model.NewPaymentRequest;
import example.Model.NewPaymentService;
import example.Model.Payment;
import example.Model.User;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class NewPaymentController {
   @Autowired
    NewPaymentService newPaymentService;
    @PostMapping("/NewPayment")
public String newPayment(@RequestBody NewPaymentRequest req) {
    return newPaymentService.addNewPayment(req.user, req.merchant, req.payment);
}

}




