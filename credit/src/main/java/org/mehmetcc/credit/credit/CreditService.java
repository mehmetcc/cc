package org.mehmetcc.credit.credit;

import org.springframework.stereotype.Service;

@Service
public class CreditService {
    private final CreditRepository repository;

    protected CreditService(final CreditRepository repository) {
        this.repository = repository;
    }

    protected void create() {
    }
}