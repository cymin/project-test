package com.github.minio.controller;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/u2")
public class UploadController2 {
    
    private Lock lock = new ReentrantLock();

    /**
     * 使用SeekableInMemoryByteChannel解决不同操作系统下压缩文件格式解析出错问题
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/extract2")
    public List<String> extractWithZipInputStream(MultipartFile file) throws Exception {
        lock.lock();
        List<String> list = new ArrayList<>();
        SeekableInMemoryByteChannel channel = null;
        try {
            channel = new SeekableInMemoryByteChannel(file.getBytes());
            ZipFile zipFile = new ZipFile(channel, "GBK");
            final Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                final ZipArchiveEntry zipArchiveEntry = entries.nextElement();
                if(!zipArchiveEntry.isDirectory()){
                    try(InputStream inputStream = zipFile.getInputStream(zipArchiveEntry)){
                        String name = zipArchiveEntry.getName();
                        System.out.println(name);
                        list.add(name);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
       return list;
    }

   /* public static void main(String[] args) throws Exception {
        String path = "E:\\cymin\\吉野家项目\\テスト用商品画像2.zip";
        File zFile = new File(path);
        ZipFile zipFile = new ZipFile(zFile);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry nextEntry = entries.nextElement();
            if(!nextEntry.isDirectory()){
                try(InputStream inputStream = zipFile.getInputStream(nextEntry)){
                    String name = nextEntry.getName();
                    System.out.println(name);
                }
            }
        }
    }*/
}


   
