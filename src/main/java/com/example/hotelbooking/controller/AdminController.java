package com.example.hotelbooking.controller;

import com.example.hotelbooking.model.*;
import com.example.hotelbooking.service.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.bouncycastle.math.raw.Mod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/")
@RequiredArgsConstructor
public class AdminController {

    private final RezervacijaService rezervacijaService;
    private final DrzavaService drzavaService;
    private final GradService gradService;
    private final HotelService hotelService;
    private final SmestajService smestajService;

    @GetMapping("/hello")
    public String hello(){
        return "admin_dashboard";
    }

    @GetMapping("/rezervacija-all")
    public String getRezervacije(Model model) {
        List<Rezervacija> rezervacije = rezervacijaService.getAllRezervacije();
        model.addAttribute("rezervacije", rezervacije);
        return "PrikazRezervacija";
    }

    @GetMapping("/drzava-all-delete")
    public String pregledDrzava(Model model) {
        List<Drzava> drzave = drzavaService.getAllDrzave();
        model.addAttribute("drzave", drzave);
        return "PregledDrzava";
    }

    @GetMapping("/drzava-delete")
    public String obrisiDrzavu(Long drzavaId) {
        Drzava drzava = drzavaService.getDrzava(drzavaId);
        drzavaService.deleteDrzava(drzava.getNaziv());
        return "UspesnoBrisanjeDrzave";
    }

    @GetMapping("/rezervacija-delete")
    public String deleteRezervacija(Model model, Long rezervacijaId) {
        Rezervacija rezervacija = rezervacijaService.getRezervacija(rezervacijaId);
        model.addAttribute("rezervacija", rezervacija);
        rezervacijaService.deleteRezervacija(rezervacija);
        return "UspesnoBrisanje";
    }

    @GetMapping("/drzava-all-unos")
    public String getDrzaveAll(Model model) {
        List<Drzava> drzave = drzavaService.getAllDrzave();
        model.addAttribute("drzave", drzave);
        return "UnosHotela";
    }

    @GetMapping("/hotel-add")
    public String addHotel(Model m, String naziv, String zvezdica, String datumIzgradnje,
                           String usluga, String ishrana, String imageUrl, Long gradId) throws ParseException {
        Grad grad = gradService.getGrad(gradId);
        Hotel newHotel = hotelService.addNewHotel(naziv, zvezdica, datumIzgradnje, usluga, ishrana, imageUrl, grad);
        if (newHotel != null) {
            m.addAttribute("hotel", newHotel);
            return "UspesnoDodavanjeHotela";
        } else {
            return "NeuspesnoDodavanjeHotela";
        }
    }

    @GetMapping("/grad-all-unos")
    public String getGradoviUnos(Model m, Long drzavaId) {
        Drzava drzava = drzavaService.getDrzava(drzavaId);
        List<Grad> gradovi = gradService.getAllGradovi(drzava);
        m.addAttribute("gradovi", gradovi);
        m.addAttribute("drzava", drzava);
        return "UnosHotela";
    }

    @GetMapping("/hotel-confirm")
    public String unosConfirm(Model m, Long gradId) {
        Grad grad = gradService.getGrad(gradId);
        List<Zvezdica> zvezdice = List.of(Zvezdica.values());
        List<Ishrana> ishrane = List.of(Ishrana.values());
        List<Usluga> usluge = List.of(Usluga.values());
        m.addAttribute("grad", grad);
        m.addAttribute("zvezdice", zvezdice);
        m.addAttribute("ishrane", ishrane);
        m.addAttribute("usluge", usluge);
        return "UnosHotela";
    }

    @GetMapping("/hotel-all-pdf")
    public void generateReport(HttpServletResponse response) throws Exception {
        response.setContentType("text/html");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(hotelService.getAll());
        InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/SviHoteli.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        Map<String, Object> params = new HashMap<>();
        params.put("imeProjekta", "Hotel-Booking");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        response.setContentType("application/x-download");
        response.addHeader("Content-disposition", "attachment; filename=SviHoteli.pdf");
        OutputStream outputStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }

    @GetMapping("/hotel-all-generate")
    public String getHoteliReport(Model model) {
        List<Hotel> hoteli = hotelService.getAll();
        model.addAttribute("hoteli", hoteli);
        return "GenerisiRezervacije";
    }

    @GetMapping("/rezervacija-all-pdf")
    public void generateReportRezervacije(HttpServletResponse response, Long hotelId,
                                          String datumOd, String datumDo) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date datOd = sdf.parse(datumOd);
        Date datDo = sdf.parse(datumDo);
        Hotel hotel = hotelService.getHotel(hotelId);
        List<Smestaj> smestaji = smestajService.getSmestaji(hotel);
        List<Rezervacija> rezervacije = rezervacijaService.getRezervacije(smestaji, datOd, datDo);
        response.setContentType("text/html");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rezervacije);
        InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/SveRezervacije.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        Map<String, Object> params = new HashMap<>();
        params.put("imeProjekta", "Hotel-Booking");
        params.put("datumOd", datOd);
        params.put("datumDo", datDo);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        response.setContentType("application/x-download");
        response.addHeader("Content-disposition", "attachment; filename=SveRezervacije.pdf");
        OutputStream outputStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }

    // Updating
    @GetMapping("/grad-all-update")
    public String getGradAllUpdate(Model model) {
        List<Grad> gradovi = gradService.getALl();
        model.addAttribute("gradovi", gradovi);
        return "PregledGradova";
    }

    @GetMapping("/grad-update-form")
    public String gradUpdate(Model model, Long gradId, Integer povrsina, Integer brojStanovnika) {
        Grad grad = gradService.getGrad(gradId);
        grad = gradService.updateGrad(grad, povrsina, brojStanovnika);
        model.addAttribute("grad", grad);
        return "PregledGradova";
    }

    @PutMapping("/drzava-update")
    public ResponseEntity<Drzava> updateDrzava(@RequestBody Drzava drzava) {
        Drzava updatedDrzava = drzavaService.updateDrzava(drzava);
        return new ResponseEntity<>(updatedDrzava, HttpStatus.OK);
    }

    @PutMapping("/grad-update")
    public ResponseEntity<Grad> updateGrad(@RequestBody Grad grad) {
        Grad updatedGrad = gradService.updateGrad(grad);
        return new ResponseEntity<>(updatedGrad, HttpStatus.OK);
    }

    @PutMapping("/hotel-update")
    public ResponseEntity<Hotel> updateHotel(@RequestBody Hotel hotel) {
        Hotel updatedHotel = hotelService.updateHotel(hotel);
        return new ResponseEntity<>(updatedHotel, HttpStatus.OK);
    }

    // Deleting
    @DeleteMapping("/drzava-delete/{naziv}")
    public ResponseEntity<Drzava> deleteDrzava(@PathVariable("naziv") String naziv) {
        drzavaService.deleteDrzava(naziv);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/grad-delete/{naziv}")
    public ResponseEntity<Grad> deleteGrad(@PathVariable("naziv") String naziv) {
        gradService.deleteGrad(naziv);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/hotel-delete/{naziv}")
    public ResponseEntity<Hotel> deleteHotel(@PathVariable("naziv") String naziv) {
        hotelService.deleteHotel(naziv);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
