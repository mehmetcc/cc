package org.mehmetcc.credit.credit;

import lombok.extern.slf4j.Slf4j;
import org.mehmetcc.credit.commons.user.UserClient;
import org.mehmetcc.credit.installment.Installment;
import org.mehmetcc.credit.installment.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CreditService {

    private final CreditRepository credit;
    private final UserClient userClient;

    @Autowired
    public CreditService(CreditRepository creditRepository, UserClient userClient) {
        this.credit = creditRepository;
        this.userClient = userClient;
    }

    @Transactional
    public Credit createCredit(CreditRequest creditRequest) {
        var user = userClient.getById(creditRequest.getUserId());

        Credit credit = new Credit();
        credit.setUserId(user.getId());
        credit.setAmount(creditRequest.getAmount());
        credit.setStatus(true);
        credit.setInstallments(getIntermediaryInstallments(creditRequest, credit));

        return this.credit.save(credit);
    }

    private List<Installment> getIntermediaryInstallments(CreditRequest creditRequest, Credit credit) {
        List<Installment> installments = new ArrayList<>();
        BigDecimal installmentAmount = creditRequest.getAmount()
                .divide(BigDecimal.valueOf(creditRequest.getInstallmentCount()), RoundingMode.HALF_UP);

        for (int i = 0; i < creditRequest.getInstallmentCount(); i++) {
            Installment installment = new Installment();
            installment.setAmount(installmentAmount);
            installment.setStatus(true);
            installment.setCredit(credit);
            installment.setPaymentType(PaymentType.NOT_PAID);
            installments.add(installment);
        }

        return installments;
    }
}
