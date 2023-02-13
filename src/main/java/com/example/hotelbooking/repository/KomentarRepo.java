package com.example.hotelbooking.repository;

import com.example.hotelbooking.model.Hotel;
import com.example.hotelbooking.model.Komentar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KomentarRepo extends JpaRepository<Komentar, Long> {
    List<Komentar> findByHotel(Hotel hotel);
}
