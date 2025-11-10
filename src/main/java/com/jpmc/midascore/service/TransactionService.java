package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.foundation.Transaction; // <-- the DTO from the scaffold
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository txRepo;

    public TransactionService(UserRepository userRepository,
                              TransactionRecordRepository txRepo) {
        this.userRepository = userRepository;
        this.txRepo = txRepo;
    }

    @Transactional
    public void processIncoming(Transaction tx) {
        // Extract sender/recipient “ids” from the DTO. If your DTO has getSenderId()/getRecipientId()
        // that are names, use findByName. If they’re numeric, parse and use findById.
        String senderKey = tx.getSenderId();
        String recipientKey = tx.getRecipientId();

        Optional<UserRecord> senderOpt = userRepository.findByName(senderKey);
        Optional<UserRecord> recipientOpt = userRepository.findByName(recipientKey);

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            // invalid IDs -> discard
            return;
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        // Work in BigDecimal for correctness; UserRecord may have float/double balance
        BigDecimal amount = toAmount(tx.getAmount());

        BigDecimal senderBal = toAmount(sender.getBalance());
        if (senderBal.compareTo(amount) < 0) {
            // insufficient funds -> discard
            return;
        }

        // apply
        BigDecimal newSender = senderBal.subtract(amount);
        BigDecimal recipientBal = toAmount(recipient.getBalance()).add(amount);

        // Save back as the original numeric type
        sender.setBalance(newSender.floatValue());
        recipient.setBalance(recipientBal.floatValue());

        // persist entities
        userRepository.save(sender);
        userRepository.save(recipient);

        // record the transaction
        txRepo.save(new TransactionRecord(amount, sender, recipient));
    }

    private static BigDecimal toAmount(double v) {
        return BigDecimal.valueOf(v);
    }
    private static BigDecimal toAmount(float v) {
        return BigDecimal.valueOf(Double.valueOf(String.valueOf(v)));
    }
    private static BigDecimal toAmount(Number n) {
        return BigDecimal.valueOf(n.doubleValue());
    }
}
