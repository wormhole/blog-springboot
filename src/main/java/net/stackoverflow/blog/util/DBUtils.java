package net.stackoverflow.blog.util;

import java.io.*;

/**
 * 数据库工具类
 *
 * @author 凉衫薄
 */
public class DBUtils {

    /**
     * 备份数据库
     *
     * @param host
     * @param username
     * @param password
     * @param path
     * @param filename
     * @param database
     * @return
     */
    public static boolean backup(String host, String username, String password, String path, String filename, String database) {
        File saveFile = new File(path);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        boolean success = false;
        try {
            process = Runtime.getRuntime().exec("mysqldump -h" + host + " -u" + username + " -p" + password + " " + database);
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path + filename), "UTF-8"));
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                printWriter.println(line);
            }
            printWriter.flush();
            if (process.waitFor() == 0) {
                success = true;
            }
        } catch (Exception e) {
            success = false;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {

            }
        }
        return success;
    }
}
