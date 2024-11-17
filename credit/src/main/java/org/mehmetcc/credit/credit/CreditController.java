package org.mehmetcc.credit.credit;

import org.mehmetcc.credit.exception.InvalidQueryParameterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credits")
public class CreditController {
    private final CreditService creditService;

    public CreditController(final CreditService creditService) {
        this.creditService = creditService;
    }

    @GetMapping("/{userId}/filter")
    public ResponseEntity<Page<Credit>> getFilteredCreditsByUserId(
            final @PathVariable Integer userId,
            final @RequestParam(required = false, defaultValue = "true") Boolean status,
            final @RequestParam(required = false, defaultValue = "ASC") String sortingOrder,
            final @RequestParam(defaultValue = "0") int page,
            final @RequestParam(defaultValue = "10") int size

    ) {
        if (!sortingOrder.equalsIgnoreCase("ASC") && !sortingOrder.equalsIgnoreCase("DESC"))
            throw new InvalidQueryParameterException("Query parameter can only be ASC or DESC");

        var pageable = PageRequest.of(page, size);
        var credits = creditService.getFilteredCreditsByUserId(userId, status, sortingOrder, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(credits);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<Credit>> getCreditsByUserId(final @PathVariable Integer userId,
                                                           final @RequestParam(defaultValue = "0") int page,
                                                           final @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(creditService.getCreditsByUserId(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<CreditResponse> createCredit(final @RequestBody CreditRequest creditRequest) {
        var credit = creditService.createCredit(creditRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(CreditResponse.fromCredit(credit));
    }
}
