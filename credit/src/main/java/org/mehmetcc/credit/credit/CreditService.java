package org.mehmetcc.credit.credit;

import org.mehmetcc.credit.commons.date.DateUtils;
import org.mehmetcc.credit.commons.user.UserClient;
import org.mehmetcc.credit.installment.Installment;
import org.mehmetcc.credit.installment.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class CreditService {
    private final CreditJpaRepository creditJpaRepository;
    private final UserClient userClient;

    @Autowired
    public CreditService(final CreditJpaRepository creditJpaRepository, final UserClient userClient) {
        this.creditJpaRepository = creditJpaRepository;
        this.userClient = userClient;
    }

    public Page<Credit> getCreditsByUserId(final Integer userId, final Pageable pageable) {
        var user = userClient.getByUserId(userId);
        return creditJpaRepository.findByUserId(user.getId(), pageable);
    }

    public Page<Credit> getFilteredCreditsByUserId(final Integer userId,
                                                   final Boolean status,
                                                   final String sortingOrder,
                                                   final Pageable pageable) {
        var user = userClient.getByUserId(userId);

        // Pfff.. Sometimes I really miss Scala
        // This is arguably the lesser of two evils, you can introduce an enum as well and switch based on that,
        // although my package size is already getting too telescopic so here we are
        if (status) {
            if (sortingOrder.equals("DESC"))
                return creditJpaRepository.findByUserIdAndStatusTrueOrderByCreatedAtDesc(user.getId(), pageable);
            else
                return creditJpaRepository.findByUserIdAndStatusTrueOrderByCreatedAtAsc(user.getId(), pageable);
        } else {
            if (sortingOrder.equals("DESC"))
                return creditJpaRepository.findByUserIdAndStatusFalseOrderByCreatedAtDesc(user.getId(), pageable);
            else
                return creditJpaRepository.findByUserIdAndStatusFalseOrderByCreatedAtAsc(user.getId(), pageable);
        }
    }

    @Transactional
    public Credit createCredit(final CreditRequest creditRequest) {
        var user = userClient.getByUserId(creditRequest.getUserId());

        Credit credit = new Credit();
        credit.setUserId(user.getId());
        credit.setAmount(creditRequest.getAmount());
        credit.setStatus(true);
        credit.setInstallments(processIntermediaryInstallments(creditRequest, credit));

        return creditJpaRepository.save(credit);
    }

    private List<Installment> processIntermediaryInstallments(final CreditRequest creditRequest, final Credit credit) {
        return IntStream.range(1, creditRequest.getInstallmentCount() + 1)
                .mapToObj(i -> Installment.builder()
                        .amount(calculateMonthlyPayment(creditRequest))
                        .credit(credit).status(true)
                        .paymentType(PaymentType.NOT_PAID)
                        .paymentDate(DateUtils.calculateNextThirtyDays(LocalDateTime.now(), i))
                        .build())
                .toList();
    }

    private BigDecimal calculateMonthlyPayment(final CreditRequest creditRequest) {
        return creditRequest.getAmount()
                .divide(BigDecimal.valueOf(creditRequest.getInstallmentCount()), RoundingMode.HALF_UP);
    }
}
