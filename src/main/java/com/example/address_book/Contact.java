package com.example.address_book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Entity //DBのEntityとして扱う

/**
 * 住所録の連絡先を表すクラス(DB本体)
 */
public class Contact {

    @Id //主キー
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自動でid振り分け
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String phone;

    private String email;

    private String postalCode;

    private String address;

    /**
     * Getter
     * @return
     */
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    @NotBlank
    @Size(max = 20)
    public String getName() {
        return name;
    }

    @NotBlank
    @Size(max = 20)
    @Pattern(
        regexp = "^[0-9-]+$",
        message = "電話番号は数字とハイフンで入力してください"
    )
    public String getPhone() {
        return phone;
    }

    @NotBlank
    @Email
    public String getEmail() {
        return email;
    }

    @NotBlank
    @Pattern(
        regexp = "^[0-9]{7}$",
        message = "郵便番号は7桁の数字で入力してください"
    )
    public String getPostalCode() {
        return postalCode;
    }

    @NotBlank
    @Size(max = 50)
    public String getAddress() {
        return address;
    }

    /**
     * Setter
     * @param name
     */
    public void setUser(User user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}