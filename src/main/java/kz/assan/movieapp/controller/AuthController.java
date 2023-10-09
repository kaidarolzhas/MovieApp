package kz.assan.movieapp.controller;

import jakarta.validation.Valid;
import kz.assan.movieapp.model.User;
import kz.assan.movieapp.util.PersonValidator;
import kz.assan.movieapp.services.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator userValidator;
    private final RegistrationService registrationService;

    public AuthController(PersonValidator userValidator, RegistrationService registrationService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }


    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user){
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user){

        registrationService.register(user);

        return "redirect:/auth/login";
    }
}
