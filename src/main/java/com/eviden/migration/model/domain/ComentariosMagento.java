package com.eviden.migration.model.domain;

import javax.persistence.*;
import java.io.Serializable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "comentariosmagento")
public class ComentariosMagento implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="comentariosmagento_sequence")
    @SequenceGenerator(name="comentariosmagento_sequence", sequenceName="comentariosmagento_sequence", allocationSize=100)
    private Long id;
    
}