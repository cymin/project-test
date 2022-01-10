cmd
```bash
docker run -p 9000:9000 -p 9001:9001 -d --network=service --name minio -e "MINIO_ROOT_USER=minioadmin" -e "MINIO_ROOT_PASSWORD=minioadmin" -e "MINIO_BUCKETNAME=jyj" -e "MINIO_BUCKET=jyj"  -v E:/minioData:/data quay.io/minio/minio  server /data --console-address ":9001"
```


