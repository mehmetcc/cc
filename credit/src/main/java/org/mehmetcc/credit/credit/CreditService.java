package org.mehmetcc.credit.credit;

import org.mehmetcc.credit.commons.date.DateUtils;
import org.mehmetcc.credit.commons.user.UserClient;
import org.mehmetcc.credit.installment.Installment;
import org.mehmetcc.credit.installment.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class CreditService {

    private final CreditRepository creditRepository;
    private final UserClient userClient;

    @Autowired
    public CreditService(final CreditRepository creditRepository, final UserClient userClient) {
        this.creditRepository = creditRepository;
        this.userClient = userClient;
    }

    public List<Credit> getCreditsByUserId(final Integer userId) {
        var user = userClient.getById(userId);
        return creditRepository.findByUserId(user.getId());
    }

    public List<Credit> getFilteredCreditsByUserId(final Integer userId,
                                                   final Boolean status,
                                                   final String sortingOrder) {
        var user = userClient.getById(userId);

        // Pfff.. Sometimes I really miss Scala
        // This is arguably the lesser of two evils, you can introduce an enum as well and switch based on that,
        // although my package size is already getting too telescopic so here we are
        if (status) {
            if (sortingOrder.equals("DESC")) return creditRepository
                    .findByUserIdAndStatusTrueOrderByCreatedAtDesc(user.getId());
            else return creditRepository.findByUserIdAndStatusTrueOrderByCreatedAtAsc(user.getId());
        } else {
            if (sortingOrder.equals("DESC"))
                return creditRepository.findByUserIdAndStatusFalseOrderByCreatedAtDesc(user.getId());
            else
                return creditRepository.findByUserIdAndStatusFalseOrderByCreatedAtAsc(user.getId());
        }
    }

    @Transactional
    public Credit createCredit(final CreditRequest creditRequest) {
        var user = userClient.getById(creditRequest.getUserId());

        Credit credit = new Credit();
        credit.setUserId(user.getId());
        credit.setAmount(creditRequest.getAmount());
        credit.setStatus(true);
        credit.setInstallments(processIntermediaryInstallments(creditRequest, credit));

        return creditRepository.save(credit);
    }

    private List<Installment> processIntermediaryInstallments(final CreditRequest creditRequest, final Credit credit) {
        return IntStream.range(1, creditRequest.getInstallmentCount() + 1)
                .mapToObj(i -> Installment.builder()
                        .amount(calculateMonthlyPayment(creditRequest))
                        .credit(credit)
                        .status(true)
                        .paymentType(PaymentType.NOT_PAID)
                        .paymentDate(DateUtils.calculateNextThirtyDays(LocalDateTime.now(), i))
                        .build())
                .toList();
    }

    private BigDecimal calculateMonthlyPayment(final CreditRequest creditRequest) {
        return creditRequest
                .getAmount()
                .divide(BigDecimal.valueOf(creditRequest.getInstallmentCount()), RoundingMode.HALF_UP);
    }
}
