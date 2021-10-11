package jp.choqi.crawltool.app.controllers;

import jp.choqi.crawltool.app.request.PathFiles;
import jp.choqi.crawltool.app.request.URLFile;
import jp.choqi.crawltool.app.result.DataApiResult;
import jp.choqi.crawltool.app.result.ListDataApiResult;
import jp.choqi.crawltool.domain.service.MedicalInstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/medicalInstitution")
public class MedicalInstitutionController {

    private MedicalInstitutionService medicalInstitutionService;

    @Autowired
    public MedicalInstitutionController(MedicalInstitutionService medicalInstitutionService) {
        this.medicalInstitutionService = medicalInstitutionService;
    }

    @PostMapping()
    public ResponseEntity<DataApiResult> save(@RequestBody PathFiles path) throws IOException {
        return medicalInstitutionService.apiInsertData(path);
    }

    @PostMapping(value = "uploads/{urlId}")
    public ResponseEntity<DataApiResult> uploads(@PathVariable int urlId, @RequestBody URLFile urls) {
        if (urls.getUrls().size() > 0) {
            return medicalInstitutionService.uploadFile(urlId, urls);
        }else {
            DataApiResult result = DataApiResult.builder()
                    .message("File is null. Upload failed!")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping(value = "/search")
    public ResponseEntity<ListDataApiResult> searchByCode(@RequestParam("request") Optional<String> request,
                                                          @RequestParam("page") Optional<Integer> page,
                                                          @RequestParam("limit") Optional<Integer> limit,
                                                          @RequestParam("status") Optional<Integer> status) {
        return medicalInstitutionService.searchMIByCodeOrPrefectureName(request.orElse(""), page.orElse(0), limit.orElse(5), status.orElse(0));
    }

    @GetMapping()
    public ResponseEntity<ListDataApiResult> getByCode(@RequestParam("code") String code) {
        ListDataApiResult result = new ListDataApiResult();
        if (code.trim().length() > 0) {
            return medicalInstitutionService.getMIByCode(code.trim());
        } else {
            result.setMessage("Code invalid!");
            result.setTotalItems(0);
            result.setTotalPage(0);
            result.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @DeleteMapping()
    public ResponseEntity<DataApiResult> deleteMIByPId(@RequestParam("id") int id) {
        return medicalInstitutionService.deleteMIByPName(id);
    }

    @GetMapping(value = "/new")
    public ResponseEntity<DataApiResult> newRecord(@RequestParam("urlId") int urlId) {
        return medicalInstitutionService.methodInsertData(urlId);
    }

    @GetMapping(value = "/detail")
    public ResponseEntity<ListDataApiResult> getMIById(@RequestParam("id") int id) {
        return medicalInstitutionService.getMIById((long) id);
    }

}
