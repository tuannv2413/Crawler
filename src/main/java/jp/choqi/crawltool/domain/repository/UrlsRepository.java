package jp.choqi.crawltool.domain.repository;

import jp.choqi.crawltool.domain.entities.Urls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UrlsRepository extends JpaRepository<Urls, Long> {
    Urls getByUrl(String url);

    Page<Urls> findAllByUrl(String url, Pageable pageable);

    Urls getByStatus(int status);

    @Transactional
    @Modifying
    @Query("update urls as u set u.status =:status where u.id =:id")
    void updateURL(@Param("id") long id, @Param("status") int status);

//    @Transactional
//    @Modifying
//    @Query("select u.id as id, u.url as url from urls as u")
//    List<UrlsResponse> querySelectURL();
}
