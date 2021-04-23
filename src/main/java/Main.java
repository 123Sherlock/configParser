import utils.ExcelUtils;
import utils.FileUtils;
import utils.PropertyUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        int parseBeginRow = PropertyUtils.getParseBeginRow();
        int parseBeginColumn = PropertyUtils.getParseBeginColumn();
        String outputDir = PropertyUtils.getOutputDir();

        List<File> excelFiles = FileUtils.loadFilesFromDir(PropertyUtils.getExcelDir(), ExcelUtils::isExcelFile);
        int successCount = 0;

        for (File excelFile : excelFiles) {
            String fileName = excelFile.getName();
            if (!fileName.contains("_")) {
                System.err.println(String.format("illegal file name:[%s]", fileName));
                continue;
            }
            String jsonFileName = getJsonFileName(fileName);

            String jsonString = ExcelUtils.excelToJsonString(excelFile, parseBeginRow, parseBeginColumn);
            if (jsonString == null) {
                System.err.println(String.format("excel file parse fail:[%s]", fileName));
                continue;
            }

            BufferedWriter out;
            try {
                out = new BufferedWriter(new FileWriter(outputDir + File.separator + jsonFileName));
                out.write(jsonString);
                out.write("\n");
                out.close();

                System.out.println(String.format("parse [%s] success", fileName));
                successCount++;
            } catch (IOException e) {
                System.err.println(String.format("json file write fail:[%s]", fileName));
                e.printStackTrace();
            }
        }
        System.out.println(String.format("complete! %s excel files, %s files success", excelFiles.size(), successCount));
    }

    /**
     * 根据excel文件名生成json文件名
     * excel文件名用下划线隔开前后两部分，后半为json名称
     */
    private static String getJsonFileName(String excelFileName) {
        String excelLastName = excelFileName.split("_")[1];
        return excelLastName.replace(excelLastName.substring(excelLastName.lastIndexOf(".")), ".json");
    }
}
