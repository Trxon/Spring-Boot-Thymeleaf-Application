package com.example.hotelbooking.service;

import com.example.hotelbooking.exception.DrzavaNotFoundException;
import com.example.hotelbooking.exception.GradAlreadyExistsException;
import com.example.hotelbooking.exception.GradNotFoundException;
import com.example.hotelbooking.model.Drzava;
import com.example.hotelbooking.model.Grad;
import com.example.hotelbooking.repository.DrzavaRepo;
import com.example.hotelbooking.repository.GradRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GradService {

    private final GradRepo gradRepo;
    private final DrzavaRepo drzavaRepo;

    public List<Grad> getAllGradovi(Drzava drzava) {
        return gradRepo.findByDrzava(drzava);
    }

    public Grad addNewGrad(String naziv, Float povrsina, Integer brojStanovnika, String drzava) {
        Optional<Grad> optionalGrad = gradRepo.findByNaziv(naziv);
        Drzava drzava1 = drzavaRepo.findDrzavaByNaziv(drzava)
                .orElseThrow(() -> new DrzavaNotFoundException("Drzava ne postoji!"));

        if (optionalGrad.isPresent()) {
            throw new GradAlreadyExistsException("Grad: " + naziv + " vec postoji u bazi!");
        } else {
            Grad grad = new Grad();
            grad.setNaziv(naziv);
            grad.setPovrsina(povrsina);
            grad.setBrojStanovnika(brojStanovnika);
            grad.setDrzava(drzava1);
            return gradRepo.save(grad);
        }
    }

    public Grad updateGrad(Grad grad) {
        Grad updatedGrad = gradRepo.findByNaziv(grad.getNaziv())
                .orElseThrow(() -> new GradNotFoundException("Grad: " + grad.getNaziv() + " nije pronadjen u bazi!"));

        updatedGrad.setPovrsina(grad.getPovrsina());
        updatedGrad.setBrojStanovnika(grad.getBrojStanovnika());
        updatedGrad.setImageUrl(grad.getImageUrl());

        return gradRepo.save(updatedGrad);
    }

    public void deleteGrad(String naziv) {
        Grad grad = gradRepo.findByNaziv(naziv)
                .orElseThrow(() -> new GradNotFoundException("Grad: " + naziv + " ne postoji!"));

        gradRepo.delete(grad);
    }

    public Grad getGrad(Long gradId) {
        return gradRepo.findById(gradId)
                .orElseThrow(() -> new GradNotFoundException("Grad sa id-em: " + gradId + " ne postoji!"));
    }

    public List<Grad> getALl() {
        return gradRepo.findAll();
    }

    public Grad updateGrad(Grad grad, Integer povrsina, Integer brojStanovnika) {
        grad.setBrojStanovnika(brojStanovnika);
        grad.setPovrsina(povrsina);
        return gradRepo.save(grad);
    }
}