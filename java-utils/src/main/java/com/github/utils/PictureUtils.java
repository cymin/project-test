package com.github.utils;

import java.io.*;
import java.util.Base64;

public class PictureUtils {
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
        String strImg = getImageStr("E:\\IdeaProjects\\project-test\\java-utils\\src\\main\\resources\\static\\c.png");
        System.out.println(strImg);
        File file = new File("E:\\IdeaProjects\\project-test\\java-utils\\src\\main\\resources\\static\\a.txt");
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        try {
            osw.write(strImg);
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final boolean b = generateImage(strImg, "E:\\IdeaProjects\\project-test\\java-utils\\src\\main\\resources\\static\\newa.png");
//        System.out.println(b);
    }

    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        // 解密
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] b = decoder.decode(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
