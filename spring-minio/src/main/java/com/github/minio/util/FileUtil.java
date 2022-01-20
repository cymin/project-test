package com.github.minio.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    // inputStream to bytes
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        //将流(inputStream)转成字节数组
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            byte[] readBuffer = new byte[4096]; //readBuffer用于存放循环读取的临时数据
            int readLen;
            while ((readLen = inputStream.read(readBuffer)) != -1) {
                outStream.write(readBuffer, 0, readLen);
            }
            //合并之后的字节数组
            return outStream.toByteArray();
        }
    }
    
    public static InputStream bytesToInputstraeam(byte[] bytes) {
        InputStream input = new ByteArrayInputStream(bytes);
        return input;
    }
}
