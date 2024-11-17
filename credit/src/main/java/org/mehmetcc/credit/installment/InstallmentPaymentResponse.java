package org.mehmetcc.credit.installment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentPaymentResponse {
    private Integer id;
    private PaymentType paymentType;
}
