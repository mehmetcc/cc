package org.mehmetcc.credit.credit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mehmetcc.credit.commons.date.DateUtils;
import org.mehmetcc.credit.commons.user.User;
import org.mehmetcc.credit.commons.user.UserClient;
import org.mehmetcc.credit.installment.Installment;
import org.mehmetcc.credit.installment.PaymentType;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CreditServiceTest {
    // TODO this is embarrassingly bad. Fix in the future
    @Mock
    private UserClient client;

    @Mock
    private CreditRepository repository;

    @InjectMocks
    private CreditService service;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }


    private User getValidUser() {
        return User.builder()
                .id(1)
                .firstName("String")
                .lastName("Stringoğlu")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();
    }

    private Credit getValidCredit() {
        var credit = Credit.builder()
                .id(1)
                .status(true)
                .amount(BigDecimal.valueOf(12.0))
                .userId(1)
                .build();

        var installments = IntStream.range(1, 5)
                .mapToObj(i -> Installment.builder()
                        .amount(BigDecimal.valueOf(3.0))
                        .credit(credit)
                        .status(true)
                        .paymentType(PaymentType.NOT_PAID)
                        .paymentDate(DateUtils.calculateNextThirtyDays(LocalDateTime.now(), i))
                        .build())
                .toList();

        credit.setInstallments(installments);
        return credit;
    }

    @Test
    void validCreditRequestGiven_shouldDivideUpInstallmentsAndCreateCredit() {
        // Data Prep.
        var user = getValidUser();
        var request = new CreditRequest(1, BigDecimal.valueOf(12.0), 4);
        var credit = getValidCredit();

        // Stubbing
        when(client.getByUserId(1)).thenReturn(user);
        when(repository.save(any())).thenReturn(credit);

        // Interaction
        var output = service.createCredit(request);

        // Verification
        verify(client).getByUserId(any());
        verify(repository).save(any());

        // Assertions
        assertThat(output.getInstallments())
                .hasSize(4)
                .allSatisfy(item -> assertThat(item.getAmount()).isEqualTo(BigDecimal.valueOf(3.0)))
                .allSatisfy(item -> assertThat(item.getPaymentDate().getDayOfWeek()).isNotEqualTo(SATURDAY))
                .allSatisfy(item -> assertThat(item.getPaymentDate().getDayOfWeek()).isNotEqualTo(SUNDAY));
        assertThat(output.getAmount()).isEqualTo(BigDecimal.valueOf(12.0));
    }
}