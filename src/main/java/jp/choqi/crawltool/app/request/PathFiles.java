package jp.choqi.crawltool.app.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PathFiles {
    private List<String> pathList;
}
