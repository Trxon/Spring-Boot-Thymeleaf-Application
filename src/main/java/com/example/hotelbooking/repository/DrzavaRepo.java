package com.example.hotelbooking.repository;

import com.example.hotelbooking.model.Drzava;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrzavaRepo extends JpaRepository<Drzava, Long> {
    Optional<Drzava> findDrzavaByNaziv(String naziv);
}
