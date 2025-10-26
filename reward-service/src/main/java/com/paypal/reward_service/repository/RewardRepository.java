package com.paypal.reward_service.repository;

import com.paypal.reward_service.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findByUserId(Long userId);
    Boolean existsByTransactionId(Long transactionId);
}
