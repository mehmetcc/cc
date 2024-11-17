package org.mehmetcc.credit.credit;

import org.mehmetcc.credit.exception.InvalidQueryParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credits")
public class CreditController {
    private final CreditService creditService;

    public CreditController(final CreditService creditService) {
        this.creditService = creditService;
    }

    @GetMapping("/{userId}/filter")
    public ResponseEntity<List<Credit>> getFilteredCreditsByUserId(
            final @PathVariable Integer userId,
            final @RequestParam(required = false, defaultValue = "true") Boolean status,
            final @RequestParam(required = false, defaultValue = "ASC") String sortingOrder
    ) {
        if (sortingOrder.equals("ASC") || sortingOrder.equals("DESC"))
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(creditService.getFilteredCreditsByUserId(userId, status, sortingOrder));
        else throw new InvalidQueryParameterException("Query parameter can only be ASC or DESC");
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<Credit>> getCreditsByUserId(final @PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(creditService.getCreditsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<CreditResponse> createCredit(final @RequestBody CreditRequest creditRequest) {
        var credit = creditService.createCredit(creditRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(CreditResponse.fromCredit(credit));
    }
}
