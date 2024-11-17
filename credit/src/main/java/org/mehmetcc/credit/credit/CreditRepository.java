package org.mehmetcc.credit.credit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Integer> {
    Page<Credit> findByUserId(Integer userId, Pageable pageable);

    Page<Credit> findByUserIdAndStatusTrueOrderByCreatedAtAsc(Integer userId, Pageable pageable);

    Page<Credit> findByUserIdAndStatusTrueOrderByCreatedAtDesc(Integer userId, Pageable pageable);

    Page<Credit> findByUserIdAndStatusFalseOrderByCreatedAtAsc(Integer userId, Pageable pageable);

    Page<Credit> findByUserIdAndStatusFalseOrderByCreatedAtDesc(Integer userId, Pageable pageable);
}
