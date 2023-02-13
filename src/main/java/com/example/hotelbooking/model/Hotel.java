package com.example.hotelbooking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hotel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String naziv;
    @Enumerated(value = EnumType.STRING)
    private Zvezdica zvezdica;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datumIzgradnje;
    @Enumerated(value = EnumType.STRING)
    private Usluga usluga;
    @Enumerated(value = EnumType.STRING)
    private Ishrana ishrana;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "grad_id")
    @JsonBackReference
    private Grad grad;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Smestaj> smestaji;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Komentar> komentari;

}
