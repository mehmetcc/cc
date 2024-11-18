package org.mehmetcc.credit.installment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Component
public class OverduePaymentCalculationJob {
    @Autowired
    private OverduePaymentCounter counter;

    @Autowired
    InstallmentJpaRepository repository;

    private static final BigDecimal INTEREST_RATE = BigDecimal.valueOf(0.2);

    @Scheduled(cron = "0 55 23 * * ?")
    public void execute() {
        List<Installment> calculatedOverdueInstallments = repository
                .findAll()
                .stream()
                .filter(installment -> installment.getPaymentDate().toLocalDate().isEqual(LocalDate.now()))
                .filter(Installment::getStatus)
                .peek(installment -> {
                    var count = counter.increment(installment.getId());
                    // (Gecikmeye Düşen Gün Sayısı * (Faiz Oranı / 100 ) * Tutar ) / 360
                    var interest = INTEREST_RATE.multiply(BigDecimal.valueOf(count))
                            .multiply(installment.getAmount())
                            .divide(BigDecimal.valueOf(360), RoundingMode.HALF_UP);
                    installment.setAmount(interest);
                })
                .toList();

        repository.saveAll(calculatedOverdueInstallments);
    }
}
