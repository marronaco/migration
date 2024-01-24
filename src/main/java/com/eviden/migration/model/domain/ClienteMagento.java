package com.eviden.migration.model.domain;

import javax.persistence.*;
import java.io.Serializable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "clientemagento")
public class ClienteMagento implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="clientemagento_sequence")
    @SequenceGenerator(name="clientemagento_sequence", sequenceName="clientemagento_sequence", allocationSize=100)
    private Long id;
    
}