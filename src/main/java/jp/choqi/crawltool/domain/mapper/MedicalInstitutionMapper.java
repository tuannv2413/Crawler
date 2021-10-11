package jp.choqi.crawltool.domain.mapper;

import jp.choqi.crawltool.app.dtos.DataExcel;
import jp.choqi.crawltool.domain.entities.MedicalInstitution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MedicalInstitutionMapper {

    /**
     * Convert List DataExcel to List MedicalInstitution
     *
     * @param list
     * @return
     */
    public List<MedicalInstitution> convertToListMedicalInstitution(List<DataExcel> list) {
        List<MedicalInstitution> medicalInstitutions = new ArrayList<>();
        for (DataExcel de : list) {
            CodeAndAnnexCode codeAndAnnexCode = detachedCodeAnnexCode(de.getCol1());
            PostAndAddress postAndAddress = detachedPostAddress(de.getCol3());
            PhoneAndDoctorInfo phoneAndDoctorInfo = detachedPhoneDoctorInfo(de.getCol4());
            Datetime datetime = detachedDatetime(de.getCol7());
            MedicalInstitution medicalInstitution = MedicalInstitution.builder()
                    .code(codeAndAnnexCode.getCode())
                    .annexCode(codeAndAnnexCode.getAnnexCode())
                    .name(de.getCol2())
                    .post(postAndAddress.getPost())
                    .address(postAndAddress.getAddress())
                    .phone(phoneAndDoctorInfo.getPhone())
                    .doctorsInfo(phoneAndDoctorInfo.getDoctorInfo())
                    .founder(de.getCol5())
                    .manager(de.getCol6())
                    .datetime1(datetime.getDateTime1())
                    .type(datetime.getType())
                    .datetime2(datetime.getDateTime2())
                    .bedsDepartments(de.getCol8())
                    .note(de.getCol9())
                    .category(de.getCategory())
                    .prefecture(de.getPrefecture())
                    .urls(de.getUrl())
                    .build();
            medicalInstitutions.add(medicalInstitution);
        }
        return medicalInstitutions;
    }

    /**
     * Detached value excel to Code and AnnexCode
     *
     * @param value
     * @return
     */
    public CodeAndAnnexCode detachedCodeAnnexCode(String value) {
        CodeAndAnnexCode codeAndAnnexCode;
        if (value.contains("\n")) {
            int index = value.indexOf("\n");
            String code = value.substring(0, index);
            String annexCode = value.substring(index + 1, value.length()).replace("(", "").replace(")", "");
            codeAndAnnexCode = CodeAndAnnexCode.builder()
                    .code(code)
                    .annexCode(annexCode)
                    .build();
        } else {
            codeAndAnnexCode = CodeAndAnnexCode.builder()
                    .code(value)
                    .annexCode("")
                    .build();
        }
        return codeAndAnnexCode;
    }

    /**
     * Detached value excel to Post and Address
     *
     * @param value
     * @return
     */
    public PostAndAddress detachedPostAddress(String value) {
        PostAndAddress postAndAddress;
        if (value.contains("\n")) {
            int index = value.indexOf("\n");
            String post = value.substring(1, index).replace("－", "-");
            String address = value.substring(index + 1, value.length());
            postAndAddress = PostAndAddress.builder()
                    .post(post)
                    .address(address)
                    .build();
        } else {
            String post = value.substring(1, 9).replace("－", "-");
            String address = value.substring(9, value.length());
            postAndAddress = PostAndAddress.builder()
                    .post(post)
                    .address(address)
                    .build();
        }
        return postAndAddress;
    }

    /**
     * Detached value excel to Phone and DoctorInfo
     *
     * @param value
     * @return
     */
    public PhoneAndDoctorInfo detachedPhoneDoctorInfo(String value) {
        PhoneAndDoctorInfo phoneAndDoctorInfo;
        if (value.contains("\n")) {
            int index = value.indexOf("\n");
            String phone = value.substring(0, index).replace("－", "-");
            String doctorInfo = value.substring(index + 1, value.length());
            phoneAndDoctorInfo = PhoneAndDoctorInfo.builder()
                    .phone(phone.trim())
                    .doctorInfo(doctorInfo.trim())
                    .build();
        } else {
            phoneAndDoctorInfo = PhoneAndDoctorInfo.builder()
                    .phone(value)
                    .doctorInfo("")
                    .build();
        }
        return phoneAndDoctorInfo;
    }

    /**
     * Detached value excel to Datetime and Type
     *
     * @param value
     * @return
     */
    public Datetime detachedDatetime(String value) {
        Datetime datetime;
        if (value.contains("\n")) {
            int index = value.indexOf("\n");
            int lastIndex = value.lastIndexOf("\n");
            String datetime1 = value.substring(0, index);
            String type = value.substring(index, lastIndex);
            String datetime2 = value.substring(lastIndex + 1, value.length());
            datetime = Datetime.builder()
                    .dateTime1(datetime1)
                    .type(type)
                    .dateTime2(datetime2)
                    .build();
        } else {
            datetime = Datetime.builder()
                    .dateTime1(value)
                    .type("")
                    .dateTime2("")
                    .build();
        }
        return datetime;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    static class CodeAndAnnexCode {
        private String code;
        private String annexCode;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    static class PostAndAddress {
        private String post;
        private String address;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    static class PhoneAndDoctorInfo {
        private String phone;
        private String doctorInfo;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    static class Datetime {
        private String dateTime1;
        private String type;
        private String dateTime2;
    }
}
