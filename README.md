# 校园流浪动物救助管理系统

面向校园流浪动物救助场景，支持动物建档、投喂打卡、救助工单、领养审核和数据统计。用户端服务学生和志愿者，管理端提供建档审批、工单处理、领养审核、投喂点维护、用户管理和统计。

## 技术栈

Java 17、Spring Boot、Spring MVC、MyBatis、MySQL 8、JWT。两个页面由同一个 Spring Boot 应用提供，默认使用原来的 8081 用户端和 8082 管理端。

## 下载 ZIP 后启动

在 Windows 10/11 x64 上，从 GitHub 选择 **Code → Download ZIP**，解压整个文件夹后双击 `启动.bat`。启动程序使用仓库自带的 JAR，自动准备本地演示数据库，并打开用户端 `http://127.0.0.1:8081/` 和管理端 `http://127.0.0.1:8082/`。

- 如果 PATH 中有 Java 17+ 和 MySQL 8，启动程序会复用它们的可执行文件。缺少运行时会在首次启动时从供应商下载并校验，MySQL 下载约 248 MB。双击运行无需 Maven、Node.js 或手动执行 SQL。
- 演示数据库使用本地 `33067` 端口，数据和上传文件保存在解压目录的 `.runtime` 中。保留该目录即可在下次启动时继续使用数据。结束演示请在启动窗口按 Ctrl+C。
- 演示账号为 `stu001`、`vol001` 和 `admin001`，演示密码均为 `123456`。

## 源码和构建

- `frontend/src/views` 是根据原发布包恢复的可编辑 Vue 页面，使用 Vue 渲染函数。路由、会话、API 请求和样式各有独立文件，`frontend/src/vendor` 保存恢复时使用的第三方运行时。
- 管理端源码位于 `src/main/resources/static/console`，Java 后端和 MyBatis 映射分别位于 `src/main/java` 和 `src/main/resources/mapper`。
- 开发时可使用 Node.js 20+ 执行 `npm --prefix frontend ci` 和 `npm --prefix frontend run dev`，Vite 会将 API 请求转发给 8081 上的后端。修改源码后，在 Windows 项目根目录运行下面的命令即可重新生成一键启动使用的 JAR。

```powershell
powershell.exe -ExecutionPolicy Bypass -File .\scripts\build-package.ps1
```

## 使用自有 MySQL

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
