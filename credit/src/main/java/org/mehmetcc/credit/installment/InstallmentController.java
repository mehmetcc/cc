package org.mehmetcc.credit.installment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/installments")
public class InstallmentController {
    private final InstallmentService installmentService;

    public InstallmentController(final InstallmentService installmentService) {
        this.installmentService = installmentService;
    }

    @PostMapping("/payment")
    public ResponseEntity<InstallmentPaymentResponse> payInstallment(
            final @RequestBody InstallmentPaymentRequest installmentPaymentRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(installmentService.makePayment(installmentPaymentRequest));
    }
}
