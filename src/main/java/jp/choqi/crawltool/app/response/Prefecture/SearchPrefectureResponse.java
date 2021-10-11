package jp.choqi.crawltool.app.response.Prefecture;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SearchPrefectureResponse {
    private Long id;
    private String name;
    private String nameKana;
}
