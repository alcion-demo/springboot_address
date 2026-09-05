package com.example.address_book.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.address_book.Contact;
import com.example.address_book.service.ContactService;

import jakarta.validation.Valid;

import com.example.address_book.User;
import com.example.address_book.repository.UserRepository;

import org.springframework.security.core.Authentication;

@RestController
public class ContactController {
    private final ContactService contactService;
    private final UserRepository userRepository;

    /**
     * コンストラクタ
     *
     * @param contactService
     * @param userRepository
     */
    public ContactController(
            ContactService contactService,
            UserRepository userRepository) {

        this.contactService = contactService;
        this.userRepository = userRepository;
    }

    @GetMapping("/contacts")
    public List<Contact> findAll() {
        return contactService.findAll();
    }

    @PostMapping("/contacts")
    public Contact create(@Valid @RequestBody Contact contact) {
        return contactService.create(contact);
    }

    @PutMapping("/contacts/{id}")
    public Contact update(
            @PathVariable Long id,
            @Valid @RequestBody Contact contact,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        return contactService.update(id, contact, user);
    }

    @DeleteMapping("/contacts/{id}")
    public void delete(@PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        contactService.delete(id, user);
    }

}
