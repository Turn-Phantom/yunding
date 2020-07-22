import cn.hutool.core.io.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc
 * @date 2020-07-17
 */
public class TestClass {
    public static void main(String[] args) {
        List<String> fileList = FileUtil.readLines("C:\\Users\\Administrator\\Desktop\\发短信数据\\短信已发\\4.23发\\ji.txt", "GB2312");
        List<String> newFile = new ArrayList<>();
        for (String s : fileList) {
            if (s.startsWith("1")){
                newFile.add(s);
            }
        }
        FileUtil.writeLines(newFile, "C:\\Users\\Administrator\\Desktop\\发短信数据\\短信已发\\4.23发\\new_ji.txt", "GB2312");
    }
}
