package jp.choqi.crawltool.domain.repository;

import jp.choqi.crawltool.domain.entities.MedicalInstitution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MedicalInstitutionRepository extends JpaRepository<MedicalInstitution, Long>, JpaSpecificationExecutor<MedicalInstitution> {

    List<MedicalInstitution> findAllByPrefectureId(Long id);

    List<MedicalInstitution> findByCode(String code);

    MedicalInstitution getByCode(String code);

    void deleteMedicalInstitutionByPrefectureId(Long id);

    @Transactional
    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query("delete from medical_institution as mi where mi.prefecture.id=:pref_id")
    void queryDeleteMedicalInstitutionByPrefectureId(@Param("pref_id") Long prefId);

    @Transactional
    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query("delete from medical_institution as mi where mi.urls.id=:url_id")
    void queryDeleteMedicalInstitutionByUrlId(@Param("url_id") Long url_id);

    @Transactional
    @Query("select mi from medical_institution as mi where mi.prefecture.name like :prefecture_name")
    Page<MedicalInstitution> querySearchMedicalInstitutionByPrefectureName(@Param("prefecture_name") String prefecture_name, Pageable pageable);

    MedicalInstitution findFirstByUrlsId(Long id);

    @Transactional
    @Query("select mi.prefecture.name as prefectureName, mi.category.name as categoryName, mi.prefecture.region.name as regionName, mi.prefecture.region.id as regionId " +
            "from medical_institution as mi where mi.urls.id=:url_id")
    PrefectureAndCategory getPrefectureAndCategory(@Param("url_id") Long urlId);

    interface PrefectureAndCategory{
        Long getRegionId();
        String getPrefectureName();
        String getCategoryName();
        String getRegionName();
    }

}
