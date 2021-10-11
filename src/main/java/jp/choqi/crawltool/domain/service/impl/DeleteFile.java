package jp.choqi.crawltool.domain.service.impl;

import jp.choqi.crawltool.app.controllers.MedicalInstitutionController;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DeleteFile {

    public static final Logger logger = LoggerFactory.getLogger(MedicalInstitutionController.class);

    /**
     * delete files in folder uploads, download
     *
     * @param file
     */
    public static void deleteDir(File file) {
        try {
            logger.info("Start Delete");
            // Xóa tất cả file và thư mục trong thu mục cha, không xóa thu mục cha
             FileUtils.cleanDirectory(file);
            logger.info("End Delete");
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("Delete Error: " + e);
        }
    }
}
