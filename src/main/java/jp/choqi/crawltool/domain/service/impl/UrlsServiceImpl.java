package jp.choqi.crawltool.domain.service.impl;

import jp.choqi.crawltool.app.dtos.UrlsDto;
import jp.choqi.crawltool.app.request.UrlRequest;
import jp.choqi.crawltool.app.response.Urls.UrlsResponse;
import jp.choqi.crawltool.app.result.DataApiResult;
import jp.choqi.crawltool.app.result.ListDataApiResult;
import jp.choqi.crawltool.domain.common.Common;
import jp.choqi.crawltool.domain.entities.MedicalInstitution;
import jp.choqi.crawltool.domain.entities.Urls;
import jp.choqi.crawltool.domain.repository.MedicalInstitutionRepository;
import jp.choqi.crawltool.domain.repository.UrlsRepository;
import jp.choqi.crawltool.domain.service.UrlsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class UrlsServiceImpl implements UrlsService {

    private UrlsRepository urlsRepository;
    private MedicalInstitutionRepository medicalInstitutionRepository;

    @Autowired
    public UrlsServiceImpl(UrlsRepository urlsRepository,MedicalInstitutionRepository medicalInstitutionRepository) {
        this.urlsRepository = urlsRepository;
        this.medicalInstitutionRepository = medicalInstitutionRepository;
    }

    /**
     * Get List Data Table urls
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<ListDataApiResult> listURL(int page, int limit) {
        ListDataApiResult result = new ListDataApiResult();
        List<UrlsResponse> listURLResponse = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, limit);
        Page<Urls> listURL = urlsRepository.findAll(pageable);
        listURL.forEach(url -> {
            UrlsResponse urlsResponse;
            MedicalInstitution medicalInstitution = medicalInstitutionRepository.findFirstByUrlsId(url.getId());
            if (medicalInstitution == null) { urlsResponse = UrlsResponse.builder()
                    .id(url.getId())
                    .url(url.getUrl())
                    .status(url.getStatus())
                    .regionName("")
                    .categoryName("")
                    .build();
            }else { urlsResponse = UrlsResponse.builder()
                    .id(url.getId())
                    .url(url.getUrl())
                    .status(url.getStatus())
                    .regionName(medicalInstitution.getPrefecture().getRegion().getName())
                    .categoryName(medicalInstitution.getPrefecture().getRegion().getId() == 8 ?
                            medicalInstitution.getPrefecture().getName() : medicalInstitution.getCategory().getName())
                    .build();
            }
            listURLResponse.add(urlsResponse);
        });
        result.setMessage("List URL!");
        result.setTotalPage(listURL.getTotalPages());
        result.setTotalItems((int) listURL.getTotalElements());
        result.setData(listURLResponse);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete record urls by id
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<DataApiResult> deleteURL(Long id) {
        if (!checkProcess()) {
            Urls urls = urlsRepository.getById(id);
            urlsRepository.delete(urls);
            return ResponseEntity.ok(DataApiResult.builder().message("削除が完了しました。").build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DataApiResult.builder().message("処理中のURLがあるので、少々お待ちください。").build());
        }
    }

    /**
     * Update record urls
     *
     * @param urlRequest
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<DataApiResult> updateURL(UrlRequest urlRequest) {
        String u = StringUtils.replaceEach(urlRequest.getUrlsDto().getUrl().trim(), new String[]{"&", "\"", "<", ">", "'"}, new String[]{"&amp;", "&quot;", "&lt;", "&gt;", "&#039;"});
        String u1 = StringUtils.replaceEach(urlRequest.getUrl().trim(), new String[]{"&", "\"", "<", ">", "'"}, new String[]{"&amp;", "&quot;", "&lt;", "&gt;", "&#039;"});
        Urls urlValid = urlsRepository.getByUrl(u);
        if (urlValid != null && u.equals(u1) == false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DataApiResult.builder().message("URLはすでに存在します。").build());
        }
        Urls urls = Urls.builder()
                .id(urlRequest.getUrlsDto().getId())
                .url(u)
                .status(Common.STATUS_NOT_RUN)
                .build();
        urlsRepository.save(urls);
        return ResponseEntity.ok(DataApiResult.builder().message("URL更新が完了しました。").build());
    }

    /**
     * Get record urls by url
     *
     * @param url
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<ListDataApiResult> getURL(String url) {
        ListDataApiResult result = new ListDataApiResult();
        List<UrlsResponse> urlsResponse = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Urls> urls = urlsRepository.findAllByUrl(url, pageable);
        urls.forEach(u -> {
            UrlsResponse urlResponse;
            MedicalInstitution medicalInstitution = medicalInstitutionRepository.findFirstByUrlsId(u.getId());
            if (medicalInstitution == null) {
                urlResponse = UrlsResponse.builder()
                        .id(u.getId())
                        .url(u.getUrl())
                        .status(u.getStatus())
                        .regionName("")
                        .categoryName("")
                        .build();
            }else {
                urlResponse = UrlsResponse.builder()
                        .id(u.getId())
                        .url(u.getUrl())
                        .status(u.getStatus())
                        .regionName(medicalInstitution.getPrefecture().getRegion().getName())
                        .categoryName(medicalInstitution.getPrefecture().getRegion().getId() == 8 ?
                                medicalInstitution.getPrefecture().getName() : medicalInstitution.getCategory().getName())
                        .build();
            }
            urlsResponse.add(urlResponse);
        });
        result.setMessage("Get URL!");
        result.setTotalPage(urls.getTotalPages());
        result.setTotalItems((int) urls.getTotalElements());
        result.setData(urlsResponse);
        return ResponseEntity.ok(result);
    }

    /**
     * New recode from table URLS
     *
     * @param urlsDto
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<DataApiResult> insertURL(UrlsDto urlsDto) {
        String u = StringUtils.replaceEach(urlsDto.getUrl().trim(), new String[]{"&", "\"", "<", ">"}, new String[]{"&amp;", "&quot;", "&lt;", "&gt;"});
        // check url
        Urls url = urlsRepository.getByUrl(u);
        if (url != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DataApiResult.builder().message("URLはすでに存在します。").build());
        }
        Urls urls = Urls.builder()
                .url(u)
                .status(Common.STATUS_NOT_RUN)
                .build();
        Urls urlResponse = urlsRepository.save(urls);
        return ResponseEntity.ok(DataApiResult.builder().message(urlResponse.getId() + "").build());
    }

    /**
     * Check Process Insert
     *
     * @return
     */
    public Boolean checkProcess() {
        Urls urls = urlsRepository.getByStatus(2);
        if (urls != null) {
            return true;
        } else {
            return false;
        }
    }

//    /**
//     * Update status urls by url_id
//     *
//     * @param id
//     * @return
//     */
//    @Transactional
//    public ResponseEntity<DataApiResult> updateStatusURL(int id) {
//        urlsRepository.updateURL((long) id);
//        return ResponseEntity.ok(DataApiResult.builder().message("Update Status Successfully!").build());
//    }
}
