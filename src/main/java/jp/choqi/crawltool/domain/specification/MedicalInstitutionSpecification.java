package jp.choqi.crawltool.domain.specification;

import jp.choqi.crawltool.domain.entities.MedicalInstitution;
import org.springframework.data.jpa.domain.Specification;

public class MedicalInstitutionSpecification {

    /**
     * Find record MedicalInstitution by MedicalInstitutionName
     * @param name
     * @return
     */
    public static Specification<MedicalInstitution> hasMedicalInstitutionName(String name) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%"+name+"%"));
    }

    /**
     * Get record MedicalInstitution by MedicalInstitutionCode
     * @param code
     * @return
     */
    public static Specification<MedicalInstitution> hasMedicalInstitutionCode(String code) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("code"), "%"+code+"%"));
    }

    /**
     * Find record MedicalInstitution by prefectureName
     * @param prefectureName
     * @return
     */
    public static Specification<MedicalInstitution> hasPrefectureName(String prefectureName) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("prefecture").get("name"), "%"+prefectureName+"%"));
    }
}
