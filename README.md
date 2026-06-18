# MyBatis Generator GUI

A JavaFX-based desktop GUI tool for [MyBatis Generator](https://mybatis.org/generator/), supporting **MySQL** and **PostgreSQL** databases. Quickly generate POJOs, Mapper interfaces, Mapper XML files, Service layers, and Controller code with built-in sharding support.

一款基于 JavaFX 的 [MyBatis Generator](https://mybatis.org/generator/) 桌面 GUI 工具，支持 **MySQL** 和 **PostgreSQL** 数据库，可快速生成实体类、Mapper 接口、XML 映射文件、Service 层和 Controller 代码，并内置分表支持。

## Features / 功能

- 🔌 **Multiple Database Support / 多数据库支持** — MySQL & PostgreSQL
- 🧩 **Code Generation / 代码生成** — POJO, Example, Mapper (interface + XML), Service, ServiceImpl, Controller
- 📦 **Sharding Support / 分表支持** — Built-in sharding-aware CRUD base classes
- 💾 **Config Persistence / 配置持久化** — Save & reuse database connections and generation configs (SQLite)
- 🎨 **JavaFX GUI / 图形界面** — Tree-based table browser, config management, one-click generation

## Screenshots / 截图

> TODO: Add screenshots

## Requirements / 环境要求

- **JDK** 21+
- **Maven** 3.8+
- **MySQL** 8.0+ or **PostgreSQL** 12+

## Quick Start / 快速开始

```bash
# Clone
git clone https://github.com/lilaizhencn/mybatis-generator.git
cd mybatis-generator

# Compile
mvn compile

# Run
mvn javafx:run

# Or package as fat JAR
mvn package
java -jar target/mybatis-generator-jar-with-dependencies.jar
```

## Usage Guide / 使用指南

1. **Add Connection / 添加连接** — Click "数据库连接" → fill in host, port, database type, username, password, schema
2. **Select Table / 选择表** — Double-click the database node → select a table from the tree
3. **Configure Generation / 配置生成** — Set project folder, package names, choose what to generate (Model, Example, Mapper, XML, Service, Controller)
4. **Generate / 生成代码** — Click "生成代码" to generate all selected artifacts

Default package prefix: `com.surprising.wallet.xxx` (replace `xxx` with your module name)

## Project Structure / 项目结构

```
src/main/java/org/mybatis/generator/
├── ui/                    # JavaFX UI layer
│   ├── MainUI.java        # Main application entry
│   ├── Launcher.java      # Launcher (non-module bootstrap)
│   ├── controller/        # FXML controllers
│   ├── model/             # Data models
│   ├── view/              # Alert utilities
│   ├── bridge/            # MyBatis Generator bridge
│   └── util/              # DB & config utilities
├── internal/              # MyBatis Generator internals (forked & modified)
│   ├── db/                # Database dialects
│   └── comment/           # Comment generators
├── api/                   # MyBatis Generator API (forked)
└── codegen/               # Code generation templates (forked)
src/main/resources/
├── fxml/                  # FXML layout files
├── icons/                 # UI icons
└── style.css              # Stylesheet
```

## License / 许可证

Apache License 2.0 — This project includes modified portions of [MyBatis Generator](https://github.com/mybatis/generator).

本项目包含 [MyBatis Generator](https://github.com/mybatis/generator) 的修改版本，使用 Apache License 2.0 许可。
