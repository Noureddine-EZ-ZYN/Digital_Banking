package net.noureddine.ebankingbackend.service;

import net.noureddine.ebankingbackend.dao.AccountOperationRepository;
import net.noureddine.ebankingbackend.dao.BankAccountRepository;
import net.noureddine.ebankingbackend.dao.CustomerRepository;
import net.noureddine.ebankingbackend.dtos.CustomerDTO;
import net.noureddine.ebankingbackend.entities.*;
import net.noureddine.ebankingbackend.enums.AccountStatus;
import net.noureddine.ebankingbackend.exceptions.BalanceNoSufficientException;
import net.noureddine.ebankingbackend.exceptions.BankAccountNotFoundException;
import net.noureddine.ebankingbackend.exceptions.CustomerNotFoundException;
import net.noureddine.ebankingbackend.mappers.BankAccountMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountOperationRepository accountOperationRepository;

    @Mock
    private BankAccountMapperImpl dtoMapper;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    // ─── Test 1 : Sauvegarder un client ───────────────────────────────────────

    @Test
    void saveCustomer_shouldReturnSavedCustomer() {
        CustomerDTO inputDTO = new CustomerDTO();
        inputDTO.setName("Ahmed");
        inputDTO.setEmail("ahmed@gmail.com");

        Customer entity = new Customer();
        entity.setName("Ahmed");
        entity.setEmail("ahmed@gmail.com");

        Customer saved = new Customer();
        saved.setId(1L);
        saved.setName("Ahmed");
        saved.setEmail("ahmed@gmail.com");

        CustomerDTO expectedDTO = new CustomerDTO();
        expectedDTO.setId(1L);
        expectedDTO.setName("Ahmed");

        when(dtoMapper.fromCustomerDTO(inputDTO)).thenReturn(entity);
        when(customerRepository.save(entity)).thenReturn(saved);
        when(dtoMapper.fromCustomer(saved)).thenReturn(expectedDTO);

        CustomerDTO result = bankAccountService.saveCustomer(inputDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(customerRepository, times(1)).save(entity);
    }

    // ─── Test 2 : Client introuvable ──────────────────────────────────────────

    @Test
    void getCustomer_shouldThrow_whenNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bankAccountService.getCustomer(99L))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    // ─── Test 3 : Débit normal ────────────────────────────────────────────────

    @Test
    void debit_shouldDecreaseBalance() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);

        CurrentAccount account = new CurrentAccount();
        account.setId("acc1");
        account.setBalance(1000);
        account.setOverDraft(0);
        account.setStatus(AccountStatus.ACTIVATED);
        account.setCustomer(customer);
        account.setAccountOperations(new ArrayList<>());

        when(bankAccountRepository.findById("acc1")).thenReturn(Optional.of(account));

        bankAccountService.debit("acc1", 400, "Achat");

        assertThat(account.getBalance()).isEqualTo(600);
        verify(bankAccountRepository).save(account);
    }

    // ─── Test 4 : Débit refusé si solde insuffisant ───────────────────────────

    @Test
    void debit_shouldThrow_whenBalanceInsufficient() {
        Customer customer = new Customer();
        customer.setId(1L);

        CurrentAccount account = new CurrentAccount();
        account.setId("acc1");
        account.setBalance(100);
        account.setOverDraft(0);
        account.setStatus(AccountStatus.ACTIVATED);
        account.setCustomer(customer);
        account.setAccountOperations(new ArrayList<>());

        when(bankAccountRepository.findById("acc1")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> bankAccountService.debit("acc1", 500, "test"))
                .isInstanceOf(BalanceNoSufficientException.class);

        verify(bankAccountRepository, never()).save(any());
    }

    // ─── Test 5 : Crédit normal ───────────────────────────────────────────────

    @Test
    void credit_shouldIncreaseBalance() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);

        CurrentAccount account = new CurrentAccount();
        account.setId("acc1");
        account.setBalance(500);
        account.setStatus(AccountStatus.ACTIVATED);
        account.setCustomer(customer);
        account.setAccountOperations(new ArrayList<>());

        when(bankAccountRepository.findById("acc1")).thenReturn(Optional.of(account));

        bankAccountService.credit("acc1", 300, "Salaire");

        assertThat(account.getBalance()).isEqualTo(800);
        verify(bankAccountRepository).save(account);
    }

    // ─── Test 6 : Compte introuvable au débit ─────────────────────────────────

    @Test
    void debit_shouldThrow_whenAccountNotFound() {
        when(bankAccountRepository.findById("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bankAccountService.debit("unknown", 100, "test"))
                .isInstanceOf(BankAccountNotFoundException.class);
    }

    // ─── Test 7 : Virement entre deux comptes ─────────────────────────────────

    @Test
    void transfer_shouldDebitSourceAndCreditDestination() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);

        CurrentAccount source = new CurrentAccount();
        source.setId("src");
        source.setBalance(1000);
        source.setOverDraft(0);
        source.setStatus(AccountStatus.ACTIVATED);
        source.setCustomer(customer);
        source.setAccountOperations(new ArrayList<>());

        CurrentAccount dest = new CurrentAccount();
        dest.setId("dst");
        dest.setBalance(200);
        dest.setOverDraft(0);
        dest.setStatus(AccountStatus.ACTIVATED);
        dest.setCustomer(customer);
        dest.setAccountOperations(new ArrayList<>());

        when(bankAccountRepository.findById("src")).thenReturn(Optional.of(source));
        when(bankAccountRepository.findById("dst")).thenReturn(Optional.of(dest));

        bankAccountService.transfer("src", "dst", 500);

        assertThat(source.getBalance()).isEqualTo(500);
        assertThat(dest.getBalance()).isEqualTo(700);
    }

    // ─── Test 8 : Supprimer un client ─────────────────────────────────────────

    @Test
    void deleteCustomer_shouldCallDeleteById() {
        bankAccountService.deleteCustomer(1L);

        verify(customerRepository, times(1)).deleteById(1L);
    }
}