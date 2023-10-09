package kz.assan.movieapp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private LocalDate day;
    private double price;
    private String productImg;
    @ManyToMany
    @JoinTable(
            name = "Basket",
            joinColumns = @JoinColumn(name = "beat_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<User> users;
}
