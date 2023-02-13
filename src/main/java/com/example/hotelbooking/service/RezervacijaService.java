package com.example.hotelbooking.service;

import com.example.hotelbooking.exception.RezervacijaNotFoundException;
import com.example.hotelbooking.model.Rezervacija;
import com.example.hotelbooking.model.Smestaj;
import com.example.hotelbooking.repository.RezervacijaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RezervacijaService {

    private final RezervacijaRepo rezervacijaRepo;

    public Rezervacija addRezervacija(String datumIsteka, String datumRezervacije, Smestaj smestaj) throws ParseException {
        Rezervacija rezervacija = new Rezervacija();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateRez = sdf.parse(datumRezervacije);
        Date dateIst = sdf.parse(datumIsteka);
        calendar.add(Calendar.HOUR_OF_DAY, -1);

        if(dateRez.getYear() == calendar.getTime().getYear() && dateRez.getMonth() == calendar.getTime().getMonth() &&
                dateRez.getDay() == calendar.getTime().getDay() && dateIst.after(dateRez) && dateIst.getDay() != dateRez.getDay()) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            dateRez.setMinutes(calendar.getTime().getMinutes());
            dateRez.setHours(calendar.getTime().getHours());
            dateIst.setMinutes(0);
            dateIst.setHours(9);
            rezervacija.setSmestaj(smestaj);
            rezervacija.setDatumIsteka(dateIst);
            rezervacija.setDatumRezervacije(dateRez);
            rezervacija.setRezervisano(true);
            return rezervacijaRepo.save(rezervacija);
        }

        else if (dateRez.before(calendar.getTime()) || dateIst.before(calendar.getTime())
                || dateIst.before(dateRez) || dateRez.equals(dateIst)) {
            return null;
        } else {
            dateRez.setMinutes(0);
            dateRez.setHours(9);
            dateIst.setMinutes(0);
            dateIst.setHours(9);
            rezervacija.setSmestaj(smestaj);
            rezervacija.setDatumIsteka(dateIst);
            rezervacija.setDatumRezervacije(dateRez);
            rezervacija.setRezervisano(true);
            return rezervacijaRepo.save(rezervacija);
        }
    }

    public List<Rezervacija> getAllRezervacije() {
        return rezervacijaRepo.findAll();
    }

    public Rezervacija getRezervacija(Long id) {
        return rezervacijaRepo.findById(id)
                .orElseThrow(() -> new RezervacijaNotFoundException("Rezervacija sa id-em: " + id + " ne postoji"));
    }

    public void deleteRezervacija(Rezervacija rezervacija) {
        rezervacijaRepo.delete(rezervacija);
    }


    public List<Rezervacija> getRezervacije(List<Smestaj> smestaji, Date datumOd, Date datumDo) {
        List<Rezervacija> rezervacije = new ArrayList<>();

        for (Smestaj smestaj: smestaji) {
            List<Rezervacija> rez = rezervacijaRepo.findAllBySmestaj(datumOd, datumDo, smestaj);
            rezervacije.addAll(rez);
        }

        // List<Rezervacija> sortRez = rezervacije.stream().sorted(Comparator.comparing(Rezervacija::getDatumRezervacije)).collect(Collectors.toList());
        return rezervacije;
    }
}
