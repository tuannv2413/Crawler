package jp.choqi.crawltool.app.response.MedicalInstitution;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SearchMIResponse {
    private Long id;
    private String code;
    private String name;
    private String post;
    private String address;
    private String phone;
    private String founder;
    private String manager;
    private String categoryName;
    private String prefectureName;
}
