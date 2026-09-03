package com.example.address_book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.address_book.Contact;
import com.example.address_book.repository.ContactRepository;

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

        // Serviceのupdate()を実行
        Contact result = contactService.update(1L, contact);

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

        // 削除するID
        Long id = 1L;

        // 実際にServiceのdelete()を実行
        contactService.delete(id);

        // deleteById()が本当に呼ばれたか確認
        Mockito.verify(contactRepository).deleteById(id);

    }
}
