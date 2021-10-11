package jp.choqi.crawltool.app.request;

import jp.choqi.crawltool.app.dtos.UrlsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UrlRequest {
    private UrlsDto urlsDto;
    private String url;
}
