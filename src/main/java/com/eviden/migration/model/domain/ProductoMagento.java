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
@Table(name = "productomagento")
public class ProductoMagento implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="productomagento_sequence")
    @SequenceGenerator(name="productomagento_sequence", sequenceName="productomagento_sequence", allocationSize=100)
    private Long id;
    
}