package com.example.hotelbooking.service;

import com.example.hotelbooking.exception.HotelAlreadyExistsException;
import com.example.hotelbooking.exception.HotelNotFoundException;
import com.example.hotelbooking.model.*;
import com.example.hotelbooking.repository.HotelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepo repo;

    public List<Hotel> getAllHotel(Grad grad) {
        return repo.findByGrad(grad);
    }

    public Hotel getHotel(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel sa id-em: " + id + " ne postoji!"));
    }

    public void deleteHotel(String naziv) {
        Hotel hotel = repo.findByNaziv(naziv)
                .orElseThrow(() -> new HotelNotFoundException("Hotel: " + naziv + " is not found in database!"));

        repo.delete(hotel);
    }

    public Hotel updateHotel(Hotel hotel) {
        Hotel updatedHotel = repo.findByNaziv(hotel.getNaziv())
                .orElseThrow(() -> new HotelNotFoundException("Hotel: " + hotel.getNaziv() + " is not found in database!"));

        updatedHotel.setUsluga(hotel.getUsluga());
        updatedHotel.setIshrana(hotel.getIshrana());
        updatedHotel.setZvezdica(hotel.getZvezdica());

        return repo.save(updatedHotel);
    }

    public List<Hotel> getAll() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "datumIzgradnje"));
    }

    public Hotel addNewHotel(String naziv, String zvezdica, String datumIzgradnje, String usluga, String ishrana, String imageUrl, Grad grad) throws ParseException {
        if (repo.findByNaziv(naziv).isPresent()) {
            throw new HotelAlreadyExistsException("Hotel: " + naziv + " already exists!");
        } else {
            Hotel hotel = new Hotel();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date datIzgr = sdf.parse(datumIzgradnje);
            if (datIzgr.after(new Date())) {
                return null;
            }
            hotel.setNaziv(naziv);
            hotel.setGrad(grad);
            hotel.setZvezdica(Zvezdica.valueOf(zvezdica));
            hotel.setDatumIzgradnje(datIzgr);
            hotel.setUsluga(Usluga.valueOf(usluga));
            hotel.setIshrana(Ishrana.valueOf(ishrana));
            hotel.setImageUrl(imageUrl);
            return repo.save(hotel);
        }
    }
}
