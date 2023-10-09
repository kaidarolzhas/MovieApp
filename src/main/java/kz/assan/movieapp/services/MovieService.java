package kz.assan.movieapp.services;

import kz.assan.movieapp.model.User;
import kz.assan.movieapp.repository.UsersRepository;
import kz.assan.movieapp.model.Movie;
import kz.assan.movieapp.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MovieService {
    private final MovieRepository movieRepository;
    private final UsersRepository usersRepository;

    public MovieService(MovieRepository movieRepository, UsersRepository usersRepository) {
        this.movieRepository = movieRepository;
        this.usersRepository = usersRepository;
    }

    public List<Movie> findAllMovies(){
        return  movieRepository.findAll();
    }
    public Movie findMovieById(int id){
        return movieRepository.findById(id);
    }

    @Transactional
    public void deleteMoviesByUser(User user) {
        List<Movie> movies = user.getMovies();
        for (Movie movie : movies) {
            movie.getUsers().remove(user);
            movieRepository.save(movie);
        }
        user.setMovies(null);
        usersRepository.save(user);
    }


    @Transactional
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Long id){
        movieRepository.deleteById(id);
    }


    @Transactional
    public void updateMovie(int id, Movie movie){
        movie.setId(id);
        movieRepository.save(movie);
    }

}


