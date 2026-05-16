package net.noureddine.ebankingbackend.service;

import net.noureddine.ebankingbackend.dtos.*;
import net.noureddine.ebankingbackend.exceptions.BalanceNoSufficientException;
import net.noureddine.ebankingbackend.exceptions.BankAccountNotFoundException;

import net.noureddine.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount (double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount (double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomer();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId,double amount, String description) throws BalanceNoSufficientException,BankAccountNotFoundException;
    void credit(String accountId,double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource,String accountIdDescription,double amount) throws BankAccountNotFoundException, BalanceNoSufficientException;
    List<BankAccountDTO> bankAccountList();
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    List<AccountOperationDTO> accountHistory(String accountId);
    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
    List<CustomerDTO> searchCustomers(String keyword);
}
