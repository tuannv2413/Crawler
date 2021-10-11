package jp.choqi.crawltool.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin
public class WebController {

//    @GetMapping(value = { "/", "/prefecture"}) // Nếu người dùng request tới địa chỉ "/" hoặc "/prefecture"
//    public String Prefecture() {
//        return "Prefecture"; // Trả về file Prefecture.html
//    }
    
    @GetMapping(value = { "/", "/medicalInstitution"}) // Nếu người dùng request tới địa chỉ "/medicalInstitution"
    public String MedicalInstitution() {
        return "MedicalInstitution"; // Trả về file MedicalInstitution.html
    }

    @GetMapping(value = "/urls") // Nếu người dùng request tới địa chỉ "/urls"
    public String TableURL() {
        return "Urls"; // Trả về file Urls.html
    }

    @GetMapping(value = "/medicalInstitutionDetail") // Nếu người dùng request tới địa chỉ "/medicalInstitution/{id}"
    public String MedicalInstitutionDetail() {
        return "MedicalInstitutionDetail"; // Trả về file MedicalInstitutionDetail.html
    }

}
