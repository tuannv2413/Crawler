package jp.choqi.crawltool.app.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UrlsDto {
    private Long id;
    private String url;
}
