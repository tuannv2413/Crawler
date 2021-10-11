package jp.choqi.crawltool.domain.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import jdk.nashorn.internal.runtime.options.Option;
import jp.choqi.crawltool.app.controllers.MedicalInstitutionController;
import jp.choqi.crawltool.app.dtos.DataExcel;
import jp.choqi.crawltool.app.request.PathFiles;
import jp.choqi.crawltool.app.request.URLFile;
import jp.choqi.crawltool.app.response.MedicalInstitution.MedicalInstitutionDetail;
import jp.choqi.crawltool.app.response.MedicalInstitution.SearchMIResponse;
import jp.choqi.crawltool.app.result.DataApiResult;
import jp.choqi.crawltool.app.result.ListDataApiResult;
import jp.choqi.crawltool.domain.common.Common;
import jp.choqi.crawltool.domain.entities.Category;
import jp.choqi.crawltool.domain.entities.MedicalInstitution;
import jp.choqi.crawltool.domain.entities.Urls;
import jp.choqi.crawltool.domain.mapper.MedicalInstitutionMapper;
import jp.choqi.crawltool.domain.repository.*;
import jp.choqi.crawltool.domain.service.MedicalInstitutionService;
import jp.choqi.crawltool.domain.specification.MedicalInstitutionSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalInstitutionServiceImpl implements MedicalInstitutionService {

    private MedicalInstitutionRepository medicalInstitutionRepository;
    private CategoryRepository categoryRepository;
    private AdAddressRepository adAddressRepository;
    private PrefectureRepository prefectureRepository;
    private UrlsRepository urlsRepository;

    public static final Logger logger = LoggerFactory.getLogger(MedicalInstitutionController.class);

    private AmazonS3 s3client;

    @Value("${bucketName}")
    private String bucketName;

    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${pathFolderDownLoad}")
    private String pathFolderDownLoad;

    @Autowired
    public MedicalInstitutionServiceImpl(MedicalInstitutionRepository medicalInstitutionRepository, CategoryRepository categoryRepository, AdAddressRepository adAddressRepository, PrefectureRepository prefectureRepository, UrlsRepository urlsRepository) {
        try {
            this.medicalInstitutionRepository = medicalInstitutionRepository;
            this.categoryRepository = categoryRepository;
            this.adAddressRepository = adAddressRepository;
            this.prefectureRepository = prefectureRepository;
            this.urlsRepository = urlsRepository;
        } finally {
            List<Category> list = categoryRepository.findAll();
            list.forEach(c -> Common.categoryHashMap.put(c.getName(), c));
        }
    }

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    /**
     * Insert data form file excel to database
     *
     * @param path
     * @throws IOException
     */
    @Override
    @Transactional
    public ResponseEntity<DataApiResult> apiInsertData(PathFiles path) {
        try {
            int urlId = 0;
            logger.info("Start Insert Data");
            MedicalInstitutionMapper mapper = new MedicalInstitutionMapper();
            DataApiResult result;
            ReadFileExcel readFileExcel = new ReadFileExcel(adAddressRepository, urlsRepository);
            List<DataExcel> list = readFileExcel.readExcel(path, urlId);
            if (list.size() == 0) {
                result = DataApiResult.builder()
                        .message("アップロードされたファイルはサポートされていません。")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            List<MedicalInstitution> medicalInstitutions = mapper.convertToListMedicalInstitution(list);
            logger.info("Process Insert");
            medicalInstitutionRepository.saveAll(medicalInstitutions);
            logger.info("Insert Successfully");
            result = DataApiResult.builder()
                    .message("保存が完了しました。")
                    .build();
            logger.info("End Insert Data");
            return new ResponseEntity<DataApiResult>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Insert Data Error: " + e);
            DataApiResult result = DataApiResult.builder()
                    .message("保存ができませんでした。")
                    .build();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(result);
        }
    }

    /**
     * @param urls
     * @return
     */
    @Override
    public ResponseEntity<DataApiResult> uploadFile(int urlId, URLFile urls) {
        if (!checkProcess()) {
            logger.info("Start Process");
            urlsRepository.updateURL((long) urlId, Common.STATUS_PROCESS);
            try {
                logger.info("Start Delete MedicalInstitution");
                logger.info("Process Delete MedicalInstitution By url_id = :" + urlId);
                medicalInstitutionRepository.queryDeleteMedicalInstitutionByUrlId((long) urlId);
                logger.info("End Delete MedicalInstitution");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error Delete MedicalInstitution");
                DataApiResult result = DataApiResult.builder()
                        .message("医療機関データの削除が失敗しました。")
                        .build();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(result);
            }
            File fileFolderDownload = new File(pathFolderDownLoad);
            PathFiles pathFiles = new PathFiles();
            GetListPathFiles getListPathFiles = new GetListPathFiles();
            DeleteFile.deleteDir(fileFolderDownload);
            // Download file
            for (String url : urls.getUrls()) {
                Boolean checkDownload = DownloadFileFromURL.download(url, pathFolderDownLoad);
                if (!checkDownload) {
                    urlsRepository.updateURL((long) urlId, Common.STATUS_FALSE);
                    DataApiResult result = DataApiResult.builder()
                            .message("ダウンロードが失敗しました。")
                            .build();
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(result);
                };
            }
            // Get path file upload to s3
            String fileName = getListPathFiles.getFileUpload(fileFolderDownload);
            File file = new File(fileName);
            String fileNameUpload = new Date().getTime() + "-" + file.getName().replace(" ", "_");
            try {
                logger.info("Start Upload File To S3");
                logger.info("File Name: " + fileNameUpload);
//                s3client.putObject(new PutObjectRequest(bucketName, fileNameUpload, file)
//                        .withCannedAcl(CannedAccessControlList.Private));
                logger.info("End Upload File To S3");
            } catch (Exception e) {
                urlsRepository.updateURL((long) urlId, Common.STATUS_FALSE);
                e.printStackTrace();
                logger.error("Upload File To S3 Error: " + e);
                DataApiResult result = DataApiResult.builder()
                        .message("ファイルをS3へのアップロードが失敗しました。")
                        .build();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(result);
            }
            DataApiResult result = DataApiResult.builder().message("URL更新が完了しました。").response(urlId+"").build();
            if (file.getName().contains(".zip")) {
                UnzipUtility unzipUtility = new UnzipUtility();
                try {
                    unzipUtility.unzip(pathFolderDownLoad + File.separator + file.getName(), pathFolderDownLoad);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            urlsRepository.updateURL((long) urlId, Common.STATUS_FALSE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DataApiResult.builder().message("処理中のURLがあるので、少々お待ちください。").build());
        }
    }

    /**
     * Insert data form file excel to database
     *
     * @throws IOException
     */
    @Transactional
    public ResponseEntity<DataApiResult> methodInsertData(int urlId) {
        try {
            logger.info("Check Running Process");
            logger.info("Start Insert Data");
            DataApiResult result;
            PathFiles pathFiles = new PathFiles();
            GetListPathFiles getListPathFiles = new GetListPathFiles();
            pathFiles = getListPathFiles.getListFile(new File(pathFolderDownLoad));
            MedicalInstitutionMapper mapper = new MedicalInstitutionMapper();
            ReadFileExcel readFileExcel = new ReadFileExcel(adAddressRepository, urlsRepository);
            List<DataExcel> list = readFileExcel.readExcel(pathFiles, urlId);
            if (list.size() > 0) {
                List<MedicalInstitution> medicalInstitutions = mapper.convertToListMedicalInstitution(list);
                logger.info("Process Insert");
                medicalInstitutionRepository.saveAll(medicalInstitutions);
                logger.info("Insert Successfully!");
                result = DataApiResult.builder()
                        .message("保存が完了しました。")
                        .build();
                logger.info("End Insert Data");
                logger.info("Update Status Url By url_id = " + urlId);
                urlsRepository.updateURL((long) urlId, Common.STATUS_SUCCESS);
                logger.info("End Update Status");
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            } else {
                urlsRepository.updateURL((long) urlId, Common.STATUS_FALSE);
                logger.info("No File Excel In Folder download");
                result = DataApiResult.builder()
                        .message("ダウンロードされたフォルダにはエクセルファイルがありません。")
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (Exception e) {
            urlsRepository.updateURL((long) urlId, Common.STATUS_FALSE);
            e.printStackTrace();
            logger.error("Error Insert Data: " + e);
            return (ResponseEntity<DataApiResult>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Override
    public ResponseEntity<ListDataApiResult> getMIById(long id) {
        ListDataApiResult listDataApiResult = new ListDataApiResult();
        Optional<MedicalInstitution> medicalInstitution = medicalInstitutionRepository.findById(id);
        if (medicalInstitution.isPresent()){
            MedicalInstitutionDetail medicalInstitutionDetail = MedicalInstitutionDetail.builder()
                    .id(medicalInstitution.get().getId())
                    .code(medicalInstitution.get().getCode())
                    .name(medicalInstitution.get().getName())
                    .post(medicalInstitution.get().getPost())
                    .phone(medicalInstitution.get().getPhone())
                    .address(medicalInstitution.get().getAddress())
                    .founder(medicalInstitution.get().getFounder())
                    .manager(medicalInstitution.get().getManager())
                    .doctorInfo(medicalInstitution.get().getDoctorsInfo())
                    .bedsDepartments(medicalInstitution.get().getBedsDepartments())
                    .categoryName(medicalInstitution.get().getCategory().getName())
                    .prefectureName(medicalInstitution.get().getPrefecture().getName())
                    .regionName(medicalInstitution.get().getPrefecture().getRegion().getName())
                    .time(medicalInstitution.get().getDatetime1()
                            + (medicalInstitution.get().getType() != null ? medicalInstitution.get().getType() + "\n" : "") + medicalInstitution.get().getDatetime2())
                    .note(medicalInstitution.get().getNote())
                    .build();
            listDataApiResult.setData(medicalInstitutionDetail);
            listDataApiResult.setMessage("MedicalInstitution ID="+id);
            listDataApiResult.setTotalItems(1);
            listDataApiResult.setTotalPage(1);
            return ResponseEntity.ok(listDataApiResult);
        }else {
            listDataApiResult.setData(null);
            listDataApiResult.setMessage("MedicalInstitution Is Valid");
            listDataApiResult.setTotalItems(0);
            listDataApiResult.setTotalPage(0);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listDataApiResult);
        }
    }

    /**
     * Find record MedicalInstitution by code or prefectureName
     *
     * @param request
     * @param page
     * @param limit
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<ListDataApiResult> searchMIByCodeOrPrefectureName(String request, int page, int limit, int status) {
        ListDataApiResult result = new ListDataApiResult();
        List<SearchMIResponse> responses = new ArrayList<>();
        Specification conditions;
        if (status == 0) {
            conditions = Specification.where((MedicalInstitutionSpecification.hasPrefectureName(request.trim())));
        } else {
            conditions = Specification.where((MedicalInstitutionSpecification.hasMedicalInstitutionCode(request.trim())));
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<MedicalInstitution> medicalInstitutions = medicalInstitutionRepository.findAll(conditions, pageable);
        for (MedicalInstitution m : medicalInstitutions) {
            SearchMIResponse response = SearchMIResponse.builder()
                    .id(m.getId())
                    .code(m.getCode())
                    .name(m.getName())
                    .post(m.getPost())
                    .address(m.getAddress())
                    .phone(m.getPhone())
                    .founder(m.getFounder())
                    .manager(m.getManager())
                    .categoryName(m.getCategory().getName())
                    .prefectureName(m.getPrefecture().getName())
                    .build();
            responses.add(response);
        }
        result.setMessage("List Data!");
        result.setTotalItems((int) medicalInstitutions.getTotalElements());
        result.setTotalPage(medicalInstitutions.getTotalPages());
        result.setData(responses);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete record MedicalInstitution by PrefectureId
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<DataApiResult> deleteMIByPName(int id) {
        try {
            logger.info("Start Delete MedicalInstitution");
            logger.info("Delete MedicalInstitution By prefecture_id: " + id);
            DataApiResult result;
//            medicalInstitutionRepository.deleteMedicalInstitutionByPrefectureId((long) id);
            medicalInstitutionRepository.queryDeleteMedicalInstitutionByPrefectureId((long) id);
            result = DataApiResult.builder()
                    .message("削除が完了しました。")
                    .build();
            logger.info("End Delete MedicalInstitution");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Delete MedicalInstitution Error: " + e);
            DataApiResult result;
            result = DataApiResult.builder()
                    .message("削除ができませんでした。")
                    .build();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(result);
        }
    }

    /**
     * Get record MedicalInstitution by MedicalInstitutionCode
     *
     * @param code
     * @return
     */
    @Override
    public ResponseEntity<ListDataApiResult> getMIByCode(String code) {
        ListDataApiResult result = new ListDataApiResult();
        List<SearchMIResponse> responses = new ArrayList<>();
        List<MedicalInstitution> medicalInstitutions = medicalInstitutionRepository.findByCode(code.trim());
        for (MedicalInstitution m : medicalInstitutions) {
            SearchMIResponse response = SearchMIResponse.builder()
                    .id(m.getId())
                    .code(m.getCode())
                    .name(m.getName())
                    .post(m.getPost())
                    .address(m.getAddress())
                    .phone(m.getPhone())
                    .founder(m.getFounder())
                    .manager(m.getManager())
                    .categoryName(m.getCategory().getName())
                    .prefectureName(m.getPrefecture().getName())
                    .build();
            responses.add(response);
        }
        result.setMessage("List Data!");
        result.setTotalItems(medicalInstitutions.size());
        result.setTotalPage(0);
        result.setData(responses);
        return ResponseEntity.ok(result);
    }

    /**
     * Check Process Insert
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

}
