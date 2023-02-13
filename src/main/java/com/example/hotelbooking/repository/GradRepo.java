package com.example.hotelbooking.repository;

import com.example.hotelbooking.model.Drzava;
import com.example.hotelbooking.model.Grad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradRepo extends JpaRepository<Grad, Long> {
    Optional<Grad> findByNaziv(String naziv);
    List<Grad> findByDrzava(Drzava drzava);
}
