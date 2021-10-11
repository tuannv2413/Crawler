package jp.choqi.crawltool.domain.service.impl;

import jp.choqi.crawltool.app.response.Prefecture.SearchPrefectureResponse;
import jp.choqi.crawltool.app.result.ListDataApiResult;
import jp.choqi.crawltool.domain.entities.Prefecture;
import jp.choqi.crawltool.domain.repository.PrefectureRepository;
import jp.choqi.crawltool.domain.service.PrefectureService;
import jp.choqi.crawltool.domain.specification.PrefectureSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrefectureServiceImpl implements PrefectureService {

    private PrefectureRepository prefectureRepository;

    @Autowired
    public PrefectureServiceImpl(PrefectureRepository prefectureRepository) {
        this.prefectureRepository = prefectureRepository;
    }

    /**
     * Find record Prefecture by PrefectureName
     *
     * @param name
     * @param page
     * @param limit
     * @return
     */
    @Override
    public ResponseEntity<ListDataApiResult> searchPrefectureByName(String name, int page, int limit) {
        ListDataApiResult result = new ListDataApiResult();
        List<SearchPrefectureResponse> responses = new ArrayList<>();
        Specification specification = Specification.where(PrefectureSpecification.hasPrefectureName(name.trim()));
        Pageable pageable = PageRequest.of(page, limit);
        Page<Prefecture> prefectures = prefectureRepository.findAll(specification, pageable);
        for (Prefecture p : prefectures) {
            SearchPrefectureResponse response = SearchPrefectureResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .nameKana(p.getNameKana())
                    .build();
            responses.add(response);
        }
        result.setMessage("List Data!");
        result.setTotalItems((int) prefectures.getTotalElements());
        result.setTotalPage(prefectures.getTotalPages());
        result.setData(responses);
        return ResponseEntity.ok(result);
    }
}
