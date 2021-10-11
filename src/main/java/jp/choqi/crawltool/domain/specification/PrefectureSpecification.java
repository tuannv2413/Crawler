package jp.choqi.crawltool.domain.specification;

import jp.choqi.crawltool.domain.entities.Prefecture;
import org.springframework.data.jpa.domain.Specification;

public class PrefectureSpecification {

    /**
     * Search record Prefecture by PrefectureName
     * @param name
     * @return
     */
    public static Specification<Prefecture> hasPrefectureName(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%"+name+"%");
    }
}
