package kz.assan.movieapp.repository;


import kz.assan.movieapp.model.User;
import kz.assan.movieapp.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findById(int id);

    List<Movie> findByUsersContaining(Optional<User> user);
}
