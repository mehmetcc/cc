package org.mehmetcc.credit.installment;

import org.mehmetcc.credit.credit.Credit;
import org.mehmetcc.credit.credit.CreditRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstallmentService {
    private final InstallmentRepository installmentRepository;

    private final CreditRepository creditRepository;

    public InstallmentService(final InstallmentRepository installmentRepository,
                              final CreditRepository creditRepository) {
        this.installmentRepository = installmentRepository;
        this.creditRepository = creditRepository;
    }

    @Transactional
    public InstallmentPaymentResponse makePayment(final InstallmentPaymentRequest installmentPaymentRequest) {
        var installment = installmentRepository.getReferenceById(installmentPaymentRequest.getId());
        var credit = installment.getCredit();

        if (installment.getAmount().compareTo(installmentPaymentRequest.getAmount()) == -1) {
            throw new InstallmentPaymentOutOfBoundsException("Installment payment is out of bounds");
        } else if (installment.getAmount().compareTo(installmentPaymentRequest.getAmount()) == 0) {
            installment.setPaymentType(PaymentType.PAID);
            installment.setStatus(false);
        } else if (installment.getAmount().compareTo(installmentPaymentRequest.getAmount()) == 1) {
            var newAmount = installment.getAmount().subtract(installmentPaymentRequest.getAmount());
            installment.setAmount(newAmount);
            installment.setPaymentType(PaymentType.PARTIALLY_PAID);
        }

        var persisted = installmentRepository.save(installment);
        persistFulfilledCredit(persisted, credit);
        return new InstallmentPaymentResponse(persisted.getId(), persisted.getPaymentType());
    }

    private void persistFulfilledCredit(final Installment installment, final Credit credit) {
        credit.getInstallments().replaceAll(item -> item.getId().equals(installment.getId()) ? installment : item);
        var check = true;

        for_loop:
        for (Installment currentInstallment : credit.getInstallments()) {
            if (currentInstallment.getPaymentType() != PaymentType.PAID) {
                check = false;
                break for_loop;
            }
        }

        if (check) {
            credit.setInstallments(credit.getInstallments());
            credit.setStatus(false);
            creditRepository.save(credit);
        }
    }
}
