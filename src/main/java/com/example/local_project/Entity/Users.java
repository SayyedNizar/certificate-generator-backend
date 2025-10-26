package com.example.local_project.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "users")
@Data
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="email",unique = true)
    private String email;
    @Column(name="password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(name="role")
    private String role;
    
}
