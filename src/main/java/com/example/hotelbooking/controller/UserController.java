package com.example.hotelbooking.controller;

import com.example.hotelbooking.model.*;
import com.example.hotelbooking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {

    private final DrzavaService drzavaService;
    private final HotelService hotelService;
    private final GradService gradService;
    private final KomentarService komentarService;
    private final SmestajService smestajService;
    private final RezervacijaService rezervacijaService;

    @GetMapping("/hello")
    public String hello(){
        return "user_dashboard";
    }

    // REZERVACIJA HOTELSKE SOBE
    @GetMapping("/drzava-all")
    public String getDrzave(Model model) {
        List<Drzava> drzave = drzavaService.getAllDrzave();
        model.addAttribute("drzave", drzave);
        return "PrikazDrzava";
    }

    @GetMapping("/grad-all")
    public String getGradovi(Model m, Long drzavaId) {
        Drzava drzava = drzavaService.getDrzava(drzavaId);
        List<Grad> gradovi = gradService.getAllGradovi(drzava);
        m.addAttribute("gradovi", gradovi);
        m.addAttribute("drzava", drzava);
        return "PrikazGradova";
    }

    @GetMapping("/hotel-all")
    public String getHoteli(Model model, Long gradId) {
        Grad grad = gradService.getGrad(gradId);
        List<Hotel> hoteli = hotelService.getAllHotel(grad);
        model.addAttribute("hoteli", hoteli);
        model.addAttribute("grad", grad);
        return "PrikazHotela";
    }

    @GetMapping("/komentar-all")
    public String getKomentari(Model model, Long hotelId) {
        Hotel hotel = hotelService.getHotel(hotelId);
        List<Komentar> komentari = komentarService.getAllKomentar(hotel);
        model.addAttribute("komentari", komentari);
        model.addAttribute("hotel", hotel);
        return "PrikazKomentara";
    }

    @GetMapping("/smestaj-all")
    public String getSmestaji(Model model, Long hotelId) {
        Hotel hotel = hotelService.getHotel(hotelId);
        List<Smestaj> smestaji = smestajService.getSmestaji(hotel);
        model.addAttribute("smestaji", smestaji);
        model.addAttribute("hotel", hotel);
        return "PrikazSmestaja";
    }

    @GetMapping("rezervacija-add")
    public String addRezervacija(Model m, Long smestajId) {
        Smestaj smestaj = smestajService.getSmestaj(smestajId);
        m.addAttribute("smestaj", smestaj);
        if (!smestaj.getRezervacije().isEmpty()) {
            return "ZauzetSmestaj";
        } else {
            m.addAttribute("smestaj", smestaj);
            return "UnosRezervacije";
        }
    }

    @PostMapping("/rezervacija-add-new")
    public String rezervisi(Model m, String datumRezervacije, String datumIsteka, Long smestajId) throws ParseException {
        Smestaj smestaj = smestajService.getSmestaj(smestajId);
        Rezervacija rezervacija = rezervacijaService.addRezervacija(datumIsteka, datumRezervacije, smestaj);
        if (rezervacija != null) {
            m.addAttribute("rezervacija", rezervacija);
            return "Rezervacija";
        } else {
            return "NeuspelaRezervacija";
        }
    }

    // PREGLED KOMENTARA ZA ODABRANI HOTEL
    @GetMapping("/hotel-all-show")
    public String getHoteliAll(Model model) {
        List<Hotel> hoteli = hotelService.getAll();
        model.addAttribute("hoteli", hoteli);
        return "PregledKomentara";
    }

    // UNOS KOMENTARA ZA ODABRANI HOTEL
    @GetMapping("/hotel-all-komentar")
    public String getHoteliAllKomentar(Model model) {
        List<Hotel> hoteli = hotelService.getAll();
        List<Zvezdica> ocene = List.of(Zvezdica.values());
        model.addAttribute("hoteli", hoteli);
        model.addAttribute("ocene", ocene);
        return "UnosKomentara";
    }

    @PostMapping("/komentar-add")
    public String addKomentarHotel(Model model, Long hotelId, String komentar, String ocena) {
        Hotel hotel = hotelService.getHotel(hotelId);
        Komentar komentarHotel = komentarService.addNewKomentar(hotel, komentar, ocena);
        model.addAttribute("komentar", komentarHotel);
        return "UnosKomentara";
    }
}
