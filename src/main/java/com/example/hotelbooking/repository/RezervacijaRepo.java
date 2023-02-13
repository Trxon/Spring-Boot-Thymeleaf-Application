package com.example.hotelbooking.repository;

import com.example.hotelbooking.model.Rezervacija;
import com.example.hotelbooking.model.Smestaj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RezervacijaRepo extends JpaRepository<Rezervacija, Long> {
    @Query("select r from Rezervacija r where r.datumRezervacije between :datumOd and :datumDo and r.smestaj = :smestaj")
    List<Rezervacija> findAllBySmestaj(@Param("datumOd") Date datumOd, @Param("datumDo") Date datumDo, @Param("smestaj") Smestaj smestaj);
}
