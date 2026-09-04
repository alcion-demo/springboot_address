package com.example.address_book.service;

import com.example.address_book.Contact;
import com.example.address_book.repository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import com.example.address_book.User;

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

    /**
     * データ取得
     * @return
     */
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    /**
     * 表示用
     * @param pageable
     * @return
     */
    public Page<Contact> findAll(User user, Pageable pageable) {
        return contactRepository.findByUser(user, pageable);
    }

    public Contact create(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact update(Long id, Contact contact, User user) {
        Contact existing = contactRepository.findById(id)
                .orElseThrow();

        if (!existing.getUser().equals(user)) {
            throw new IllegalArgumentException();
        }

        existing.setName(contact.getName());
        existing.setPhone(contact.getPhone());
        existing.setEmail(contact.getEmail());
        existing.setPostalCode(contact.getPostalCode());
        existing.setAddress(contact.getAddress());

         return contactRepository.save(existing);
    }

    public void delete(Long id, User user) {
        //JpaRepository を継承、findById()
        Contact contact = contactRepository.findById(id)
            .orElseThrow();//idを取り出す。なければ例外

        //ログインユーザー確認
        if (!contact.getUser().equals(user)) {
            return;
        }
        contactRepository.deleteById(id);
    }

    public Contact findById(Long id, User user) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow();
        if (!contact.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return contact;
    }

    public void deleteAll(List<Long> ids, User user) {
        for (Long id : ids) {

            Contact contact = contactRepository.findById(id)
                    .orElseThrow();

            if (!contact.getUser().equals(user)) {
                continue;
            }

            contactRepository.delete(contact);
        }
    }
}
