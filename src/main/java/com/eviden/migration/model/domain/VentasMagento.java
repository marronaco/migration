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
@Table(name = "ventasmagento")
public class VentasMagento implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ventasmagento_sequence")
    @SequenceGenerator(name="ventasmagento_sequence", sequenceName="ventasmagento_sequence", allocationSize=100)
    private Long id;
    
}