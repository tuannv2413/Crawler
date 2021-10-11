package jp.choqi.crawltool.domain.exception;

import jp.choqi.crawltool.app.result.DataApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<DataApiResult> handleUploadException(MaxUploadSizeExceededException exc) {
        DataApiResult result = DataApiResult.builder()
                .message("アップロードができませんでした。")
                .build();
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(result);
    }
}
