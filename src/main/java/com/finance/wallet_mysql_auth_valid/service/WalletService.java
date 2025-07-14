package com.finance.wallet_mysql_auth_valid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.finance.wallet_mysql_auth_valid.model.Wallet;
import com.finance.wallet_mysql_auth_valid.repository.WalletRepository;

@Service
public class WalletService {
    
    @Autowired
    private WalletRepository walletRepository;

    // Additional methods for wallet operations can be added here
    public Wallet createWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public Page<Wallet> getAllWallets(int page, int size) {
        Pageable pageableobj = PageRequest.of(page, size, Sort.by("id").descending());
        return walletRepository.findAll(pageableobj);
    }
    public Wallet getWalletById(Long id) {
        return walletRepository.findById(id).orElse(null);
    }
    public void updateWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }   
    public void deleteWallet(Long id) {
        walletRepository.deleteById(id);
    }   

}
