package com.example.hotelbooking.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Drzava implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false)
    private String naziv;
    private float povrsina;
    private int brojStanovnika;
    private String valuta;
    private String imageUrl;
    @OneToMany(mappedBy = "drzava", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Grad> gradovi;

}
