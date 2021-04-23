package utils;

import java.util.Properties;

public class PropertyUtils {

    private static final Properties PROPERTIES;

    static {
        PROPERTIES = FileUtils.loadProperties("config.properties");
    }

    public static String getExcelDir() {
        return PROPERTIES.getProperty("excel_dir");
    }

    public static String getOutputDir() {
        return PROPERTIES.getProperty("output_dir");
    }

    public static int getParseBeginRow() {
        return Integer.parseInt(PROPERTIES.getProperty("parse_begin_row"));
    }

    public static int getParseBeginColumn() {
        return Integer.parseInt(PROPERTIES.getProperty("parse_begin_column"));
    }
}
