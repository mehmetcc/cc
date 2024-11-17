package org.mehmetcc.credit.credit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {
    private Integer userId;
    private BigDecimal amount;
    private int installmentCount;
}
