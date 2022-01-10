package com.github.minio.service;

import com.github.minio.config.MinioConfig;
import com.github.minio.model.FileItem;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    MinioConfig minioConfig;

    @Autowired
    private MinioClient client;
    
    public List<FileItem> listObjects(String prefix) throws Exception {
        String bucketName = minioConfig.getBucketName();
        List<FileItem> list = new ArrayList<>();
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (found) {
            Iterable<Result<Item>> myObjects = client.listObjects(ListObjectsArgs.builder().prefix(prefix).recursive(true).bucket(bucketName).build());
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                FileItem fileItem = new FileItem();
                fileItem.setDeleteMarker(item.isDeleteMarker());
                fileItem.setDir(item.isDir());
                fileItem.setEtag(item.etag());
                fileItem.setLatest(item.isLatest());
                fileItem.setLastModified(item.lastModified());
                fileItem.setObjectName(item.objectName());
                fileItem.setVersionId(item.versionId());
                fileItem.setSize(item.size());
                list.add(fileItem);
            }
        } else {
            System.out.println(bucketName + " does not exist");
        }
        return list;
    }

    public void copyObject(String object, String sourceBucketName) throws Exception {
        final CopyObjectArgs copyObjectArgs = CopyObjectArgs.builder()
                .object(object)
                .bucket(minioConfig.getBucketName())
                .source(CopySource.builder().object(object).bucket(sourceBucketName).build())
                .build();
        client.copyObject(copyObjectArgs);
    }
    
    public void putObject(String bucketName, byte[] bytes, String fileName, String contentType) throws Exception {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                .contentType(contentType)
                .build();
        client.putObject(args);
    }

    public void putObject(byte[] bytes, String fileName, String contentType) throws Exception {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                .contentType(contentType)
                .build();
        client.putObject(args);
    }

    public void delete(String bucketName, String path) throws Exception {
        String[] paths = path.split("_");
        List<DeleteObject> deleteObjects = Arrays.stream(paths).map(DeleteObject::new).collect(Collectors.toList());
        RemoveObjectsArgs args = RemoveObjectsArgs.builder()
                .bucket(bucketName)
                .objects(deleteObjects)
                .build();
        Iterable<Result<DeleteError>> results = client.removeObjects(args);
        results.forEach(r -> {
            try {
                System.out.println(r.get().message());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void delete(String path) throws Exception {
        String[] paths = path.split("_");
        List<DeleteObject> deleteObjects = Arrays.stream(paths).map(DeleteObject::new).collect(Collectors.toList());
        RemoveObjectsArgs args = RemoveObjectsArgs.builder()
                .bucket(minioConfig.getBucketName())
                .objects(deleteObjects)
                .build();
        Iterable<Result<DeleteError>> results = client.removeObjects(args);
        results.forEach(r -> {
            try {
                System.out.println(r.get().message());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void download(String path, HttpServletResponse response) throws Exception {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(path)
                .build();
        GetObjectResponse object = client.getObject(args);
        ServletOutputStream outputStream = response.getOutputStream();
        StreamUtils.copy(object, outputStream);
        outputStream.close();
    }

    public void uploadFile(InputStream inputStream, String fileName, String contentType) throws Exception {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(inputStream, inputStream.available(), -1)
                .contentType(contentType)
                .build();
        client.putObject(args);
    }
}
