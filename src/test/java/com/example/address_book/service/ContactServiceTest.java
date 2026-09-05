package com.example.address_book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.address_book.Contact;
import com.example.address_book.repository.ContactRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.address_book.User;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

public class ContactServiceTest {
    @Test
    void createTest() {
        // ContactRepositoryの偽物を作る
        ContactRepository contactRepository = mock(ContactRepository.class);

        // 偽物のRepositoryを使ってContactServiceを作る
        ContactService contactService = new ContactService(contactRepository);

        // テスト用のContactを作る
        Contact contact = new Contact();
        contact.setName("田中太郎");
        contact.setPhone("090-1234-5678");
        contact.setEmail("tanaka@example.com");
        contact.setAddress("東京都");

        // 「save()が呼ばれたら、このcontactを返す」と設定
        when(contactRepository.save(contact))
                .thenReturn(contact);

        // 実際にServiceのcreate()を実行
        Contact result = contactService.create(contact);

        // Serviceから返ってきたContactが、期待したContactと同じか確認
        assertEquals(contact, result);

        // save()が本当に呼ばれたか確認
        Mockito.verify(contactRepository).save(contact);
    }
    
    @Test
    void updateTest() {

        // Repositoryの偽物を作る
        ContactRepository contactRepository =
                mock(ContactRepository.class);

        // 偽物RepositoryをServiceに渡す
        ContactService contactService =
                new ContactService(contactRepository);

        // DBにすでに存在しているContactを作る
        Contact existing = new Contact();
        existing.setName("田中太郎");
        existing.setPhone("090-1111-1111");
        existing.setEmail("old@example.com");
        existing.setAddress("東京都");

        // 更新後に渡すContactを作る
        Contact contact = new Contact();
        contact.setName("山田花子");
        contact.setPhone("090-2222-2222");
        contact.setEmail("new@example.com");
        contact.setAddress("大阪府");

        // findById()されたらexistingを返す
        when(contactRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));

        // save()されたら更新されたexistingを返す
        when(contactRepository.save(existing))
                .thenReturn(existing);

        User user = new User();
        existing.setUser(user);

        // Serviceのupdate()を実行
        Contact result = contactService.update(1L, contact, user);

        // 更新された内容を確認
        assertEquals("山田花子", result.getName());
        assertEquals("090-2222-2222", result.getPhone());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("大阪府", result.getAddress());

        // findById()が呼ばれたことを確認
        Mockito.verify(contactRepository).findById(1L);

        // save()が呼ばれたことを確認
        Mockito.verify(contactRepository).save(existing);
    }

    @Test
    void deleteTest() {
        // ContactRepositoryの偽物を作る
        ContactRepository contactRepository = mock(ContactRepository.class);

        // 偽物のRepositoryを使ってContactServiceを作る
        ContactService contactService = new ContactService(contactRepository);

        // テスト用ユーザー
        User user = new User();

        // 削除するContact
        Contact contact = new Contact();
        contact.setUser(user);

        // 削除するID
        Long id = 1L;

        // findById()されたらcontactを返す
        when(contactRepository.findById(id))
                .thenReturn(java.util.Optional.of(contact));

        // 実際にServiceのdelete()を実行
        contactService.delete(id, user);

        // findById()が呼ばれたことを確認
        Mockito.verify(contactRepository).findById(1L);

        // deleteById()が本当に呼ばれたか確認
        Mockito.verify(contactRepository).deleteById(id);

    }

    @Test
    void findAllTest() {
        // Repositoryの偽物を作る
        ContactRepository contactRepository = mock(ContactRepository.class);

        // 偽物RepositoryをServiceに渡す
        ContactService contactService = new ContactService(contactRepository);

        // テスト用ユーザー
        User user = new User();

        // Pageableの偽物
        Pageable pageable = mock(Pageable.class);

        // Repositoryが返すPageの偽物
        Page<Contact> page = mock(Page.class);

        // findByUser()が呼ばれたらpageを返す
        when(contactRepository.findByUser(user, pageable))
                .thenReturn(page);

        // ServiceのfindAll()を実行
        Page<Contact> result = contactService.findAll(user, pageable);

        // 返ってきたPageが期待したものか確認
        assertEquals(page, result);

        // RepositoryのfindByUser()が呼ばれたか確認
        Mockito.verify(contactRepository)
                .findByUser(user, pageable);
    }

    @Test
    void findByIdTest() {
        // Repositoryの偽物を作る
        ContactRepository contactRepository = mock(ContactRepository.class);

        // 偽物RepositoryをServiceに渡す
        ContactService contactService = new ContactService(contactRepository);

        // テスト用ユーザー
        User user = new User();

        // テスト用Contact
        Contact contact = new Contact();
        contact.setName("田中太郎");
        contact.setPhone("090-1234-5678");
        contact.setEmail("tanaka@example.com");
        contact.setAddress("東京都");
        contact.setUser(user);

        // findById()されたらcontactを返す
        when(contactRepository.findById(1L))
                .thenReturn(java.util.Optional.of(contact));

        // ServiceのfindById()を実行
        Contact result = contactService.findById(1L, user);

        // 期待したContactが返ってきたか確認
        assertEquals(contact, result);

        // RepositoryのfindById()が呼ばれたか確認
        Mockito.verify(contactRepository)
                .findById(1L);
    }

    @Test
    void findByIdOtherUserTest() {
        // Repositoryの偽物を作る
        ContactRepository contactRepository = mock(ContactRepository.class);

        // 偽物RepositoryをServiceに渡す
        ContactService contactService = new ContactService(contactRepository);

        // ログインしているユーザー
        User loginUser = new User();

        // Contactを所有している別ユーザー
        User otherUser = new User();

        // 他ユーザーのContact
        Contact contact = new Contact();
        contact.setName("田中太郎");
        contact.setPhone("090-1234-5678");
        contact.setEmail("tanaka@example.com");
        contact.setAddress("東京都");
        contact.setUser(otherUser);

        // findById()されたら他ユーザーのContactを返す
        when(contactRepository.findById(1L))
                .thenReturn(java.util.Optional.of(contact));

        // 他ユーザーのContactなので
        // ResponseStatusExceptionが発生することを確認
        assertThrows(
                ResponseStatusException.class,
                () -> contactService.findById(1L, loginUser));

        // findById()が呼ばれたことを確認
        Mockito.verify(contactRepository)
                .findById(1L);
    }

    @Test
    void updateOtherUserTest() {
        // Repositoryの偽物を作る
        ContactRepository contactRepository = mock(ContactRepository.class);

        // 偽物RepositoryをServiceに渡す
        ContactService contactService = new ContactService(contactRepository);

        // ログインしているユーザー
        User loginUser = new User();

        // Contactを所有している別ユーザー
        User otherUser = new User();

        // DBに存在している他ユーザーのContact
        Contact existing = new Contact();
        existing.setName("田中太郎");
        existing.setPhone("090-1111-1111");
        existing.setEmail("old@example.com");
        existing.setAddress("東京都");
        existing.setUser(otherUser);

        // 更新用Contact
        Contact contact = new Contact();
        contact.setName("山田花子");
        contact.setPhone("090-2222-2222");
        contact.setEmail("new@example.com");
        contact.setAddress("大阪府");

        // findById()されたら他ユーザーのContactを返す
        when(contactRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));

        // 他ユーザーのContactなので例外が発生することを確認
        assertThrows(
                IllegalArgumentException.class,
                () -> contactService.update(1L, contact, loginUser));

        // findById()が呼ばれたことを確認
        Mockito.verify(contactRepository).findById(1L);

        // 他人のContactなのでsave()は呼ばれないことを確認
        Mockito.verify(contactRepository, Mockito.never())
                .save(existing);
    }

    @Test
    void deleteOtherUserTest() {
        // Repositoryの偽物を作る
        ContactRepository contactRepository = mock(ContactRepository.class);

        // 偽物RepositoryをServiceに渡す
        ContactService contactService = new ContactService(contactRepository);

        // ログインしているユーザー
        User loginUser = new User();

        // Contactを所有している別ユーザー
        User otherUser = new User();

        // 他ユーザーのContact
        Contact contact = new Contact();
        contact.setUser(otherUser);

        Long id = 1L;

        // findById()されたら他ユーザーのContactを返す
        when(contactRepository.findById(id))
                .thenReturn(java.util.Optional.of(contact));

        // Serviceのdelete()を実行
        contactService.delete(id, loginUser);

        // 他ユーザーのContactなのでdeleteById()は呼ばれない
        Mockito.verify(contactRepository, Mockito.never())
                .deleteById(id);
    }

    @Test
    void deleteAllTest() {
        // Repositoryの偽物を作る
        ContactRepository contactRepository =
                mock(ContactRepository.class);

        // 偽物RepositoryをServiceに渡す
        ContactService contactService =
                new ContactService(contactRepository);

        // ログインしているユーザー
        User user = new User();

        // 削除するContactを2件作る
        Contact contact1 = new Contact();
        contact1.setUser(user);

        Contact contact2 = new Contact();
        contact2.setUser(user);

        // 削除するID
        List<Long> ids = List.of(1L, 2L);

        // findById()されたら、それぞれのContactを返す
        when(contactRepository.findById(1L))
                .thenReturn(java.util.Optional.of(contact1));

        when(contactRepository.findById(2L))
                .thenReturn(java.util.Optional.of(contact2));

        // ServiceのdeleteAll()を実行
        contactService.deleteAll(ids, user);

        // 2件ともdelete()されたことを確認
        Mockito.verify(contactRepository).delete(contact1);
        Mockito.verify(contactRepository).delete(contact2);
    }

    @Test
    void deleteAllOtherUserTest() {
        // Repositoryの偽物を作る
        ContactRepository contactRepository =
                mock(ContactRepository.class);

        // 偽物RepositoryをServiceに渡す
        ContactService contactService =
                new ContactService(contactRepository);

        // ログインしているユーザー
        User loginUser = new User();

        // Contactを所有している別ユーザー
        User otherUser = new User();

        // 自分のContact
        Contact myContact = new Contact();
        myContact.setUser(loginUser);

        // 他人のContact
        Contact otherContact = new Contact();
        otherContact.setUser(otherUser);

        // 削除するID
        List<Long> ids = List.of(1L, 2L);

        // 1Lなら自分のContactを返す
        when(contactRepository.findById(1L))
                .thenReturn(java.util.Optional.of(myContact));

        // 2Lなら他人のContactを返す
        when(contactRepository.findById(2L))
                .thenReturn(java.util.Optional.of(otherContact));

        // ServiceのdeleteAll()を実行
        contactService.deleteAll(ids, loginUser);

        // 自分のContactは削除される
        Mockito.verify(contactRepository)
                .delete(myContact);

        // 他人のContactは削除されない
        Mockito.verify(contactRepository, Mockito.never())
                .delete(otherContact);
    }
}
