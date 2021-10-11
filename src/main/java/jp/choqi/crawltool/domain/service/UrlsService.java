package jp.choqi.crawltool.domain.service;

import jp.choqi.crawltool.app.dtos.UrlsDto;
import jp.choqi.crawltool.app.request.UrlRequest;
import jp.choqi.crawltool.app.result.DataApiResult;
import jp.choqi.crawltool.app.result.ListDataApiResult;
import org.springframework.http.ResponseEntity;

public interface UrlsService {
    ResponseEntity<ListDataApiResult> listURL(int page, int limit);
    ResponseEntity<DataApiResult> deleteURL(Long id);
    ResponseEntity<DataApiResult> updateURL(UrlRequest urlRequest);
    ResponseEntity<ListDataApiResult> getURL(String url);
    ResponseEntity<DataApiResult> insertURL(UrlsDto urlsDto);
}
