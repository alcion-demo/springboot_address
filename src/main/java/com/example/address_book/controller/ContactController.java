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


@RestController
public class ContactController {
    private final ContactService contactService;

    /**
     * コンストラクタ
     * @param contactService
     */
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    public List<Contact> findAll() {
        return contactService.findAll();
    }

    @PostMapping("/contacts")
    public Contact create(@RequestBody Contact contact) {
        return contactService.create(contact);
    }

    @PutMapping("/contacts/{id}")
    public Contact update(
            @PathVariable Long id,
            @RequestBody Contact contact) {

        return contactService.update(id, contact);
    }

    @DeleteMapping("/contacts/{id}")
    public void delete(@PathVariable Long id) {
        contactService.delete(id);
    }

}
