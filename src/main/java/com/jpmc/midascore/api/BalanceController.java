// src/main/java/com/jpmc/midascore/api/BalanceController.java
package com.jpmc.midascore.api;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.foundation.Balance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class BalanceController {

    private final UserRepository userRepository;

    public BalanceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam("userId") long userId) {
        // getBalance() returns Float (or float) in your entity
        Float amount = userRepository.findById(userId)
                .map(UserRecord::getBalance)   // Optional<Float>
                .filter(Objects::nonNull)
                .orElse(0f);                   // ✅ default must be Float, not BigDecimal

        return new Balance(amount);            // Balance takes a Float
    }
}