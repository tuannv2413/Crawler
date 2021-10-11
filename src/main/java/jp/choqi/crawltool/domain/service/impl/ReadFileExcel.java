package jp.choqi.crawltool.domain.service.impl;

import jp.choqi.crawltool.app.controllers.MedicalInstitutionController;
import jp.choqi.crawltool.app.dtos.DataExcel;
import jp.choqi.crawltool.app.request.PathFiles;
import jp.choqi.crawltool.domain.common.Common;
import jp.choqi.crawltool.domain.entities.AdAddress;
import jp.choqi.crawltool.domain.entities.Category;
import jp.choqi.crawltool.domain.entities.Prefecture;
import jp.choqi.crawltool.domain.entities.Urls;
import jp.choqi.crawltool.domain.repository.AdAddressRepository;
import jp.choqi.crawltool.domain.repository.UrlsRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadFileExcel {
    public static final int COLUMN_0 = 0;
    public static final int COLUMN_1 = 1;
    public static final int COLUMN_2 = 2;
    public static final int COLUMN_3 = 3;
    public static final int COLUMN_4 = 4;
    public static final int COLUMN_5 = 5;
    public static final int COLUMN_6 = 6;
    public static final int COLUMN_7 = 7;
    public static final int COLUMN_8 = 8;
    public static final int COLUMN_9 = 9;

    public static final Logger logger = LoggerFactory.getLogger(MedicalInstitutionController.class);

    private AdAddressRepository adAddressRepository;
    private UrlsRepository urlsRepository;

    @Autowired
    public ReadFileExcel(AdAddressRepository adAddressRepository, UrlsRepository urlsRepository) {
        this.adAddressRepository = adAddressRepository;
        this.urlsRepository = urlsRepository;
    }

    /**
     * Đọc file excel trong folder download
     *
     * @param path
     * @return
     */
    @Transactional
    public List<DataExcel> readExcel(PathFiles path, int urlId) {
        logger.info("Start Read Files Excel");
        String prefectureCode ="";
        String categoryCode ="";
        // lấy object url theo urlId
        Urls url = urlsRepository.getById((long) urlId);
        List<DataExcel> listData = new ArrayList<>();
        List<DataExcel> listDataExcel = new ArrayList<>();
        List<DataExcel> listResult = new ArrayList<>();
        try {
            for (String pathName : path.getPathList()) {
                logger.info("Path File Read: " + pathName);
                // Get file
                InputStream inputStream = new FileInputStream(new File(pathName));
                // Get workbook
                Workbook workbook = getWorkbook(inputStream, pathName);

                // Get sheet
                Sheet sheet = workbook.getSheetAt(0);

                // Get all rows
                Iterator<Row> iterator = sheet.iterator();
                int i = -1;
                String value1 = "";
                String value2 = "";
                String value3 = "";
                String value4 = "";
                String value5 = "";
                String value6 = "";
                String value7 = "";
                String value8 = "";
                String value9 = "";
                Category category = new Category();
                Prefecture prefecture = new Prefecture();
                // Kiểm tra xem có phải là record đầu tiên không;
                Boolean checkRecord1 = false;
                while (iterator.hasNext()) {
                    Row nextRow = iterator.next();
                    i++;
                    if (nextRow.getRowNum() == 0 || nextRow.getRowNum() == 5) {
                        // Ignore header
                        continue;
                    }
                    // Get all cells
                    Iterator<Cell> cellIterator = nextRow.cellIterator();

                    // Read cells and set value for book object
                    DataExcel dataExcel = new DataExcel();
                    while (cellIterator.hasNext()) {
                        // Read cell
                        Cell cell = cellIterator.next();
                        Object cellValue = getCellValue(cell);
                        // Set value for book object
                        int columnIndex = cell.getColumnIndex();
                        switch (columnIndex) {
                            case COLUMN_0:
                                if (i == 3) {
//                                    String categoryName = (cell).toString().substring(15, 17);
                                    String categoryName = getCategoryName((cell).toString());
                                    try {
                                        // lấy object Category theo categoryName
                                        logger.info("Get Category");
                                        category = Common.categoryHashMap.get(categoryName);
                                        categoryCode = getCategoryCode(category.getId());
                                        logger.info("Category ID: " + category.getId());
                                    }catch (Exception e) {
                                        e.printStackTrace();
                                        logger.info("Error Get Category");
                                    }
                                }
                                // Nếu i (số hàng) khác row 1, row 3 và có giá trị thì tạo 1 instance dataExcel
                                if (i != 1 && i != 3 && getCellValue(cell).toString().length() > 0) {
                                    if (!getCellValue(cell).toString().equals("1")) {
                                        dataExcel.setCategory(category);
                                        dataExcel.setPrefecture(prefecture);
                                        dataExcel.setUrl(url);
                                        dataExcel.setCol1(prefectureCode + categoryCode + convertCode(value1.trim()));
                                        dataExcel.setCol2(value2.trim());
                                        dataExcel.setCol3(value3.trim());
                                        dataExcel.setCol4(value4);
                                        dataExcel.setCol5(value5.trim());
                                        dataExcel.setCol6(value6.trim());
                                        dataExcel.setCol7(value7.trim());
                                        dataExcel.setCol8(value8.trim());
                                        dataExcel.setCol9(value9.trim());
                                        listData.add(dataExcel);
                                        value1 = "";
                                        value2 = "";
                                        value3 = "";
                                        value4 = "";
                                        value5 = "";
                                        value6 = "";
                                        value7 = "";
                                        value8 = "";
                                        value9 = "";
                                    }else{
                                        checkRecord1 = true;
                                    }
                                }else {
                                    checkRecord1 = false;
                                }
                                break;
                            case COLUMN_1:
                                if (i != 1 && i != 3 && getCellValue(cell).toString().length() > 0) {
                                    value1 += getCellValue(cell).toString() + "\n";
                                }
                                break;
                            case COLUMN_2:
                                if (getCellValue(cell).toString().length() > 0) {
                                    value2 += getCellValue(cell).toString() + "\n";
                                }
                                break;
                            case COLUMN_3:
                                if (getCellValue(cell).toString().length() > 0) {
                                    value3 += getCellValue(cell).toString() + "\n";
                                    if (i == 10) {
                                        String prefName = getCellValue(cell).toString().substring(1, 9);
                                        try {
                                            // Lấy object Prefecture theo ZipCode trong file excel
                                            logger.info("Find Prefecture");
                                            AdAddress adAddress = adAddressRepository.findFirstByZip(prefName.replace("－", "-"));
                                            prefecture = adAddress.getPrefecture();
                                            if (prefecture.getId() < 10l) {
                                                prefectureCode = "0"+prefecture.getId();
                                            }else {
                                                prefectureCode = prefecture.getId()+"";
                                            }
                                            logger.info("Prefecture ID: " + prefecture.getId());
                                        }catch (Exception e) {
                                            logger.error("Error Find Prefecture");
                                            e.printStackTrace();
                                            return listResult;
                                        }
                                    }
                                }
                                break;
                            case COLUMN_4:
//                                if (getCellValue(cell).toString().length() > 0) {
//                                    value4 += getCellValue(cell).toString() + "\n";
//                                }
                                if (checkRecord1) {
                                    value4 = getCellValue(cell).toString() + "\n";
                                }else {
                                    value4 += getCellValue(cell).toString() + "\n";
                                }
                                break;
                            case COLUMN_5:
                                if (getCellValue(cell).toString().length() > 0) {
                                    value5 += getCellValue(cell).toString() + "\n";
                                }
                                break;
                            case COLUMN_6:
                                if (getCellValue(cell).toString().length() > 0) {
                                    value6 += getCellValue(cell).toString() + "\n";
                                }
                                break;
                            case COLUMN_7:
                                if (getCellValue(cell).toString().length() > 0) {
                                    value7 += getCellValue(cell).toString() + "\n";
                                }
                                break;
                            case COLUMN_8:
                                if (getCellValue(cell).toString().length() > 0) {
                                    value8 += getCellValue(cell).toString() + "\n";
                                }
                                break;
                            case COLUMN_9:
                                if (getCellValue(cell).toString().length() > 0) {
                                    value9 += getCellValue(cell).toString() + "\n";
                                }
                                break;
                            default:
                                break;
                        }

                    }
                }
                // Lấy dataExcel bản ghi cuối cùng trong file excel
                DataExcel dataExcel = new DataExcel();
                dataExcel.setCategory(category);
                dataExcel.setPrefecture(prefecture);
                dataExcel.setUrl(url);
                dataExcel.setCol1(prefectureCode + categoryCode + convertCode(value1.trim()));
                dataExcel.setCol2(value2.trim());
                dataExcel.setCol3(value3.trim());
                dataExcel.setCol4(value4);
                dataExcel.setCol5(value5.trim());
                dataExcel.setCol6(value6.trim());
                dataExcel.setCol7(value7.trim());
                dataExcel.setCol8(value8.trim());
                dataExcel.setCol9(value9.trim());
                listData.add(dataExcel);
                workbook.close();
                inputStream.close();
            }
            for (DataExcel dataExcel1 : listData) {
                if (dataExcel1.getCol1().length() > 0) {
                    listDataExcel.add(dataExcel1);
                }
            }
            for (DataExcel dataExcel2 : listDataExcel) {
                if (dataExcel2.getCol1().length() > 0) {
                    listResult.add(dataExcel2);
                }
            }
            logger.info("End Read Files Excel");
            return listResult;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error: " + e);
            return listResult;
        }
    }

    /**
     * Get Workbook
     *
     * @param inputStream
     * @param excelFilePath
     * @return
     * @throws IOException
     */
    private static Workbook getWorkbook(InputStream inputStream, String excelFilePath) {
        try {
            logger.info("Get Workbook");
            Workbook workbook = null;
            if (excelFilePath.endsWith("xlsx")) {
                logger.info("Excel file ending in .xlsx");
                workbook = new XSSFWorkbook(inputStream);
            } else if (excelFilePath.endsWith("xls")) {
                logger.info("Excel file ending in .xls");
                workbook = new HSSFWorkbook(inputStream);
            } else {
                logger.info("The specified file is not Excel file");
                throw new IllegalArgumentException("The specified file is not Excel file");
            }

            return workbook;
        }catch (Exception e) {
            logger.error("Get Workbook Error: " + e);
            return null;
        }
    }

    /**
     * Get cell value
     *
     * @param cell
     * @return
     */
    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case _NONE:
            case BLANK:
            case ERROR:
                break;
            default:
                break;
        }

        return cellValue;
    }

    /**
     * Convert code
     *
     * @param value
     * @return
     */
    private static String convertCode(String value) {
        if (value.contains("-")) {
            value = value.replace("-", "");
        }
        if (value.contains(".")) {
            value = value.replace(".", "");
        }
        if (value.contains("・")) {
            value = value.replace("・", "");
        }
        if (value.contains(" ")) {
            value = value.replace(" ", "");
        }
        if (value.contains(",")) {
            value = value.replace(",", "");
        }
        return value;
    }

    /**
     * Lấy Category Code theo categoryId để thêm vào medicalInstituonCode
     * @param categoryId
     * @return
     */
    private static String getCategoryCode(long categoryId) {
        String categoryCode = "";
        if (categoryId <= 2L) {
            categoryCode = "1";
        } else if (categoryId >= 3L && categoryId <= 4) {
            categoryCode = "3";
        } else {
            categoryCode = "4";
        }
        return categoryCode;
    }

    /**
     * Lấy categoryName từ cell trong file excel
     * @param text
     * @return
     */
    private static String getCategoryName(String text) {
        String categoryName = "";
        if (text.contains("医科")) {
            if(text.contains("併設")) {
                categoryName = "医科併設";
            }else {
                categoryName = "医科";
            }
        }else if (text.contains("歯科")) {
            if(text.contains("併設")) {
                categoryName = "歯科併設";
            }else {
                categoryName = "歯科";
            }
        }else{
            categoryName = "薬局";
        }
        return categoryName;
    }

}
