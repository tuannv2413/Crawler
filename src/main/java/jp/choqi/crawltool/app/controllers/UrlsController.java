package jp.choqi.crawltool.app.controllers;

import jp.choqi.crawltool.app.dtos.UrlsDto;
import jp.choqi.crawltool.app.request.UrlRequest;
import jp.choqi.crawltool.app.result.DataApiResult;
import jp.choqi.crawltool.app.result.ListDataApiResult;
import jp.choqi.crawltool.domain.service.impl.UrlsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/urls")
public class UrlsController {

    private UrlsServiceImpl urlsService;

    @Autowired
    public UrlsController(UrlsServiceImpl urlsService) {
        this.urlsService = urlsService;
    }

    @GetMapping()
    public ResponseEntity<ListDataApiResult> getListUrl(@RequestParam("page") Optional<Integer> page,
                                                        @RequestParam("limit") Optional<Integer> limit) {
        return urlsService.listURL(page.orElse(0), limit.orElse(5));
    }

    @DeleteMapping()
    public ResponseEntity<DataApiResult> deleteUrl(@RequestParam("id") int id) {
        return urlsService.deleteURL((long) id);
    }

    @PutMapping()
    public ResponseEntity<DataApiResult> updateUrl(@RequestBody UrlRequest urlRequest) {
        return urlsService.updateURL(urlRequest);
    }

    @GetMapping(value = "/url")
    public ResponseEntity<?> getUrl(@RequestParam("url") String url) {
        if (url.trim().length() > 0) {
            return urlsService.getURL(url);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DataApiResult.builder().message("URL Is Null").build());
        }
    }

    @PostMapping()
    public ResponseEntity<DataApiResult> insertUrl(@RequestBody UrlsDto urlsDto) {
        if (urlsDto.getUrl().trim().length() > 0) {
            return urlsService.insertURL(urlsDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DataApiResult.builder().message("URL Is Null").build());
        }
    }
}
