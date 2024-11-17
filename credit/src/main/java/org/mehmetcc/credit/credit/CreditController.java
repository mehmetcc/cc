package org.mehmetcc.credit.credit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/credits")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping
    public ResponseEntity<CreditResponse> createCredit(@RequestBody CreditRequest creditRequest) {
        var credit = creditService.createCredit(creditRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(CreditResponse.fromCredit(credit));
    }
}
