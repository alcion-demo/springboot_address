package com.example.address_book.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.address_book.Contact;
import com.example.address_book.repository.UserRepository;
import com.example.address_book.service.ContactService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.util.Optional;
import com.example.address_book.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ContactService contactService;

    @MockitoBean
    UserRepository userRepository;


    // GET /contacts のControllerテスト
    @Test
    void findAllTest() throws Exception {

        // Controllerから呼ばれるServiceが返すデータを作る
        Contact contact = new Contact();
        contact.setName("山田太郎");
        contact.setPhone("090-1111-2222");

        // ContactService.findAll() が呼ばれたら、
        // 上で作ったContactを返すように設定する
        when(contactService.findAll())
                .thenReturn(List.of(contact));

        // GET /contacts にリクエストを送る
        mockMvc.perform(get("/contacts"))

            // HTTPステータスが200 OKであることを確認
            .andExpect(status().isOk())

            // JSONの1件目のnameが「山田太郎」であることを確認
            .andExpect(jsonPath("$[0].name").value("山田太郎"))

            // JSONの1件目のphoneが「090-1111-2222」であることを確認
            .andExpect(jsonPath("$[0].phone").value("090-1111-2222"));
    }

    // POST /contacts のControllerテスト
    @Test
    void createTest() throws Exception {

        // Serviceから返ってくる「登録後のContact」を作る
        Contact contact = new Contact();
        contact.setName("佐藤花子");
        contact.setPhone("080-3333-4444");
        contact.setEmail("hanako@example.com");
        contact.setPostalCode("1000001");
        contact.setAddress("東京都千代田区");

        // ContactService.create() が呼ばれたら、
        // 上で作ったContactを返すように設定する
        //
        // any(Contact.class) は
        // 「Contact型の値なら何が渡されてもOK」という意味
        when(contactService.create(any(Contact.class)))
                .thenReturn(contact);

        // POST /contacts にリクエストを送る
        mockMvc.perform(post("/contacts")

                // JSON形式で送信することを指定
                .contentType(MediaType.APPLICATION_JSON)

                // リクエストボディにJSONを入れる
                .content("""
                        {
                            "name": "佐藤花子",
                            "phone": "080-3333-4444",
                            "email": "hanako@example.com",
                            "postalCode": "1000001",
                            "address": "東京都千代田区"
                        }
                        """))

            // 正常に処理されたので200 OKを確認
            .andExpect(status().isOk())

            // 返ってきたJSONのnameを確認
            .andExpect(jsonPath("$.name").value("佐藤花子"))

            // 返ってきたJSONのphoneを確認
            .andExpect(jsonPath("$.phone").value("080-3333-4444"));
    }

    // PUT /contacts/{id} のControllerテスト
    @Test
    void updateTest() throws Exception {

        // 認証しているユーザーを作る
        User user = new User();
        user.setEmail("test@example.com");

        // Serviceから返ってくる「更新後のContact」を作る
        Contact contact = new Contact();
        contact.setName("佐藤花子");
        contact.setPhone("080-3333-4444");
        contact.setEmail("hanako@example.com");
        contact.setPostalCode("1000001");
        contact.setAddress("東京都千代田区");

        // ControllerはAuthenticationからメールアドレスを取得して、
        // UserRepository.findByEmail() でUserを取得する。
        //
        // そのため、テストでは
        // 「test@example.comを検索したら、このuserを返す」
        // という設定をする
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        // ContactService.update() が呼ばれたら、
        // 更新後のContactを返すように設定する
        //
        // eq(1L)
        // → ID「1」のContactを更新したことにする
        //
        // any(Contact.class)
        // → Contact型ならOK
        //
        // eq(user)
        // → 上で作ったuserが渡されたことにする
        when(contactService.update(
                eq(1L),
                any(Contact.class),
                eq(user)))
                .thenReturn(contact);

        // PUT /contacts/1 にリクエストを送る
        mockMvc.perform(put("/contacts/1")
                //ログインユーザーの確認
                .principal(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        List.of()))

                // JSON形式で送信する
                .contentType(MediaType.APPLICATION_JSON)

                // 更新するContactのデータ
                .content("""
                        {
                            "name": "佐藤花子",
                            "phone": "080-3333-4444",
                            "email": "hanako@example.com",
                            "postalCode": "1000001",
                            "address": "東京都千代田区"
                        }
                        """))

                // 正常に更新できたので200 OKを確認
                .andExpect(status().isOk())

                // 返ってきたJSONのnameを確認
                .andExpect(jsonPath("$.name").value("佐藤花子"))

                // 返ってきたJSONのphoneを確認
                .andExpect(jsonPath("$.phone").value("080-3333-4444"));
    }

    // DELETE /contacts/{id} のControllerテスト
    @Test
    void deleteTest() throws Exception {

        // 認証しているユーザーを作る
        User user = new User();
        user.setEmail("test@example.com");

        // ControllerはAuthenticationからメールアドレスを取得して、
        // UserRepository.findByEmail() でUserを取得する。
        //
        // そのため、テストでは
        // 「test@example.comを検索したら、このuserを返す」
        // という設定をする
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        // DELETE /contacts/1 にリクエストを送る
        mockMvc.perform(delete("/contacts/1")
                // ログインユーザーの確認
                .principal(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        List.of())))

                // 正常に削除できたので200 OKを確認
                .andExpect(status().isOk());

        // ContactService.delete() に
        // ID「1」と上で作ったuserが渡されたことを確認
        verify(contactService).delete(1L, user);
    }
}
