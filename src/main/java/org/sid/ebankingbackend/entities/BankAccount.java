package org.sid.ebankingbackend.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankingbackend.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity //utilise la strategie single table du moment qu'elle est abstraite elle sait qu'elle
 // créer des tables que pr les classes concretes//
//Jpa, tt les classes qui vont heriter de cette classes
// on les mets dans une même table//
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//comment on veut appeler la colonne type qui fait la descrimination//

@DiscriminatorColumn(name = "TYPE",length = 4, discriminatorType=DiscriminatorType.STRING)
@Data @NoArgsConstructor @AllArgsConstructor
//il crée que les classes concretes//
public abstract class BankAccount {
    @Id
    private String id;//rib, c pas la bdd qui va le generer//
    private double balance;//solde//
    private Date createdAt;//date de creation//
    @Enumerated(EnumType.STRING)//enum dans la bdd en format string
    private AccountStatus status;//compte appartient à client "enum"//
    @ManyToOne //client peut avoir plusieurs compte mais 1compte concerne 1seul client//
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount",fetch=FetchType.EAGER)
    private List<AccountOperation> accountOperations;
}
