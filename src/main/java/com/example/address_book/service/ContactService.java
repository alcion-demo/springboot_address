package com.example.address_book.service;

import com.example.address_book.Contact;
import com.example.address_book.repository.ContactRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    public Page<Contact> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable);
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

    public Contact findById(Long id) {
        return contactRepository.findById(id).orElseThrow();
    }

    public void deleteAll(List<Long> ids) {
        contactRepository.deleteAllById(ids);
    }
}
