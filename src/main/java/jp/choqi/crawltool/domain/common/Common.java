package jp.choqi.crawltool.domain.common;

import jp.choqi.crawltool.domain.entities.Category;

import java.util.HashMap;

public class Common {
    public static HashMap<String, Category> categoryHashMap = new HashMap<>();
    public static final int STATUS_FALSE = 1;
    public static final int STATUS_PROCESS = 2;
    public static final int STATUS_SUCCESS = 3;
    public static final int STATUS_NOT_RUN = 4;
}
