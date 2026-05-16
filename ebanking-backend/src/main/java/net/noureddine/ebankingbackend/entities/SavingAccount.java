package net.noureddine.ebankingbackend.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("SA")
public class SavingAccount extends BankAccount {
    private double interestRate;
}
