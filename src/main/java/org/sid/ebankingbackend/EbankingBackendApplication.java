package org.sid.ebankingbackend;

import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.AccountStatus;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.repos.AccountOperationRepository;
import org.sid.ebankingbackend.repos.BankAccountRepository;
import org.sid.ebankingbackend.repos.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);

    }

    //executer automatiquement au démarrage//
    @Bean
    //@Transactional //tte les operations se déroulent au sein de la même transaction//
    CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository){
        return args-> {
            //consulter compte//
            BankAccount bankAccount =
                    bankAccountRepository.findById("93834ca2-9885-4104-a848-430c4c26caa8").orElse(null);
            if (bankAccount != null) {
                System.out.println("**************");
                System.out.println(bankAccount.getId());
                System.out.println(bankAccount.getBalance());
                System.out.println(bankAccount.getCreatedAt());
                System.out.println(bankAccount.getCustomer().getName());
                if (bankAccount instanceof CurrentAccount) {
                    System.out.println("Over Draft=>" + ((CurrentAccount) bankAccount).getOverDraft());

                } else if (bankAccount instanceof SavingAccount) {
                    System.out.println("Rate=>" + ((SavingAccount) bankAccount).getInterestRate());
                }
                //afficher les operations//
                bankAccount.getAccountOperations().forEach(op -> {
                    System.out.println(op.getType() + "\t" +
                            (op.getOperationDate()) + "\t" +
                            (op.getAmount()));
                });
            }

        };
    }
    //@Bean on veut pas qu'elle insere les données//
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Maryam", "Imane", "Sara").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                //genere une chaine de caracteres aléatoire qui depend de la date systeme//
                currentAccount.setBalance(Math.random() * 90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setCustomer(cust);//on créer un account pour chaque customer//
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);//on créer un account pour chaque customer//
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });
            //créer operations//
            bankAccountRepository.findAll().forEach(acc -> {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);

                }

            });
        };


    }

}
