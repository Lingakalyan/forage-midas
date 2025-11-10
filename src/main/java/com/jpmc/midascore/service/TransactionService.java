package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
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
        long senderId = tx.getSenderId();       // long from DTO
        long recipientId = tx.getRecipientId(); // long from DTO

        Optional<UserRecord> senderOpt = userRepository.findById(senderId);
        Optional<UserRecord> recipientOpt = userRepository.findById(recipientId);
        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) return;

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        BigDecimal amount = BigDecimal.valueOf(tx.getAmount());             // adapt if BigDecimal in DTO
        BigDecimal senderBal = BigDecimal.valueOf(sender.getBalance());     // balance field is float/double in scaffold
        if (senderBal.compareTo(amount) < 0) return;                         // insufficient funds

        BigDecimal recipientBal = BigDecimal.valueOf(recipient.getBalance());

        sender.setBalance(senderBal.subtract(amount).floatValue());
        recipient.setBalance(recipientBal.add(amount).floatValue());

        userRepository.save(sender);
        userRepository.save(recipient);

        txRepo.save(new TransactionRecord(amount, sender, recipient));
    }
}