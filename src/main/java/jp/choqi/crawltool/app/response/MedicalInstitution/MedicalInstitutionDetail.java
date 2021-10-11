package jp.choqi.crawltool.app.response.MedicalInstitution;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MedicalInstitutionDetail {
    private Long id;
    private String code;
    private String name;
    private String post;
    private String address;
    private String phone;
    private String founder;
    private String manager;
    private String doctorInfo;
    private String bedsDepartments;
    private String categoryName;
    private String prefectureName;
    private String regionName;
    private String time;
    private String note;
}
