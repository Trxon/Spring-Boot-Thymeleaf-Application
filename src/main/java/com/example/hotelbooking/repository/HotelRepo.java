package com.example.hotelbooking.repository;

import com.example.hotelbooking.model.Grad;
import com.example.hotelbooking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByNaziv(String naziv);
    List<Hotel> findByGrad(Grad grad);
}
