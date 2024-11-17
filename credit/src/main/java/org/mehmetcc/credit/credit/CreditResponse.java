package org.mehmetcc.credit.credit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mehmetcc.credit.installment.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditResponse {
    @Builder
    @Data
    @AllArgsConstructor
    static class InstallmentDto {
        private Integer id;
        private BigDecimal amount;
        private Boolean status;
        private PaymentType paymentType;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    private Integer creditId;
    private List<InstallmentDto> installments;

    static CreditResponse fromCredit(Credit credit) {
        return new CreditResponse(credit.getId(), credit.getInstallments()
                .stream()
                .map(installment ->
                        InstallmentDto.builder()
                                .id(installment.getId())
                                .amount(installment.getAmount())
                                .status(installment.getStatus())
                                .paymentType(installment.getPaymentType())
                                .createdAt(installment.getCreatedAt())
                                .updatedAt(installment.getUpdatedAt())
                                .build())
                .toList());
    }
}
