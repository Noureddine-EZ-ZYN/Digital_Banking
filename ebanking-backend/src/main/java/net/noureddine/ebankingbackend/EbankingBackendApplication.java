package net.noureddine.ebankingbackend;
import net.noureddine.ebankingbackend.dtos.BankAccountDTO;
import net.noureddine.ebankingbackend.dtos.CurrentBankAccountDTO;
import net.noureddine.ebankingbackend.dtos.CustomerDTO;
import net.noureddine.ebankingbackend.dtos.SavingBankAccountDTO;
import net.noureddine.ebankingbackend.exceptions.CustomerNotFoundException;
import net.noureddine.ebankingbackend.service.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class EbankingBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
        @Bean
        CommandLineRunner start(BankAccountService bankAccountService){
            return args ->{
                Stream.of("Hassan","Yassine","Aicha").forEach(name ->{
                    CustomerDTO customer =new CustomerDTO();
                    customer.setName(name);
                    customer.setEmail(name + "@gmail.com");
                    bankAccountService.saveCustomer(customer);
            });
                bankAccountService.listCustomer().forEach(customer ->{
                    try {
                        bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
                        bankAccountService.saveSavingBankAccount(Math.random()*1200000,5.5,customer.getId());

                    } catch (CustomerNotFoundException e) {
                       e.printStackTrace();
                    }
                });

                List<BankAccountDTO> bankAccountsDTOS=bankAccountService.bankAccountList();
                for(BankAccountDTO bankAccount:bankAccountsDTOS){
                    for (int i=0 ;i< 10;i++){
                        String accountId;
                        if(bankAccount instanceof SavingBankAccountDTO){
                            accountId=((SavingBankAccountDTO) bankAccount).getId();
                        }
                        else {
                            accountId=((CurrentBankAccountDTO) bankAccount).getId();
                        }
                        bankAccountService.credit(accountId,30*Math.random()*140,"Credit");
                        bankAccountService.debit(accountId,5*Math.random()*12,"Debit");
                    }
                }

            };
    }

}

