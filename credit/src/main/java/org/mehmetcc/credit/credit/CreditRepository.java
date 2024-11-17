package org.mehmetcc.credit.credit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Integer> {
    List<Credit> findByUserId(Integer userId);

    List<Credit> findByUserIdAndStatus(Integer userId, Boolean status);

    List<Credit> findByUserIdAndStatusTrueOrderByCreatedAtAsc(Integer userId);

    List<Credit> findByUserIdAndStatusTrueOrderByCreatedAtDesc(Integer userId);

    List<Credit> findByUserIdAndStatusFalseOrderByCreatedAtAsc(Integer userId);

    List<Credit> findByUserIdAndStatusFalseOrderByCreatedAtDesc(Integer userId);
}
