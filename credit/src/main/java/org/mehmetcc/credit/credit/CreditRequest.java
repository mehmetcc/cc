package org.mehmetcc.credit.credit;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
class CreditRequest {
    private Integer userId;
    private BigDecimal amount;
    private int installmentCount;
}
