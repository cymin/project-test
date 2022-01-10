package com.github.minio.controller;

import com.github.minio.model.FileItem;
import com.github.minio.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
public class UploadController {
    private static final String BUCKET_JYJ = "jyj";
    @Autowired
    FileService fileService;

    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("E:\\cymin\\吉野家项目\\テスト用商品画像\\テスト用商品画像.zip");

        ZipEntry nextEntry = null;
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream, Charset.forName("GBK"))) {
            nextEntry = zipInputStream.getNextEntry();
            System.out.println(nextEntry);

            while (nextEntry != null) {
                File extractedFile = new File(nextEntry.getName());
                System.out.println(extractedFile);
                nextEntry = zipInputStream.getNextEntry();
            }
        }
    }

    /**
     * 删除文件
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/delete")
    public String deleteOne() throws Exception {
        String path = "吉野家v0.1.1项目进度.xlsx";
        fileService.delete(path);
        return "success";
    }

    /**
     * 获取path中带test（前缀）的文件列表
     * 比如path=jyj/test/新建文本文档.txt
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/list")
    public List<FileItem> list() throws Exception {
        final List<FileItem> list = fileService.listObjects("test");
        return list;
    }

    /**
     * 把文件path中带menu-timing中的数据copy到指定的地方
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/copy")
    public List<FileItem> copy() throws Exception {
        // copy
        final List<FileItem> list = fileService.listObjects("menu-timing");
        if (list != null && list.size() > 0) {
            list.stream().forEach(f -> {
                final String objectName = f.getObjectName();
                try {
                    fileService.copyObject(objectName, BUCKET_JYJ);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // delete 
            final List<String> collect = list.stream().map(f -> f.getObjectName()).collect(Collectors.toList());
            final String join = String.join("_", collect);
            fileService.delete(BUCKET_JYJ, join);
        }
        return list;
    }

    /**
     * 在线解压zip文件
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/extract")
    public Map<String, Object> extractZipFiles(MultipartFile file) throws Exception {
        List<String> list = new ArrayList<>();
        final InputStream inputStream = file.getInputStream();
        ZipEntry nextEntry;
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream, Charset.forName("GBK"))) {
            nextEntry = zipInputStream.getNextEntry();
            while (nextEntry != null) {
                nextEntry = zipInputStream.getNextEntry();
                if (nextEntry == null) {
                    break;
                }
                String name = nextEntry.getName();
                list.add(name);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("filenames", list);
        map.put("count", list.size());
        return map;
    }

    /**
     * 在线解压文件并存储到minio
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/saveToMinnio", headers = "content-type=multipart/*")
    public List<String> importFile(MultipartFile file) throws Exception {
        List<String> list = new ArrayList<>();
        final InputStream inputStream = file.getInputStream();
        int n = 0;
        ZipEntry nextEntry;
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream, Charset.forName("GBK"))) {
            nextEntry = zipInputStream.getNextEntry();
            while (nextEntry != null) {
                nextEntry = zipInputStream.getNextEntry();
                if (nextEntry == null) {
                    break;
                }
                
                //不要文件夹
                String name = nextEntry.getName();
                int i = name.lastIndexOf("/");
                if (-1 != i) {
                    name = name.substring(i + 1);
                }
                String fileName = name.substring(0,name.lastIndexOf("."));
                String fileType = name.substring(name.lastIndexOf("."));
                if (!nextEntry.isDirectory()) {
                    final byte[] bytes = getBytes(zipInputStream);
                    fileService.putObject(BUCKET_JYJ, bytes, fileName + fileType, fileType);
                }
                list.add(name);
                n ++;
                if (n > 4) {
                    System.out.println("n=" + n);
                    break;
                }
               
            }
        }
        return list;
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
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

   
}


   
