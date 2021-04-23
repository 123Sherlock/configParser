package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileUtils {

    public static final String USER_DIR = System.getProperty("user.dir");

    /**
     * 加载配置文件
     *
     * @param propertiesName 配置文件名称
     */
    public static Properties loadProperties(String propertiesName) {
        String propertyPath = USER_DIR + File.separator + propertiesName;
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(propertyPath);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 加载目录下的所有文件
     *
     * @param directory  目录
     * @param fileFilter 文件过滤器
     * @return 不包括子目录
     */
    public static List<File> loadFilesFromDir(String directory, Predicate<File> fileFilter) {
        return Arrays.stream(Objects.requireNonNull(new File(directory).listFiles()))
            .filter(File::isFile) // 暂时不递归文件夹
            .filter(fileFilter)
            .collect(Collectors.toList());
    }
}
