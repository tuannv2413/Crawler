package jp.choqi.crawltool.domain.repository;

import jp.choqi.crawltool.domain.entities.AdAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdAddressRepository extends JpaRepository<AdAddress, Long> {
    AdAddress findFirstByZip(String zip);
}
