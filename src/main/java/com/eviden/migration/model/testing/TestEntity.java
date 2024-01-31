package com.eviden.migration.model.testing;

import javax.persistence.*;
import java.io.Serializable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
public class TestEntity implements Serializable {
    
    String factouu;
    int textLength;
    
}