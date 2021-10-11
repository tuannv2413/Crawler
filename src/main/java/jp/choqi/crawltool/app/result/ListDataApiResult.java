package jp.choqi.crawltool.app.result;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListDataApiResult extends DataApiResult{
    private Object data;
    private int totalPage;
    private int totalItems;
}
