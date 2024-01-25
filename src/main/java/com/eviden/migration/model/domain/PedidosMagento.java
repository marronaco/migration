package com.eviden.migration.model.domain;

import javax.persistence.*;
import java.io.Serializable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "pedidosmagento")
public class PedidosMagento implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pedidosmagento_sequence")
    @SequenceGenerator(name="pedidosmagento_sequence", sequenceName="pedidosmagento_sequence", allocationSize=100)
    private Long id;
    
}