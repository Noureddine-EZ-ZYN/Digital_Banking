package net.noureddine.ebankingbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("CA")
public class CurrentAccount extends BankAccount {
private double overDraft;
}
