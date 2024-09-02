package org.astrologist.midea.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.astrologist.midea.entity.User;
import org.astrologist.midea.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/midea")
@Log4j2
@RequiredArgsConstructor
@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private HttpSession session;

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return "redirect:/midea/login";
        } else {
            model.addAttribute("nickname", loggedInUser.getNickname());
        }
        return "midea/contact";
    }

    @PostMapping("/contact")
    public String sendMail(@RequestParam("from") String from,
                           @RequestParam("subject") String subject,
                           @RequestParam("content") String content,
                           Model model) {
        User loggedUser = (User) session.getAttribute("user");
        String userEmail = loggedUser != null ? loggedUser.getEmail() : from;

        // Call the service to send the email
        contactService.sendEmail(userEmail, subject, content);

        // Add success attribute to the model
        model.addAttribute("emailSent", true);

        return "redirect:/midea/contact?success=true";
    }
}
