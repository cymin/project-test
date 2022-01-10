package com.github.minio.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio 配置信息
 *
 * @author ruoiy
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig
{
    /**
     * 服务地址
     */
    private String url;

    /**
     * 用户名
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName;

//    /**
//     * 定时任务中图片的存储桶名称
//     */
//    private String bucketNameTiming;

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getAccessKey()
    {
        return accessKey;
    }

    public void setAccessKey(String accessKey)
    {
        this.accessKey = accessKey;
    }

    public String getSecretKey()
    {
        return secretKey;
    }

    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }

    public String getBucketName()
    {
        return bucketName;
    }

    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

 /*   public String getBucketNameTiming() {
        return bucketNameTiming;
    }

    public void setBucketNameTiming(String bucketNameTiming) {
        this.bucketNameTiming = bucketNameTiming;
    }*/

    @Bean
    public MinioClient getMinioClient() throws Exception {

        MinioClient minioClient = MinioClient.builder().endpoint(url).credentials(accessKey, secretKey).build();
        boolean res = minioClient.bucketExists(BucketExistsArgs.builder().bucket(this.getBucketName()).build());
        if (res) {
            System.out.println("bucket：" + this.getBucketName() + "已经存在！");
        } else {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(this.getBucketName()).build());
            System.out.println("bucket：" + this.getBucketName() + "完成创建！");
        }
        String policy = "{\"Version\": \"2012-10-17\",\"Statement\": [{\"Action\": [\"s3:GetObject\"],\"Effect\": \"Allow\",\"Resource\": \"arn:aws:s3:::" + this.bucketName + "/*\",\"Principal\": \"*\"}]}";
        SetBucketPolicyArgs policyArgs = SetBucketPolicyArgs.builder().bucket(this.getBucketName()).config(policy).build();
        minioClient.setBucketPolicy(policyArgs);
      /*  
        boolean res2 = minioClient.bucketExists(BucketExistsArgs.builder().bucket(this.getBucketNameTiming()).build());
        if (res2) {
            System.out.println("bucket：" + this.getBucketNameTiming() + "已经存在！");
        } else {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(this.getBucketNameTiming()).build());
            System.out.println("bucket：" + this.getBucketNameTiming() + "完成创建！");
        }

        String policy2 = "{\"Version\": \"2012-10-17\",\"Statement\": [{\"Action\": [\"s3:GetObject\"],\"Effect\": \"Allow\",\"Resource\": \"arn:aws:s3:::" + this.bucketNameTiming + "/*\",\"Principal\": \"*\"}]}";
        SetBucketPolicyArgs policyArgs2 = SetBucketPolicyArgs.builder().bucket(this.getBucketNameTiming()).config(policy2).build();
        minioClient.setBucketPolicy(policyArgs2);*/
        return minioClient;
    }
}
