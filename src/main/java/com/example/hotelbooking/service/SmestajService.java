package com.example.hotelbooking.service;

import com.example.hotelbooking.exception.SmestajNotFoundException;
import com.example.hotelbooking.model.Hotel;
import com.example.hotelbooking.model.Smestaj;
import com.example.hotelbooking.repository.SmestajRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmestajService {

    private final SmestajRepo smestajRepo;

    public List<Smestaj> getSmestaji(Hotel hotel) {
        return smestajRepo.findByHotel(hotel);
    }

    public Smestaj getSmestaj(Long id) {
        return smestajRepo.findById(id)
                .orElseThrow(() -> new SmestajNotFoundException("Smestaj sa id-em: " + id + " ne postoji!"));
    }
}
