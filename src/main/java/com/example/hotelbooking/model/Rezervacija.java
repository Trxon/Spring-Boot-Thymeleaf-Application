package com.example.hotelbooking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rezervacija implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Boolean rezervisano;
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private Date datumRezervacije;
    private Date datumIsteka;
    @ManyToOne
    @JoinColumn(name = "smestaj_id")
    @JsonBackReference
    private Smestaj smestaj;

}
