package kz.assan.movieapp.controller;


import kz.assan.movieapp.model.User;
import kz.assan.movieapp.services.UserService;
import kz.assan.movieapp.model.Movie;
import kz.assan.movieapp.services.MovieService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class mainController {
    @Value("${file.img.viewPath}")
    private String viewPath;

    @Value("${file.img.defaultPicture}")
    private String defaultPicture;

    @Value("${file.img.uploadPath}")
    private String uploadPath;

    private final MovieService movieService;
    private final UserService userService;

    public mainController(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @GetMapping(value = "/viewPhoto/{url}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] viewPicture(@PathVariable(name = "url") String url) throws IOException {
        String pictureURL = viewPath + defaultPicture;

        if(url != null && !url.equals("null")) {
            pictureURL = viewPath + url + ".jpg";
        }
        InputStream in;

        try {
            ClassPathResource resource = new ClassPathResource(pictureURL);
            in = resource.getInputStream();
        } catch (Exception e) {
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }


    @GetMapping("/")
    public String movies(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<Movie> movieList = movieService.findAllMovies();
        model.addAttribute("kino", movieList);
        User user = userService.findByUsername(currentPrincipalName);
        if (user != null && user.getRole().equals("ROLE_ADMIN")) {
            return "admin/index.html";
        }
        return "user/index";
    }

    @GetMapping(value = "/movies/{idshka}")
    public String details(Model model, @PathVariable(name = "idshka") int id){
        Movie movie = movieService.findMovieById(id);
        model.addAttribute("kino", movie);
        return "user/details";
    }

    @PostMapping(value = "/kino/{idshka}")
    public String addBasket(@PathVariable(name = "idshka") int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User person = userService.findByUsername(authentication.getName());
        Movie movie = movieService.findMovieById(id);
        if(!person.getMovies().contains(movie)) {
            person.getMovies().add(movie);
            movie.getUsers().add(person);

            userService.savePerson(person);
            movieService.saveMovie(movie);
        }

        return "redirect:/";
    }

    @GetMapping(value = "/basket")
    public String myBasket(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUsername(authentication.getName());
        int price = 0;

        for (Movie b: user.getMovies()) {
            price += b.getPrice();
        }

        model.addAttribute("total", user.getMovies().size());
        model.addAttribute("price", price);
        model.addAttribute("myBasket", user.getMovies());

        return "user/basket";
    }


    @DeleteMapping("/movies/{id}")
    public String delete(@PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        Movie movie = movieService.findMovieById(id);
        user.getMovies().remove(movie);
        movie.getUsers().remove(user);
        userService.savePerson(user);
        movieService.saveMovie(movie);

        return "redirect:/basket";
    }

    @DeleteMapping("/order")
    public String deleteMoviesByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        movieService.deleteMoviesByUser(user);

        return "redirect:/basket";
    }
}
