package com.example.address_book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.address_book.Contact;

/**
 * DBのデータ処理
 * JpaRepositoryを継承すると、住所録についての基本的なDB操作をSpringが用意
 */
public interface ContactRepository extends JpaRepository<Contact, Long> {
}