package com.example.local_project.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "institutions")
@Data
@Getter
@Setter
public class Institutions {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="Institution_id")
    private Long institutionId;
    @Column(name="Institution_name")
    private String institutionName;
    
}
