package jp.choqi.crawltool.app.response.Urls;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UrlsResponse {
    private Long id;
    private String url;
    private Integer status;
    private String regionName;
    private String categoryName;
}
