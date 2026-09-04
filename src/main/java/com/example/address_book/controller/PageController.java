package com.example.address_book.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.address_book.Contact;
import com.example.address_book.service.ContactService;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Controller
public class PageController {

    private final ContactService contactService;

    public PageController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "田中太郎");

        return "hello";
    }

    /**
     * 住所一覧取得
     * @param model
     * @return view(string)
     */
    @GetMapping("/contacts/view")
    public String contacts(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Contact> contacts = contactService.findAll(pageable);

        model.addAttribute("contacts", contacts);

        return "contacts";
    }

    /**
     * 一括削除
     * @param ids
     * @return
     */
    @PostMapping("/contacts/view/delete")
    public String deleteSelected(@RequestParam List<Long> ids) {
        contactService.deleteAll(ids);
        return "redirect:/contacts/view";
    }

    /**
     * 新規登録画面を表示
     * @return view(string)
     */
    @GetMapping("/contacts/view/create")
    public String createForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact-form";
    }

    /**
     * 登録フォームの値を受け取る
     * @param contact
     * @param result
     * @return view(string)
     */
    @PostMapping("/contacts/view/create")
    public String create(
        @Valid Contact contact,
        BindingResult result) {

        if (result.hasErrors()) {
            return "contact-form";
        }
        contactService.create(contact);
        return "redirect:/contacts/view";
    }

    /**
     * 削除
     * @param id
     * @return
     */
    @PostMapping("/contacts/view/delete/{id}")
    public String delete(@PathVariable Long id) {
        contactService.delete(id);
        return "redirect:/contacts/view";
    }

    /**
     * 編集画面表示
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/contacts/view/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model) {

        Contact contact = contactService.findById(id);
        model.addAttribute("contact", contact);

        return "contact-edit";
    }

    /**
     * 編集
     * @param id
     * @param contact
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/contacts/view/edit/{id}")
    public String update(
            @PathVariable Long id,
            @Valid Contact contact,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("contact", contact);
            return "contact-edit";
        }

        contactService.update(id, contact);

        return "redirect:/contacts/view";
    }
}
