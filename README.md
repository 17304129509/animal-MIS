# 校园流浪动物救助管理系统

面向校园流浪动物救助场景，支持动物建档、投喂打卡、救助工单、领养审核和数据统计。用户端服务学生和志愿者，管理端提供建档审批、工单处理、领养审核、投喂点维护、用户管理和统计。

## 技术栈

Java 17、Spring Boot、Spring MVC、MyBatis、MySQL 8、JWT。两个页面由同一个 Spring Boot 应用提供，默认使用原来的 8081 用户端和 8082 管理端。

## 本地运行

准备 JDK 17、Maven 和 MySQL 8。在仓库根目录初始化数据库。以下命令适用于 PowerShell，其他 shell 可以用 MySQL 客户端执行同名 SQL 文件。

```powershell
mysql -u root -p --default-character-set=utf8mb4 --execute="source database/schema.sql"
mysql -u root -p --default-character-set=utf8mb4 campus_animal --execute="source database/demo-data.sql"
$env:DB_USER = 'root'
$env:DB_PASSWORD = '填写本机 MySQL 密码'
mvn clean verify
java -jar target/animal-rescue-1.0.0.jar
```

打开 [用户端](http://localhost:8081) 或 [管理端](http://localhost:8082)。第二条 SQL 可在空表中加载演示数据，演示账号均使用密码 `123456`。

| 账号 | 角色 |
| --- | --- |
| `stu001` | 学生 |
| `vol001` | 志愿者 |
| `admin001` | 管理员 |

| 环境变量 | 用途 |
| --- | --- |
| `DB_URL` | 默认连接本机 MySQL 的 `campus_animal` |
| `DB_USER`、`DB_PASSWORD` | MySQL 登录配置 |
| `SERVER_PORT`、`ADMIN_PORT` | 默认分别为 8081、8082 |
| `JWT_SECRET` | 可配置至少 32 字节的签名密钥，留空时每次启动随机生成 |
| `UPLOAD_DIR` | 新上传图片保存目录，默认 `static/uploads` |

## 从旧版迁移数据

在现有数据库上执行 `database/migrate-legacy.sql`。脚本检查并补齐动物的性别、健康字段和工单紧急程度字段。配置 `DB_URL` 连接原库，保留原 `static/uploads` 或将 `UPLOAD_DIR` 指向它。旧账号的明文密码在验证成功后转存为散列。两个后端现在使用同一个签名配置，迁移后需重新登录。

## 后端实现

- Spring MVC 控制器保留用户端和管理端各自使用的 RESTful 路径与业务字段。拦截器验证 JWT 并读取当前用户角色，业务异常统一返回状态与提示。
- MyBatis Mapper 使用参数绑定、动态条件和 MySQL `LIMIT/OFFSET` 处理列表筛选与分页。统计和排行榜由 SQL 聚合。
- 救助工单状态变更和领养审核在事务中锁定相关记录。领养通过与动物状态更新一起提交。投喂扣除积分和写入打卡记录一起提交。
- `src/main/resources/static` 保存原用户端打包静态资源及管理端页面，运行时一起随 JAR 提供。

## 回归检查

在导入演示数据的独立测试库启动应用，然后执行 `node scripts/smoke.mjs`。脚本需要 Node.js 20+，会创建测试记录，覆盖用户与管理端认证、档案审批、投喂扣分及事务回滚、并发接单、领养审核、分页筛选和统计。
