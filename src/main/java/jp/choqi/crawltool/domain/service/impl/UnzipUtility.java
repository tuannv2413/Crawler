package jp.choqi.crawltool.domain.service.impl;


import jp.choqi.crawltool.app.controllers.MedicalInstitutionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtility {

    public static final Logger logger = LoggerFactory.getLogger(MedicalInstitutionController.class);

    private static final int BUFFER_SIZE = 4096;

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     *
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(String zipFilePath, String destDirectory) {
        try {
            logger.info("Start Unzip File");
            logger.info("Path File Unzip: " + zipFilePath);
            File destDir = new File(destDirectory);
            // Nếu không có thư mục thì tạo thư mục mới
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            FileInputStream fis = new FileInputStream(zipFilePath);
            // Đọc file zip. 1 số file có tiếng Trung lên phải thêm Charset.forName("GBK")
            ZipInputStream zipIn = new ZipInputStream(fis, Charset.forName("GBK"));
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.closeEntry();
            zipIn.close();
            fis.close();
            logger.info("End Unzip File");
        }catch (Exception e) {
            logger.error("Unzip File Error: " + e);
        }
    }


    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) {
        try {
            logger.info("Start Extract File");
            FileOutputStream fis = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fis);
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            logger.info("Read File");
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
            fis.close();
            logger.info("End Extract File");
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("Extract File Error: " + e);
        }
    }
}
