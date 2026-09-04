package com.example.address_book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {
    @Id //主キー
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自動でid振り分け
    private Long id;

    private String name;

    private String email;

    private String password;

    /**
     * Getter
     * @return
     */
    public Long getId() {
        return id;
    }

    @NotBlank
    @Size(max = 20)
    public String getName() {
        return name;
    }

    @NotBlank
    @Email
    public String getEmail() {
        return email;
    }

    @NotBlank
    @Size(min = 8, max = 100)
    public String getPassword() {
        return password;
    }

    /**
     * Setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
