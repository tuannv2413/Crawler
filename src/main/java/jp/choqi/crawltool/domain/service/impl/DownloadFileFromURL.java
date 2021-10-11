package jp.choqi.crawltool.domain.service.impl;

import jp.choqi.crawltool.app.controllers.MedicalInstitutionController;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

public class DownloadFileFromURL {

    public static final Logger logger = LoggerFactory.getLogger(MedicalInstitutionController.class);

    /**
     * Download file from url, download to folder download
     *
     * @param url
     * @throws IOException
     */
    public static Boolean download(String url, String pathFolderDownLoad){
        try {
            logger.info("Start Download");
            logger.info("Url File Download: " + url);
            String nameFIle = url.substring(url.lastIndexOf("/"), url.length());
            InputStream inputStream = new URL(url).openStream();
            FileOutputStream fileOS = new FileOutputStream(pathFolderDownLoad + "/" + nameFIle);
            logger.info("Downloaded File: " + nameFIle);
            int i = IOUtils.copy(inputStream, fileOS);
            if (i == -1) {
                IOUtils.copyLarge(inputStream, fileOS);
            }
            inputStream.close();
            fileOS.close();
            logger.info("End Download");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("Download Error: " + e);
            return false;
        }
    }

}
