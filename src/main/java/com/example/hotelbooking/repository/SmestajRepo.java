package com.example.hotelbooking.repository;

import com.example.hotelbooking.model.Hotel;
import com.example.hotelbooking.model.Smestaj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmestajRepo extends JpaRepository<Smestaj, Long> {
    List<Smestaj> findByHotel(Hotel hotel);
}
