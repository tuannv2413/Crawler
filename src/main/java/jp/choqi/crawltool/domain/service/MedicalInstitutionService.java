package jp.choqi.crawltool.domain.service;

import jp.choqi.crawltool.app.request.PathFiles;
import jp.choqi.crawltool.app.request.URLFile;
import jp.choqi.crawltool.app.result.DataApiResult;
import jp.choqi.crawltool.app.result.ListDataApiResult;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface MedicalInstitutionService {
    ResponseEntity<DataApiResult> apiInsertData(PathFiles path) throws IOException;
    ResponseEntity<DataApiResult> uploadFile(int urlId, URLFile urls);
    ResponseEntity<ListDataApiResult> searchMIByCodeOrPrefectureName(String code, int page, int limit, int status);
    ResponseEntity<DataApiResult> deleteMIByPName(int id);
    ResponseEntity<ListDataApiResult> getMIByCode(String code);
    ResponseEntity<DataApiResult> methodInsertData(int urlId);
    ResponseEntity<ListDataApiResult> getMIById(long id);
}
