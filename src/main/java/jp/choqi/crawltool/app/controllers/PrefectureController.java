package jp.choqi.crawltool.app.controllers;

import jp.choqi.crawltool.app.result.ListDataApiResult;
import jp.choqi.crawltool.domain.service.PrefectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/prefecture")
public class PrefectureController {

    private PrefectureService prefectureService;

    @Autowired
    public PrefectureController(PrefectureService prefectureService) {
        this.prefectureService = prefectureService;
    }

    @GetMapping()
    public ResponseEntity<ListDataApiResult> searchPrefecture(@RequestParam("name") Optional<String> name,
                                                              @RequestParam("page") Optional<Integer> page,
                                                              @RequestParam("limit") Optional<Integer> limit){
        return prefectureService.searchPrefectureByName(name.orElse(""), page.orElse(0), limit.orElse(5));
    }
}
