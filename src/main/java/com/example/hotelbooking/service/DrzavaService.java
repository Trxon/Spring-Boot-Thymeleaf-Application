package com.example.hotelbooking.service;

import com.example.hotelbooking.exception.DrzavaAlreadyExistsException;
import com.example.hotelbooking.exception.DrzavaNotFoundException;
import com.example.hotelbooking.model.Drzava;
import com.example.hotelbooking.repository.DrzavaRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrzavaService {

    private final DrzavaRepo drzavaRepo;

    public Drzava addNewDrzava(Drzava drzava) {
        Optional<Drzava> optionalDrzava = drzavaRepo.findDrzavaByNaziv(drzava.getNaziv());

        if (optionalDrzava.isPresent()) {
            log.error("Drzava already exists");
            throw new DrzavaAlreadyExistsException("Drzava: " + drzava.getNaziv() + " vec postoji u bazi.");
        } else {
            return drzavaRepo.save(drzava);
        }
    }

    public List<Drzava> getAllDrzave() {
        log.info("Fetching all Drzave");
        return drzavaRepo.findAll();
    }

    public Drzava getDrzava(Long drzavaId) {
        return drzavaRepo.findById(drzavaId)
                .orElseThrow(() -> new DrzavaNotFoundException("Drzava sa id-em: " + drzavaId + " ne postoji!"));
    }

    public Drzava updateDrzava(Drzava drzava) {
        Drzava updatedDrzava = drzavaRepo.findDrzavaByNaziv(drzava.getNaziv())
                .orElseThrow(() -> new DrzavaNotFoundException("Drzava: " + drzava.getNaziv() + " ne postoji u bazi!"));

        updatedDrzava.setPovrsina(drzava.getPovrsina());
        updatedDrzava.setValuta(drzava.getValuta());
        updatedDrzava.setBrojStanovnika(drzava.getBrojStanovnika());
        updatedDrzava.setImageUrl(drzava.getImageUrl());

        log.info("Successfully updated " + drzava.getNaziv());
        return drzavaRepo.save(updatedDrzava);
    }

    public void deleteDrzava(String naziv) {
        Drzava drzava = drzavaRepo.findDrzavaByNaziv(naziv)
                .orElseThrow(() -> new DrzavaNotFoundException("Drzava: " + naziv + " ne postoji u bazi!"));

        log.info("Successfully deleted " + naziv);
        drzavaRepo.delete(drzava);
    }
}
