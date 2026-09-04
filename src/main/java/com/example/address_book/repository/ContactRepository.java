package com.example.address_book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.address_book.Contact;

import com.example.address_book.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * DBのデータ処理
 * JpaRepositoryを継承すると、住所録についての基本的なDB操作をSpringが用意
 */
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByUser(User user, Pageable pageable);

}