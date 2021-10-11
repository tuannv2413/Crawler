package jp.choqi.crawltool.domain.service.impl;

import jp.choqi.crawltool.app.controllers.MedicalInstitutionController;
import jp.choqi.crawltool.app.request.PathFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetListPathFiles {

    public static final Logger logger = LoggerFactory.getLogger(MedicalInstitutionController.class);

    /**
     * Lấy danh sách đường dẫn file (path File) trong folder download
     *
     * @param file
     */
    public PathFiles getListFile(File file) {
        logger.info("Start Get List Path File");
        String pathFolderDownLoad = file.getAbsolutePath();
        PathFiles pathFiles = new PathFiles();
        List<String> listFileName = new ArrayList<>();
        if (file.isDirectory()) {
            String[] files = file.list();
            for (String child : files) {
                File childDir = new File(file, child);
                if (childDir.isDirectory()) {
                    listFileName.addAll(getListFileFolder(childDir));
                } else if (childDir.getName().contains(".xls") || childDir.getName().contains(".xlsx")) {
                    listFileName.add(pathFolderDownLoad + File.separator + childDir.getName());
                }
            }
        }
        pathFiles.setPathList(listFileName);
        logger.info("End Get List Path File");
        return pathFiles;
    }

    /**
     * Lấy danh sách đường dẫn file (path File) trong folder con của folder download
     *
     * @param file
     */
    public List<String> getListFileFolder(File file) {
        List<String> listFileName = new ArrayList<>();
        if (file.isDirectory()) {
            String[] files = file.list();
            for (String child : files) {
                File childDir = new File(file, child);
                if (childDir.getName().contains(".xls") || childDir.getName().contains(".xlsx")) {
                    listFileName.add(file.getAbsolutePath() + File.separator + childDir.getName());
                }
            }
        }
        return listFileName;
    }

    /**
     * Lấy đường dẫn file trong folder download để upload lên S3
     *
     * @param file
     */
    public String getFileUpload(File file) {
        String fileName = "";
        if (file.isDirectory()) {
            String[] files = file.list();
            for (String child : files) {
                File childDir = new File(file, child);
                if (childDir.getName().contains(".xls") || childDir.getName().contains(".xlsx") || childDir.getName().contains(".zip")) {
                    fileName = childDir.getAbsolutePath();
                }
            }
        }
        return fileName;
    }
}
