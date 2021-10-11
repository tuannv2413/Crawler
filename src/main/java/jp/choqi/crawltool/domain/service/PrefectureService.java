package jp.choqi.crawltool.domain.service;

import jp.choqi.crawltool.app.result.ListDataApiResult;
import org.springframework.http.ResponseEntity;

public interface PrefectureService {
    ResponseEntity<ListDataApiResult> searchPrefectureByName(String code, int page, int limit);
}
