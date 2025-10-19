# training24 - Mini 教务选课系统

一个用于 Java Web 课程实训的轻量项目：后端基于 JDK 内置 `HttpServer` + 注解路由的微框架，前端使用自研 `miniquery.js` 与集成 `lwquery.js`，支持学生、课程与选课的基础功能。

## 目录结构
```
training24/
├── src/com/training/               # 后端代码
│   ├── framework/annotations       # 注解：Controller/Route/Inject/FieldFormat
│   ├── framework/core              # 核心：Router/Injector/HttpServerApp/RequestContext/Json
│   ├── framework/utils             # 工具：JsonLite（POST体解析）
│   ├── controllers                 # 控制器：Student/Course/Enroll
│   ├── services                    # 服务：Student/Course/Enroll
│   └── entities                    # 实体：Student/Course
├── web/                            # 前端页面与静态资源
│   ├── assets/                     # 样式与脚本
│   │   ├── style.css
│   │   ├── miniquery.js
│   │   └── lwquery.js              # 也可通过 /ext/lwquery.js 加载外部版本
│   ├── index.html                  # 首页
│   ├── students.html               # 学生管理
│   ├── courses.html                # 课程管理
│   ├── enroll.html                 # 选课管理
│   └── tasks.html                  # 扩展任务概览
├── docs/                           # 项目文档
│   ├── 实训项目任务书.md
│   ├── 实训项目指导书.md
│   └── 扩展任务.md
└── run.ps1                         # 一键编译与运行（Windows PowerShell）
```

## 快速开始
- 依赖：`JDK 11+`，`PowerShell`，`git`（可选）
- 运行：
  - PowerShell 执行：
    ```powershell
    ./run.ps1
    ```
  - 控制台输出：`Server started at http://localhost:8080/`
  - 浏览器访问：`http://localhost:8080/`

## 后端 API（示例）
- 学生：
  - `GET /api/students` 列表
  - `POST /api/students` 新增
- 课程：
  - `GET /api/courses` 列表
  - `POST /api/courses` 新增
- 选课：
  - `GET /api/enroll?studentId=20201` 查询某学生已选课程
  - `POST /api/enroll` { studentId, courseName } 选课

统一返回格式：
```json
{ "error": 0, "reason": "ok", "data": ... }
{ "error": 400, "reason": "错误说明" }
```

## 前端说明
- 页面统一引入：
  ```html
  <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
  <script src="/ext/lwquery.js"></script>
  <script src="assets/miniquery.js"></script>
  ```
- POST 统一使用 `LW.rest(url, req, okHandler, errHandler)`；GET 保持 `$.ajax({method:'GET'})` 或 `$.get`。

## 扩展任务
详见 `docs/扩展任务.md`，包含：搜索与分页、更新与删除、选课取消、导出 CSV、登录鉴权、持久化、文件上传、统一异常与日志等。

## 推送到 GitHub（手动）
若本机未安装 `gh` CLI，可按以下步骤手动推送：
1. 在 GitHub Web 创建仓库（建议名：`training24`）。
2. 在本项目根目录执行：
   ```powershell
   git init
   git add -A
   git commit -m "Initial commit: training24 micro-framework and frontend"
   git branch -M main
   git remote add origin https://github.com/<your-username>/training24.git
   git push -u origin main
   ```
- 若使用 SSH：
  ```powershell
  git remote add origin git@github.com:<your-username>/training24.git
  git push -u origin main
  ```

> 如需我自动创建仓库并推送，请安装并登录 GitHub CLI：
> ```powershell
> winget install GitHub.cli
> gh auth login
> gh repo create training24 --source . --public --remote origin --push -y
> ```