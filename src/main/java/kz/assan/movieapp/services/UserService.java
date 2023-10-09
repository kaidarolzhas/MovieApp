package kz.assan.movieapp.services;


import kz.assan.movieapp.repository.UsersRepository;
import kz.assan.movieapp.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    public User findByUsername(String s){
        Optional<User> person = usersRepository.findByUsername(s);

        return person.orElse(null);
    }

    @Transactional
    public void savePerson(User person){
        usersRepository.save(person);
    }

    @Transactional
    public void updatePerson(int id, User updatedPerson){
        updatedPerson.setId(id);

        usersRepository.save(updatedPerson);
    }

    public Optional<User> findUserById(Long userId) {
        Optional<User> person = usersRepository.findById(userId);

        return person;
    }
}