package com.finance.wallet_mysql_auth_valid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.finance.wallet_mysql_auth_valid.model.Wallet;
import com.finance.wallet_mysql_auth_valid.service.WalletService;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet wallet) {
        Wallet created = walletService.createWallet(wallet);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/bulk")
    public ResponseEntity<java.util.List<Wallet>> createWalletsBulk(@RequestBody java.util.List<Wallet> wallets) {
        java.util.List<Wallet> createdWallets = new java.util.ArrayList<>();
        for (Wallet wallet : wallets) {
            createdWallets.add(walletService.createWallet(wallet));
        }
        return ResponseEntity.ok(createdWallets);
    }

    @GetMapping
    public ResponseEntity<Page<Wallet>> getAllWallets(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Page<Wallet> wallets = walletService.getAllWallets(page, size);
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable Long id) {
        Wallet wallet = walletService.getWalletById(id);
        if (wallet != null) {
            return ResponseEntity.ok(wallet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wallet> updateWallet(@PathVariable Long id, @RequestBody Wallet walletDetails) {
        Wallet updated = walletService.getWalletById(id);
        if (updated != null) {
            updated.setOwnerName(walletDetails.getOwnerName());
            updated.setBalance(walletDetails.getBalance());
            updated.setCurrency(walletDetails.getCurrency());
            walletService.updateWallet(updated);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
        Wallet wallet = walletService.getWalletById(id);
        if (wallet != null) {
            walletService.deleteWallet(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
