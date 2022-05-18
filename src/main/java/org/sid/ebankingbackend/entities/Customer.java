package org.sid.ebankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Entity //entité jpa mapping objet relationnel//
@Data
@NoArgsConstructor @AllArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "customer") //il s'agit de la même relation que celle dans la class bankAccount
    // bankAccount doit être une entité//
    //mappedBy : pour générer une clé etrangere//
    private List<BankAccount> bankAccounts;
    //client peut avoir plusieurs comptes//

}
