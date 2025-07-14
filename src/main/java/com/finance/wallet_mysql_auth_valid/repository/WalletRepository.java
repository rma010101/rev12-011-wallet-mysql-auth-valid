package com.finance.wallet_mysql_auth_valid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.wallet_mysql_auth_valid.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    // Custom query methods can be defined here if needed


}
