package org.mehmetcc.credit.installment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class OverduePaymentCounter {
    @Autowired
    private RedisTemplate<Integer, Integer> template;

    public Integer get(Integer installmentId) {
        return template.opsForValue().get(installmentId);
    }

    public Integer increment(Integer installmentId) {
        var got = template.opsForValue().get(installmentId);
        var counter = 0;

        if (got == null) counter = 1;
        else counter = got + 1;

        return counter;
    }
}
