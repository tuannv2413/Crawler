package jp.choqi.crawltool.domain.repository;

import jp.choqi.crawltool.domain.entities.Prefecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PrefectureRepository extends JpaRepository<Prefecture, Long>, JpaSpecificationExecutor<Prefecture> {
}
