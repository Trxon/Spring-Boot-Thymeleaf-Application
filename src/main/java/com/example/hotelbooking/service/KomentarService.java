package com.example.hotelbooking.service;

import com.example.hotelbooking.exception.KomentarNotFoundException;
import com.example.hotelbooking.model.Hotel;
import com.example.hotelbooking.model.Komentar;
import com.example.hotelbooking.model.Zvezdica;
import com.example.hotelbooking.repository.KomentarRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KomentarService {

    private final KomentarRepo repo;

    public List<Komentar> getAllKomentar(Hotel hotel) {
        return repo.findByHotel(hotel);
    }

    public Komentar addNewKomentar(Komentar komentar) {
        return repo.save(komentar);
    }

    public Komentar updateKomentar(Long id, String komentar, Zvezdica ocena) {
        Komentar k = repo.findById(id)
                .orElseThrow(() -> new KomentarNotFoundException("Komentar sa id-em: " + id + " nije pronadjen"));

        k.setKomentar(komentar);
        k.setOcena(ocena);
        return repo.save(k);
    }

    public void deleteKomentar(Long id) {
        Komentar k = repo.findById(id)
                .orElseThrow(() -> new KomentarNotFoundException("Komentar sa id-em: " + id + " nije pronadjen"));

        repo.delete(k);
    }

    public Komentar addNewKomentar(Hotel hotel, String komentar, String ocena) {
        Komentar k = new Komentar();
        k.setKomentar(komentar);
        k.setOcena(Zvezdica.valueOf(ocena));
        k.setHotel(hotel);
        return repo.save(k);
    }
}
