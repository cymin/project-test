springcloud-shutdown-dev.yml
```yaml
server:
  port: 8085
  shutdown: graceful
  
spring:
  lifecycle:
    timeout-per-shutdown-phase: 50s

management:
  endpoints:
    web:
      base-path: /actuator
      exposure:
        include: service-registry,health,info,shutdown
        # include: '*'
    # enabled-by-default: false
  server:
    address: 127.0.0.1
  endpoint:
    health:
      show-details: always
      enabled: true
    shutdown:
      enabled: true
    prometheus:
      enabled: true
    info:
      enabled: true

```
