package kz.assan.movieapp.controller;

import jakarta.validation.Valid;
import kz.assan.movieapp.model.Movie;
import kz.assan.movieapp.services.MovieService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Value("${file.img.uploadPath}")
    private String uploadPath;

    private final MovieService movieService;

    public AdminController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/add")
    public String newPerson(@ModelAttribute("movie") Movie movie) {
        return "admin/addProduct";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("product") @Valid Movie movie, BindingResult bindingResult,
                         @RequestParam(name = "file") MultipartFile file) throws IOException {
        if (bindingResult.hasErrors())
            return "admin/addProduct";
        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
            try {
                String picName = DigestUtils.sha1Hex("img" + movie.getTitle() + "file1");
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName + ".jpg");
                Files.write(path, bytes);
                movie.setProductImg(picName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        movieService.saveMovie(movie);
        return "redirect:/";
    }


    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        movieService.deleteMovie(id);
        return "redirect:/";
    }

    @PatchMapping("/update/{id}")
    public String update(@ModelAttribute("product") @Valid Movie movie, BindingResult bindingResult,
                         @PathVariable("id") int id,
                         @RequestParam(name = "file") MultipartFile file,
                         @RequestParam(name = "productImg") String productImg) {

        if (bindingResult.hasErrors()) {
            return "admin/update";
        }

        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
            try {
                String picName = DigestUtils.sha1Hex("img" + movie.getTitle() + "file1");

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName + ".jpg");
                Files.write(path, bytes);

                movie.setProductImg(picName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (file.getContentType().equals(null) || file.getContentType().equals("")) {
            movie.setProductImg(productImg);
        }

        movieService.updateMovie(id, movie);
        return "redirect:/";
    }


    @GetMapping("/update/{id}")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("movie", movieService.findMovieById(id));
        return "admin/update";
    }
}
