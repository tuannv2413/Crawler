package jp.choqi.crawltool.app.dtos;

import jp.choqi.crawltool.domain.entities.Category;
import jp.choqi.crawltool.domain.entities.Prefecture;
import jp.choqi.crawltool.domain.entities.Urls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DataExcel {

    private Prefecture prefecture;
    private Category category;
    private Urls url;
    private String col1;
    private String col2;
    private String col3;
    private String col4;
    private String col5;
    private String col6;
    private String col7;
    private String col8;
    private String col9;
}
