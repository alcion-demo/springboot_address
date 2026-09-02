package com.example.address_book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity //DBのEntityとして扱う

/**
 * 住所録の連絡先を表すクラス(DB本体)
 */
public class Contact {

    @Id //主キー
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自動でid振り分け
    private Long id;

    private String name;

    private String phone;

    private String email;

    private String address;

    /**
     * Getter
     * @return
     */
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    /**
     * Setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}