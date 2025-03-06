package org.acme.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name= "icon")
public class Icon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String iconName;

    public Icon(){}

    public Icon(String iconName){
        if(iconName == null || iconName.trim().isEmpty()){
            throw new IllegalArgumentException("Icon name can not be null or empty");
        }
        this.iconName = iconName;
    }
}

