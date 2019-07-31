CREATE TABLE `properties`
(
  `id`          INT(11)      NOT NULL AUTO_INCREMENT  ,
  `application` VARCHAR(50)  NOT NULL COMMENT '应用名称',
  `profile`     VARCHAR(50)  NOT NULL COMMENT '应用环境',
  `label`       VARCHAR(50)  NOT NULL COMMENT '资源label',
  `key`        VARCHAR(50)  NOT NULL COMMENT '配置参数key',
  `value`      VARCHAR(500) NOT NULL COMMENT '配置参数 value',
  PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

# application
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'define.user.name', 'admin');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'define.user.password', '{cipher}a1d30c1325f1b0cd2e4619bff2be57bc32ae0413c0ce1fee07153b60bfa3a782');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'define.auth.securityKey', '121231313131313');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'define.druid.username', 'admin');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'define.druid.password', '{cipher}a1d30c1325f1b0cd2e4619bff2be57bc32ae0413c0ce1fee07153b60bfa3a782');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'define.mysql.write.url', 'jdbc:mysql://192.168.0.86:3306/survey?useUnicode=true&characterEncoding=utf8&useSSL=false');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'define.mysql.write.username', 'root');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'define.mysql.write.password', 'mysql');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'define.mysql.read1.url', 'jdbc:mysql://192.168.0.86:3306/survey?useUnicode=true&characterEncoding=utf8&useSSL=false');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'define.mysql.read1.username', 'root');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'define.mysql.read1.password', 'mysql');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'eureka.client.fetch-registry', 'true');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'eureka.client.register-with-eureka', 'true');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'eureka.client.registry-fetch-interval-seconds', '5');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'eureka.client.serviceUrl.defaultZone', 'http://${define.user.name}:${define.user.password}@192.168.0.86:3306:48010/eureka/');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'eureka.instance.prefer-ip-address', 'true');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'eureka.instance.instance-id', '${spring.cloud.client.ip-address}:${server.port}');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'eureka.instance.metadata-map.user.name', '${define.user.name}');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'eureka.instance.metadata-map.user.password', '${define.user.password}');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'eureka.instance.health-check-url-path', '/actuator/health');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'eureka.instance.lease-renewal-interval-in-seconds', '1');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'eureka.instance.lease-expiration-duration-in-seconds', '2');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.http.multipart.max-file-size', '5MB');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.http.multipart.max-request-size', '100MB');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.cloud.bus.trace.enabled', 'true');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.data.mongodb.host', '192.168.0.86');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.data.mongodb.port', '27017');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.data.mongodb.database', 'survey');
/*INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.data.mongodb.username', 'admin');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.data.mongodb.password', 'admin123');*/
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.redis.host', '192.168.0.86');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.redis.port', '6379');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.redis.password', 'legaoyun@2018');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.redis.database', '0');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.redis.jedis.pool.max-active', '20');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.redis.jedis.pool.max-idle', '20');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.redis.jedis.pool.max-wait', '10000');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.redis.jedis.pool.min-idle', '0');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.redis.timeout', '10000');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.rabbitmq.host', '192.168.0.86');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.rabbitmq.port', '5672');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.rabbitmq.username', 'admin');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.rabbitmq.password', 'legaoyun@2018');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.jackson.date-format', 'yyyy-MM-dd HH:mm:ss');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.jackson.time-zone', 'GMT+8');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.boot.admin.client.username', '${define.user.name}');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'spring.boot.admin.client.password', '${define.user.password}');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'management.endpoints.web.exposure.include', '*');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'logging.level.com.lego.**', 'info');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'logging.file', '/home/work/logs/${spring.application.name}/current.log');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'ribbon.ConnectTimeout', '30000');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'ribbon.ReadTimeout', '60000');
INSERT INTO properties (application, profile, label, `key`, value)
VALUES ('application', 'dev', 'master', 'ribbon.command.default.execution.isolation.thread.timeoutInMilliseconds', '60000');
# api-survey
INSERT INTO properties  (application, profile, label, `key`, value)
VALUES ( 'api-survey', 'dev', 'master', 'define.test.name', 'test09');
# auth-service
INSERT INTO properties  (application, profile, label, `key`, value)
VALUES ( 'auth-service', 'dev', 'master', 'jwt.info.clientId', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO properties  (application, profile, label, `key`, value)
VALUES ('auth-service', 'dev', 'master', 'jwt.info.base64Secret', 'PL8iU@vt&0tFWijuoi%zASgsLP1rKwks');
INSERT INTO properties  (application, profile, label, `key`, value)
VALUES ('auth-service', 'dev', 'master', 'jwt.info.name', 'user-api');
INSERT INTO properties  (application, profile, label, `key`, value)
VALUES ( 'auth-service', 'dev', 'master', 'jwt.info.expiresSecond', '432000');
# report-service
INSERT INTO properties  (application, profile, label, `key`, value)
VALUES ( 'report-service', 'dev', 'master', 'define.datasource.type', 'com.alibaba.druid.pool.DruidDataSource');
INSERT INTO properties  (application, profile, label, `key`, value)
VALUES ('report-service', 'dev', 'master', 'jwt.info.base64Secret', 'PL8iU@vt&0tFWijuoi%zASgsLP1rKwks');
INSERT INTO properties  (application, profile, label, `key`, value)
VALUES ('report-service', 'dev', 'master', 'jwt.info.name', 'user-api');
INSERT INTO properties  (application, profile, label, `key`, value)
VALUES ( 'report-service', 'dev', 'master', 'jwt.info.expiresSecond', '432000');


COMMIT;
SELECT *
FROM properties