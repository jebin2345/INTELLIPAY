package example.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerPaymentController {

    @GetMapping("/customer-payment")
    public String customerPayment() {
        return "customer-payment";
    }
}
