package com.example.address_book.controller;

// GETリクエストを送るために使用
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

// Modelに入れた値を確認するために使用
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

// HTTPステータスを確認するために使用
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Controllerが返した画面名を確認するために使用
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.address_book.repository.UserRepository;
import com.example.address_book.service.ContactService;

import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.example.address_book.Contact;
import com.example.address_book.User;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

// PageControllerだけを対象にして、Controllerのテストを行う
@WebMvcTest(PageController.class)
public class PageControllerTest {

    // Controllerに対してHTTPリクエストを送るためのテスト用オブジェクト
    @Autowired
    private MockMvc mockMvc;

    // PageControllerが必要としているServiceをモックとして用意する
    @MockitoBean
    private ContactService contactService;

    // PageControllerが必要としているUserRepositoryをモックとして用意する
    @MockitoBean
    private UserRepository userRepository;

    // PageControllerが必要としているPasswordEncoderをモックとして用意する
    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void helloTest() throws Exception {

        // /hello にGETリクエストを送る
        mockMvc.perform(get("/hello"))

                // HTTPステータスが200 OKになることを確認
                .andExpect(status().isOk())

                // Controllerが「hello」という画面を返すことを確認
                // PageControllerの return "hello"; に対応
                .andExpect(view().name("hello"))

                // Modelに「name」という名前で
                // 「田中太郎」が入っていることを確認
                // PageControllerの
                // model.addAttribute("name", "田中太郎");
                // に対応
                .andExpect(model().attribute("name", "田中太郎"));
    }

    @Test
    void homeTest() throws Exception {

        // "/" にGETリクエストを送る
        mockMvc.perform(get("/"))

                // リダイレクトなのでHTTPステータスが302になることを確認
                .andExpect(status().isFound())

                // "/login" にリダイレクトすることを確認
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    void loginTest() throws Exception {

        // "/login" にGETリクエストを送る
        mockMvc.perform(get("/login"))

                // HTTPステータスが200 OKになることを確認
                .andExpect(status().isOk())

                // Controllerが「login」という画面を返すことを確認
                // PageControllerの return "login"; に対応
                .andExpect(view().name("login"));
    }

    @Test
    void registerTest() throws Exception {

        // "/register" にGETリクエストを送る
        mockMvc.perform(get("/register"))

                // HTTPステータスが200 OKになることを確認
                .andExpect(status().isOk())

                // Controllerが「register」という画面を返すことを確認
                // PageControllerの return "register"; に対応
                .andExpect(view().name("register"))

                // Modelに「user」という名前でUserオブジェクトが
                // 入っていることを確認
                // PageControllerの
                // model.addAttribute("user", new User());
                // に対応
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void contactsTest() throws Exception {

        // ログインしているユーザーのメールアドレスを設定
        // PageControllerでは authentication.getName() でメールアドレスを取得している
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        java.util.List.of()
                );

        // テスト用のUserを作る
        User user = new User();

        // UserRepositoryにメールアドレスを渡したら
        // 上で作ったUserを返すように設定する
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(java.util.Optional.of(user));

        // テスト用のContactを作る
        Contact contact = new Contact();

        // Contactを1件入れたPageを作る
        Page<Contact> contacts =
                new PageImpl<>(java.util.List.of(contact));

        // ContactServiceに検索を依頼したら
        // 上で作ったcontactsを返すように設定する
        when(contactService.findAll(
                any(User.class),
                any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(contacts);

        // "/contacts/view" にGETリクエストを送る
        // principal(authentication) → ログインユーザーを再現
        // csrf() → CSRFトークンをテスト用に用意
        mockMvc.perform(get("/contacts/view")
                .principal(authentication)
                .with(csrf()))

                // HTTPステータスが200 OKになることを確認
                .andExpect(status().isOk())

                // "contacts" という画面を返すことを確認
                .andExpect(view().name("contacts"))

                // Modelに"contacts"が入っていることを確認
                .andExpect(model().attribute("contacts", contacts));
    }

    @Test
    void createFormTest() throws Exception {

        // "/contacts/view/create" にGETリクエストを送る
        mockMvc.perform(get("/contacts/view/create"))

                // HTTPステータスが200 OKになることを確認
                .andExpect(status().isOk())

                // Controllerが「contact-form」という画面を返すことを確認
                // PageControllerの return "contact-form"; に対応
                .andExpect(view().name("contact-form"))

                // Modelに「contact」という名前のデータが
                // 入っていることを確認
                // PageControllerの
                // model.addAttribute("contact", new Contact());
                // に対応
                .andExpect(model().attributeExists("contact"));
    }

    @Test
    void createTest() throws Exception {

        // ログインしているユーザーを再現する
        // PageControllerでは authentication.getName() で
        // メールアドレスを取得している
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        java.util.List.of()
                );

        // テスト用のUserを作る
        User user = new User();

        // 「test@example.com」で検索されたら
        // 上で作ったUserを返すように設定する
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(java.util.Optional.of(user));

        // "/contacts/view/create" にPOSTリクエストを送る
        mockMvc.perform(post("/contacts/view/create")
                .principal(authentication)

                // CSRFトークンをテスト用に用意する
                .with(csrf())

                // Contactの各項目をフォームから送信する
                // @NotBlank、@Email、@Patternなどを
                // すべて通過する値にしている
                .param("name", "山田太郎")
                .param("phone", "090-1234-5678")
                .param("email", "contact@example.com")
                .param("postalCode", "1234567")
                .param("address", "東京都新宿区"))

                // 登録成功なので /contacts/view にリダイレクトする
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/contacts/view"));

        // ContactServiceのcreate()が1回呼ばれたことを確認する
        verify(contactService).create(any(Contact.class));
    }

    @Test
    void createValidationErrorTest() throws Exception {

        // ログインしているユーザーを再現する
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        java.util.List.of()
                );

        // 入力エラーになるようにnameを空にしてPOSTする
        mockMvc.perform(post("/contacts/view/create")
                .principal(authentication)
                .with(csrf())

                // nameは@NotBlankなので空文字だとバリデーションエラーになる
                .param("name", "")

                // それ以外は正常な値を入れておく
                .param("phone", "090-1234-5678")
                .param("email", "contact@example.com")
                .param("postalCode", "1234567")
                .param("address", "東京都新宿区"))

                // バリデーションエラーでも画面を正常に返すので200 OK
                .andExpect(status().isOk())

                // エラーがある場合はcontact-formに戻る
                .andExpect(view().name("contact-form"))

                // バリデーションエラーが発生していることを確認
                .andExpect(model().attributeHasFieldErrors("contact", "name"));

        // バリデーションエラーなので、
        // ContactServiceのcreate()は呼ばれていないことを確認
        verify(contactService, org.mockito.Mockito.never())
                .create(any(Contact.class));
    }

    @Test
    void deleteSelectedTest() throws Exception {

        // ログインしているユーザーを再現する
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        java.util.List.of()
                );

        // テスト用のUserを作る
        User user = new User();

        // メールアドレスからUserを取得できるように設定する
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(java.util.Optional.of(user));

        // "/contacts/view/delete" にPOSTリクエストを送る
        mockMvc.perform(post("/contacts/view/delete")
                .principal(authentication)
                .with(csrf())

                // 削除対象のIDを2件送る
                .param("ids", "1", "2"))

                // 削除後は一覧画面へリダイレクトする
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/contacts/view"));

        // ContactServiceのdeleteAll()が
        // 指定したIDとUserで1回呼ばれたことを確認する
        verify(contactService).deleteAll(
                java.util.List.of(1L, 2L),
                user
        );
    }

    @Test
    void deleteTest() throws Exception {

        // ログインしているユーザーを再現する
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        java.util.List.of()
                );

        // テスト用のUserを作る
        User user = new User();

        // メールアドレスからUserを取得できるように設定する
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(java.util.Optional.of(user));

        // "/contacts/view/delete/1" にPOSTリクエストを送る
        // URLの「1」が@PathVariable Long idに入る
        mockMvc.perform(post("/contacts/view/delete/1")
                .principal(authentication)
                .with(csrf()))

                // 削除後は一覧画面へリダイレクトする
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/contacts/view"));

        // ContactServiceのdelete()が
        // ID「1」とUserを渡して1回呼ばれたことを確認する
        verify(contactService).delete(1L, user);
    }

    @Test
    void editFormTest() throws Exception {

        // ログインしているユーザーを再現する
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        java.util.List.of()
                );

        // テスト用のUserを作る
        User user = new User();

        // メールアドレスからUserを取得できるように設定する
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(java.util.Optional.of(user));

        // テスト用のContactを作る
        Contact contact = new Contact();

        // ID「1」のContactを取得したら、
        // 上で作ったcontactを返すように設定する
        when(contactService.findById(1L, user))
                .thenReturn(contact);

        // "/contacts/view/edit/1" にGETリクエストを送る
        mockMvc.perform(get("/contacts/view/edit/1")
                .principal(authentication)
                .with(csrf()))

                // HTTPステータスが200 OKになることを確認
                .andExpect(status().isOk())

                // "contact-edit" という画面を返すことを確認
                .andExpect(view().name("contact-edit"))

                // Modelに取得したContactが
                // "contact"という名前で入っていることを確認
                .andExpect(model().attribute("contact", contact));
    }

    @Test
    void updateTest() throws Exception {

        // ログインしているユーザーを再現する
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        java.util.List.of()
                );

        // テスト用のUserを作る
        User user = new User();

        // メールアドレスからUserを取得できるように設定する
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(java.util.Optional.of(user));

        // "/contacts/view/edit/1" にPOSTリクエストを送る
        mockMvc.perform(post("/contacts/view/edit/1")
                .principal(authentication)
                .with(csrf())
                .param("name", "山田太郎")
                .param("phone", "090-1234-5678")
                .param("email", "contact@example.com")
                .param("postalCode", "1234567")
                .param("address", "東京都新宿区"))

                // 更新成功なので /contacts/view にリダイレクトする
                .andExpect(status().isFound())

                // /contacts/view にリダイレクトすることを確認
                .andExpect(view().name("redirect:/contacts/view"));

        // ContactServiceのupdate()が
        // ID「1」とContactとUserを渡して1回呼ばれたことを確認する
        verify(contactService).update(
                org.mockito.ArgumentMatchers.eq(1L),
                any(Contact.class),
                org.mockito.ArgumentMatchers.eq(user)
        );
    }

    @Test
    void updateValidationErrorTest() throws Exception {

        // ログインしているユーザーを再現する
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        java.util.List.of()
                );

        // nameを空にしてPOSTする
        // @NotBlankに違反するのでバリデーションエラーになる
        mockMvc.perform(post("/contacts/view/edit/1")
                .principal(authentication)
                .with(csrf())
                .param("name", "")
                .param("phone", "090-1234-5678")
                .param("email", "contact@example.com")
                .param("postalCode", "1234567")
                .param("address", "東京都新宿区"))

                // バリデーションエラーなので200 OK
                .andExpect(status().isOk())

                // contact-edit画面に戻る
                .andExpect(view().name("contact-edit"))

                // nameにバリデーションエラーがあることを確認
                .andExpect(model().attributeHasFieldErrors("contact", "name"));

        // バリデーションエラーなのでupdate()は呼ばれない
        verify(contactService, org.mockito.Mockito.never())
                .update(
                        org.mockito.ArgumentMatchers.eq(1L),
                        any(Contact.class),
                        any(User.class)
                );
    }

    @Test
    void createUserTest() throws Exception {

        // "/register" にPOSTリクエストを送る
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("name", "山田太郎")
                .param("email", "test@example.com")
                .param("password", "password123"))

                // 登録成功なので /login にリダイレクトする
                .andExpect(status().isFound())

                // /login にリダイレクトすることを確認
                .andExpect(view().name("redirect:/login"));

        // パスワードを暗号化する処理が呼ばれたことを確認
        verify(passwordEncoder).encode("password123");

        // UserRepositoryのsave()が呼ばれたことを確認
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUserValidationErrorTest() throws Exception {

        // "/register" にPOSTリクエストを送る
        // nameを空にして、バリデーションエラーを発生させる
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("name", "")
                .param("email", "test@example.com")
                .param("password", "password123"))

                // バリデーションエラーなので200 OK
                .andExpect(status().isOk())

                // エラーがある場合はregister画面に戻る
                .andExpect(view().name("register"))

                // nameにバリデーションエラーがあることを確認
                .andExpect(model().attributeHasFieldErrors("user", "name"));

        // バリデーションエラーなので
        // パスワードの暗号化処理は呼ばれない
        verify(passwordEncoder, org.mockito.Mockito.never())
                .encode(any(String.class));

        // バリデーションエラーなので
        // UserRepositoryのsave()も呼ばれない
        verify(userRepository, org.mockito.Mockito.never())
                .save(any(User.class));
    }

}
