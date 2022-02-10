```yaml
# 健康检查配置
management:
  endpoints:
    web:
      base-path: /monitor
      exposure:
        include: '*'
    enabled-by-default: false
  server:
    address: 127.0.0.1
    port: 19999
  endpoint:
    health:
      show-details: always
      enabled: true
    shutdown:
      enabled: true
    prometheus:
      enabled: true

```
