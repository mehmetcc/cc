package org.mehmetcc.credit.credit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mehmetcc.credit.commons.user.User;
import org.mehmetcc.credit.commons.user.UserClient;
import org.mehmetcc.credit.installment.Installment;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CreditServiceTest {
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

        var installments = Stream
                .of(BigDecimal.valueOf(3.0), BigDecimal.valueOf(3.0), BigDecimal.valueOf(3.0), BigDecimal.valueOf(3.0))
                .map(value -> Installment.builder()
                        .id(1)
                        .amount(value)
                        .credit(credit)
                        .status(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
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
        when(client.getById(1)).thenReturn(user);
        when(repository.save(any())).thenReturn(credit);

        // Interaction
        var output = service.createCredit(request);

        // Verification
        verify(client).getById(any());
        verify(repository).save(any());

        // Assertions
        assertThat(output.getInstallments())
                .hasSize(4)
                .allSatisfy(item -> assertThat(item.getAmount()).isEqualTo(BigDecimal.valueOf(3.0)));
        assertThat(output.getAmount()).isEqualTo(BigDecimal.valueOf(12.0));
    }
}