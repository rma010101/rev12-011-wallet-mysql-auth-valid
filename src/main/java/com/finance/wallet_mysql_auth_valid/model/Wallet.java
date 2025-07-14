package com.finance.wallet_mysql_auth_valid.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Owner name cannot be blank")
    @Column(name = "owner_name")
    @NotBlank(message = "Owner name must not be blank")
    private String ownerName;

    @Min(value=0, message= "Balance must be at least 0")
    @NotNull(message = "Balance cannot be null")
    @Column(nullable = false)
    private Double balance;

    @NotNull(message = "Currency cannot be null")
    @Size(min = 3, max = 3, message = "Currency must be exactly 3 characters long") 
    @Column(length = 3)
    private String currency;

    public Wallet() {}

    public Wallet(String ownerName, Double balance) {
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public Wallet(String ownerName, Double balance, String currency) {
        this.ownerName = ownerName;
        this.balance = balance;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
