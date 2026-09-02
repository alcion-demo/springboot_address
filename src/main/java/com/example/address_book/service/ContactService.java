package com.example.address_book.service;

import com.example.address_book.Contact;
import com.example.address_book.repository.ContactRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    /**
     * ContactRepositoryを受け取る
     * @param contactRepository
     */
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public Contact create(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact update(Long id, Contact contact) {
        Contact existing = contactRepository.findById(id).orElseThrow();

        existing.setName(contact.getName());
        existing.setPhone(contact.getPhone());
        existing.setEmail(contact.getEmail());
        existing.setAddress(contact.getAddress());

        return contactRepository.save(existing);
    }

    public void delete(Long id) {
        contactRepository.deleteById(id);
    }

}
