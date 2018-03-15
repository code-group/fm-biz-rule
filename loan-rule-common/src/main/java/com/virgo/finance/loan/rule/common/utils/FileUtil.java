package com.virgo.finance.loan.rule.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author: zhulili1
 * date: 2017/10/16
 * description: 文件操作工具类
 */
@Slf4j
public class FileUtil {

    /**
     * created by zhulili1, on 2017/10/16
     * description: 读取文件转化为字符串
     **/
    public static String readFileToString(String filePath) {
        int len=0;
        StringBuffer strBuffer = new StringBuffer("");
        File file = new File(filePath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            while( (line = reader.readLine())!=null ) {
                if (len != 0) {
                    strBuffer.append("\n" + line);
                } else {
                    strBuffer.append(line);
                }
                len++;
            }
            fileInputStream.close();
            inputStreamReader.close();
        } catch (IOException e) {
            log.error("读取文件转化为字符串异常：", e.getMessage(), e);
        }
        return strBuffer.toString();
    }

}
