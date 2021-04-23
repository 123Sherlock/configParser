package utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelUtils {

    /**
     * 把excel文件内容转换为json字符串
     */
    public static String excelToJsonString(File excelFile, int parseBeginRow, int parseBeginColumn) {
        try {
            Workbook workbook = WorkbookFactory.create(excelFile);
            if (workbook.getNumberOfSheets() <= 0) {
                return null;
            }
            // 只取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);

            // 参数的行号从1开始，poi的行号从0开始
            int beginRowIndex = parseBeginRow - 1;
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum < beginRowIndex + 2) {
                // 至少要有数据名称，数据类型和一条数据三行
                return null;
            }

            // 规定读取的第一行是数据名称（不能重复）
            Row nameRow = sheet.getRow(beginRowIndex);
            // 规定读取的第二行是数据类型
            Row typeRow = sheet.getRow(beginRowIndex + 1);
            // 列数要与名称对应
            short columnNum = nameRow.getLastCellNum();

            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\n");

            int index = 1;
            for (int i = beginRowIndex + 2; i <= lastRowNum; i++) {
                Row dataRow = sheet.getRow(i);
                Map<String, Object> dataMap = new LinkedHashMap<>();

                // 参数的列号从1开始，poi的列号从0开始
                for (int j = parseBeginColumn - 1; j < columnNum; j++) {
                    Cell cellType = typeRow.getCell(j);
                    Cell cellVal = dataRow.getCell(j);

                    String dataName = nameRow.getCell(j).getStringCellValue();
                    Object dataValue = cellValToObj(cellType, cellVal);
                    dataMap.put(dataName, dataValue);
                }

                sb.append("\"").append(index).append("\"").append(":");
                sb.append(new JSONObject(dataMap).toJSONString());
                sb.append(",");
                sb.append("\n");
                index++;
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("}");
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isExcelFile(File file) {
        String fileName = file.getName();
        return fileName.endsWith(".xls") || fileName.endsWith(".xlsx");
    }

    /**
     * 把单元格内容转换为对应类型的对象
     *
     * @param cellType 单元格的数据类型
     * @param cellVal  单元格的原数据内容
     */
    private static Object cellValToObj(Cell cellType, Cell cellVal) {
        String type = cellType.getStringCellValue();
        String value = new DataFormatter().formatCellValue(cellVal);
        switch (type) {
            case "int":
                return Integer.valueOf(value);
            case "double":
                return Double.valueOf(value);
            case "bool":
                return Boolean.valueOf(value);
            case "array": // 数组只能用逗号作分隔符
                return value.split(",");
            default: // 默认字符串类型
                return value;
        }
    }
}
