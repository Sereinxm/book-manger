# 图书管理系统

## 需求分析

#### 一、实体分析

| 实体               | 属性                                                                            | 关系说明                |
| ---------------- | ----------------------------------------------------------------------------- | ------------------- |
| **User**         | `id, name, email, google_id, password_hash, role(enum)`, join_date, is_active | 支持Google OAuth和邮箱登录 |
| **Admin**        | 继承自User实体，额外属性：`admin_level(enum)`                                            | 管理员分为超级管理员和普通管理员    |
| **Book**         | `isbn, title, publisher, publish_year`                                        | 核心书目元数据             |
| **BookCopy**     | `copy_id, book_isbn(FK), status(enum)`, location                              | 物理副本管理              |
| **Author**       | `id, name, bio, user_id(FK)`                                                  | 作者实体（可关联用户）         |
| **BorrowRecord** | `id, user_id(FK), copy_id(FK)`, borrow_date, due_date, return_date            | 借阅生命周期管理            |

**关键关系说明**：

1. 书-作者多对多：通过`book_author`关联表实现
   
   - 图书删除时自动解除关联

2. 作者-用户可选绑定：
   
   ```
   erDiagram
      USER ||--o{ AUTHOR : "可选关联"
      USER {
          string id PK
          string google_id
      }
      AUTHOR {
          string id PK
          string user_id FK "nullable"
      }
   ```

---

#### 二、功能需求

##### 用户模块（User）

| 功能     | 描述                  | 权限                 |
| ------ | ------------------- | ------------------ |
| 谷歌凭证登录 | 通过Google OAuth2.0实现 | 公开                 |
| 邮箱注册登录 | 使用邮箱和密码注册/登录        | 公开                 |
| 查看借阅记录 | 显示当前/历史借阅及归还状态      | 自身  (作者看到自己书的所有记录) |
| 修改个人信息 | 更新姓名、邮箱等基本信息        | 自身                 |

##### 图书管理模块

| 功能     | 描述              | 权限         |
| ------ | --------------- | ---------- |
| 添加新书   | 创建书目+初始副本（最少1本） | Admin      |
| 编辑图书详情 | 修改元数据（ISBN不可修改） | Admin      |
| 删除图书   | 需检查无借出副本才允许删除   | Admin      |
| 借阅图书   | 选择在馆副本生成借阅记录    | User       |
| 归还图书   | 更新副本状态+借阅记录     | User/Admin |

##### 管理员专属功能（Admin）

**普通管理员（ADMIN）**：

- **用户管理**：冻结/解冻普通用户账户
- **副本管理**：新增/报废图书副本
- **借阅监督**：查看所有借阅记录
- **系统维护**：配置借阅规则（借期）

**超级管理员（SUPER_ADMIN）**：

- 包含普通管理员的所有权限
- **权限管理**：提升普通用户为管理员，降级普通管理员
- **管理员管理**：冻结/解冻管理员账户
- **系统配置**：修改系统级别配置参数

---

#### 三、权限矩阵

| 操作         | User | Admin | Super Admin |
| ---------- | ---- | ----- | ----------- |
| 查看自身借阅记录   | ✔    | ✔     | ✔           |
| 借阅可用图书     | ✔    | ✔     | ✔           |
| 归还自己借阅的书   | ✔    | ✔     | ✔           |
| 创建/编辑/删除图书 | ✘    | ✔     | ✔           |
| 归还他人借阅的图书  | ✘    | ✔     | ✔           |
| 查看所有用户借阅历史 | ✘    | ✔     | ✔           |
| 管理普通用户账户状态 | ✘    | ✔     | ✔           |
| 管理管理员账户状态  | ✘    | ✘     | ✔           |
| 提升/降级用户权限  | ✘    | ✘     | ✔           |
| 系统级别配置     | ✘    | ✘     | ✔           |

---

#### 四、核心流程规范

1. **图书删除保护机制**：
   
   ```
   graph TD
      A[删除图书] --> B{检查副本状态}
      B -->|存在借出副本| C[终止操作并告警]
      B -->|全部在馆| D[解除作者关联]
      D --> E[删除所有副本]
      E --> F[删除书目]
   ```

2. **借阅生命周期**：
   
   - 借出：副本状态 → `BORROWED`
   - 归还：副本状态 → `AVAILABLE`
   - 超期：借阅记录标记`OVERDUE`（不阻塞归还）

3. **作者管理原则**：
   
   - 作者删除：当图书仍关联时禁止删除
   - 用户绑定：当作者关联用户时，需用户解绑才能删除

---

#### 五、特殊设计说明

1. **多种登录方式**：
   
   **谷歌登录集成**：
   
   ```
   用户流程：
   1. 前端调用Google OAuth
   2. 后端验证ID Token
   3. 系统自动注册/登录（存储google_id）
   4. 首次登录时提示补充姓名信息
   ```
   
   **邮箱注册登录**：
   
   ```
   注册流程：
   1. 用户输入邮箱、密码、姓名
   2. 系统验证邮箱格式和密码强度
   3. 发送邮箱验证码
   4. 用户验证邮箱后完成注册
   
   登录流程：
   1. 用户输入邮箱和密码
   2. 系统验证凭证
   3. 生成JWT Token完成登录
   ```

2. **实体操作边界**：
   
   - 普通用户：仅能操作自身数据
   - 普通管理员：具有图书和用户管理权限
   - 超级管理员：具有全局编辑权限，包括管理员权限管理
   - 作者实体：独立维护不影响用户系统

3. **系统初始化**：
   
   ```
   系统启动时的初始化流程：
   1. 检查是否存在超级管理员账户
   2. 如不存在，创建默认超级管理员账户
   3. 默认账户：admin@system.com / 系统生成密码
   4. 首次登录强制修改密码
   ```

## 数据库设计

### 数据库基本信息

- **数据库名称**：`book_manger`
- **字符集**：UTF8MB4
- **排序规则**：utf8mb4_unicode_ci
- **存储引擎**：InnoDB
- **建表语句**：[sql/schema.sql](sql/schema.sql)

### 数据表结构

#### 核心业务表

##### 1. 用户表 (users)

存储系统用户信息，支持Google OAuth和邮箱登录。

| 字段名         | 类型           | 约束                                                              | 说明              |
| ----------- | ------------ | --------------------------------------------------------------- | --------------- |
| id          | BIGINT       | PRIMARY KEY, AUTO_INCREMENT                                     | 用户ID            |
| name        | VARCHAR(100) | NOT NULL                                                        | 用户姓名            |
| email       | VARCHAR(255) | NOT NULL, UNIQUE                                                | 邮箱地址            |
| google_id   | VARCHAR(255) | UNIQUE                                                          | Google OAuth ID |
| password    | VARCHAR(255) | NULL                                                            | 密码(hash)        |
| role        | VARCHAR(64)  | NOT NULL, DEFAULT 'user'                                        | 用户角色            |
| admin_level | VARCHAR(64)  | NULL                                                            | 管理员级别           |
| join_date   | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP                             | 注册时间            |
| is_active   | TINYINT(1)   | NOT NULL, DEFAULT 1                                             | 账户状态            |
| create_time | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP                             | 创建时间            |
| update_time | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间            |
| deleted     | TINYINT(1)   | NOT NULL, DEFAULT 0                                             | 逻辑删除标志          |
| version     | INT          | NOT NULL, DEFAULT 1                                             | 乐观锁版本号          |

**索引**：

- `uk_email`：邮箱唯一索引
- `uk_google_id`：Google ID唯一索引

##### 2. 图书表 (books)

存储图书基本信息，以ISBN为主键。

| 字段名          | 类型           | 约束                                                              | 说明            |
| ------------ | ------------ | --------------------------------------------------------------- | ------------- |
| isbn         | VARCHAR(20)  | PRIMARY KEY                                                     | ISBN码（国际标准书号） |
| title        | VARCHAR(500) | NOT NULL                                                        | 书名            |
| publisher    | VARCHAR(200) | NOT NULL                                                        | 出版社           |
| publish_year | YEAR         | NOT NULL                                                        | 出版年份          |
| description  | TEXT         | NULL                                                            | 图书描述          |
| create_time  | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP                             | 创建时间          |
| update_time  | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间          |
| deleted      | TINYINT(1)   | NOT NULL, DEFAULT 0                                             | 逻辑删除标志        |
| version      | INT          | NOT NULL, DEFAULT 1                                             | 乐观锁版本号        |

##### 3. 作者表 (authors)

存储作者信息，支持与用户关联。

| 字段名         | 类型           | 约束                                                              | 说明         |
| ----------- | ------------ | --------------------------------------------------------------- | ---------- |
| id          | BIGINT       | PRIMARY KEY, AUTO_INCREMENT                                     | 作者ID       |
| name        | VARCHAR(100) | NOT NULL                                                        | 作者姓名       |
| bio         | TEXT         | NULL                                                            | 作者简介       |
| user_id     | BIGINT       | NULL                                                            | 关联用户ID（可选） |
| birth_date  | DATE         | NULL                                                            | 出生日期       |
| nationality | VARCHAR(100) | NULL                                                            | 国籍         |
| create_time | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP                             | 创建时间       |
| update_time | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间       |
| deleted     | TINYINT(1)   | NOT NULL, DEFAULT 0                                             | 逻辑删除标志     |
| version     | INT          | NOT NULL, DEFAULT 1                                             | 乐观锁版本号     |

##### 4. 图书副本表 (book_copies)

存储图书的物理副本信息。

| 字段名             | 类型           | 约束                                                              | 说明     |
| --------------- | ------------ | --------------------------------------------------------------- | ------ |
| copy_id         | BIGINT       | PRIMARY KEY, AUTO_INCREMENT                                     | 副本ID   |
| book_isbn       | VARCHAR(20)  | NOT NULL                                                        | 图书ISBN |
| status          | VARCHAR(20)  | NOT NULL, DEFAULT 'available'                                   | 副本状态   |
| location        | VARCHAR(200) | NULL                                                            | 存放位置   |
| condition_notes | TEXT         | NULL                                                            | 状态备注   |
| create_time     | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP                             | 创建时间   |
| update_time     | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间   |
| deleted         | TINYINT(1)   | NOT NULL, DEFAULT 0                                             | 逻辑删除标志 |
| version         | INT          | NOT NULL, DEFAULT 1                                             | 乐观锁版本号 |

**副本状态枚举**：

- `available`：可借阅
- `borrowed`：已借出
- `damaged`：损坏
- `lost`：丢失

##### 5. 借阅记录表 (borrow_records)

存储图书借阅的完整生命周期。

| 字段名           | 类型            | 约束                                                              | 说明     |
| ------------- | ------------- | --------------------------------------------------------------- | ------ |
| id            | BIGINT        | PRIMARY KEY, AUTO_INCREMENT                                     | 借阅记录ID |
| user_id       | BIGINT        | NOT NULL                                                        | 借阅用户ID |
| copy_id       | BIGINT        | NOT NULL                                                        | 图书副本ID |
| borrow_date   | DATETIME      | NOT NULL, DEFAULT CURRENT_TIMESTAMP                             | 借阅时间   |
| due_date      | DATETIME      | NOT NULL                                                        | 应还时间   |
| return_date   | DATETIME      | NULL                                                            | 实际归还时间 |
| status        | VARCHAR(20)   | NOT NULL, DEFAULT 'active'                                      | 借阅状态   |
| renewal_count | INT           | NOT NULL, DEFAULT 0                                             | 续借次数   |
| fine_amount   | DECIMAL(10,2) | NOT NULL, DEFAULT 0.00                                          | 罚金金额   |
| notes         | TEXT          | NULL                                                            | 备注信息   |
| create_time   | DATETIME      | NOT NULL, DEFAULT CURRENT_TIMESTAMP                             | 创建时间   |
| update_time   | DATETIME      | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间   |
| deleted       | TINYINT(1)    | NOT NULL, DEFAULT 0                                             | 逻辑删除标志 |
| version       | INT           | NOT NULL, DEFAULT 1                                             | 乐观锁版本号 |

**借阅状态枚举**：

- `active`：借阅中
- `returned`：已归还
- `overdue`：逾期

#### 关联表

##### 6. 图书作者关联表 (book_authors)

维护图书与作者的多对多关系。

| 字段名         | 类型          | 约束                                                              | 说明     |
| ----------- | ----------- | --------------------------------------------------------------- | ------ |
| id          | BIGINT      | PRIMARY KEY, AUTO_INCREMENT                                     | 关联ID   |
| book_isbn   | VARCHAR(20) | NOT NULL                                                        | 图书ISBN |
| author_id   | BIGINT      | NOT NULL                                                        | 作者ID   |
| create_time | DATETIME    | NOT NULL, DEFAULT CURRENT_TIMESTAMP                             | 创建时间   |
| update_time | DATETIME    | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间   |
| deleted     | TINYINT(1)  | NOT NULL, DEFAULT 0                                             | 逻辑删除标志 |

**索引**：

- `uk_book_author`：图书-作者联合唯一索引

#### 系统表

##### 7. 系统配置表 (system_configs)

存储系统级别配置参数。

| 字段名          | 类型           | 约束                                                              | 说明   |
| ------------ | ------------ | --------------------------------------------------------------- | ---- |
| id           | BIGINT       | PRIMARY KEY, AUTO_INCREMENT                                     | 配置ID |
| config_key   | VARCHAR(100) | NOT NULL, UNIQUE                                                | 配置键  |
| config_value | VARCHAR(255) | NOT NULL                                                        | 配置值  |
| config_type  | VARCHAR(20)  | NOT NULL, DEFAULT 'STRING'                                      | 配置类型 |
| description  | VARCHAR(500) | NULL                                                            | 配置描述 |
| create_time  | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP                             | 创建时间 |
| update_time  | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |
| is_enabled   | TINYINT(1)   | NOT NULL, DEFAULT 1                                             | 是否开启 |

**配置类型枚举**：

- `STRING`：字符串类型
- `NUMBER`：数值类型
- `BOOLEAN`：布尔类型

##### 8. 操作日志表 (operation_logs)

记录系统关键操作的审计日志。

| 字段名             | 类型           | 约束                                  | 说明       |
| --------------- | ------------ | ----------------------------------- | -------- |
| id              | BIGINT       | PRIMARY KEY, AUTO_INCREMENT         | 日志ID     |
| user_id         | BIGINT       | NULL                                | 操作用户ID   |
| operation_type  | VARCHAR(50)  | NOT NULL                            | 操作类型     |
| operation_desc  | VARCHAR(500) | NOT NULL                            | 操作描述     |
| target_type     | VARCHAR(50)  | NULL                                | 目标类型     |
| target_id       | VARCHAR(100) | NULL                                | 目标ID     |
| ip_address      | VARCHAR(45)  | NULL                                | IP地址     |
| user_agent      | VARCHAR(500) | NULL                                | 用户代理     |
| request_params  | TEXT         | NULL                                | 请求参数     |
| response_result | TEXT         | NULL                                | 响应结果     |
| execution_time  | INT          | NULL                                | 执行时间(ms) |
| create_time     | DATETIME     | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间     |

### 数据库视图

#### 1. 用户借阅统计视图 (user_borrow_stats)

提供用户借阅情况的统计信息。

```sql
CREATE VIEW `user_borrow_stats` AS
SELECT 
    u.id,
    u.name,
    u.email,
    COUNT(CASE WHEN br.status = 'ACTIVE' THEN 1 END) as active_borrows,
    COUNT(CASE WHEN br.status = 'RETURNED' THEN 1 END) as returned_borrows,
    COUNT(CASE WHEN br.status = 'OVERDUE' THEN 1 END) as overdue_borrows,
    COUNT(br.id) as total_borrows,
    SUM(br.fine_amount) as total_fines
FROM users u
LEFT JOIN borrow_records br ON u.id = br.user_id AND br.deleted = 0
WHERE u.deleted = 0
GROUP BY u.id, u.name, u.email;
```

#### 2. 图书库存统计视图 (book_inventory_stats)

提供图书库存情况的统计信息。

```sql
CREATE VIEW `book_inventory_stats` AS
SELECT 
    b.isbn,
    b.title,
    b.publisher,
    b.publish_year,
    COUNT(bc.copy_id) as total_copies,
    COUNT(CASE WHEN bc.status = 'AVAILABLE' THEN 1 END) as available_copies,
    COUNT(CASE WHEN bc.status = 'BORROWED' THEN 1 END) as borrowed_copies,
    COUNT(CASE WHEN bc.status = 'DAMAGED' THEN 1 END) as damaged_copies,
    COUNT(CASE WHEN bc.status = 'LOST' THEN 1 END) as lost_copies
FROM books b
LEFT JOIN book_copies bc ON b.isbn = bc.book_isbn AND bc.deleted = 0
WHERE b.deleted = 0
GROUP BY b.isbn, b.title, b.publisher, b.publish_year;
```

### 数据库设计特点

#### 1. 逻辑删除机制

所有业务表都采用逻辑删除设计，通过`deleted`字段标记删除状态，避免物理删除导致的数据丢失。

#### 2. 乐观锁机制

通过`version`字段实现乐观锁，防止并发更新时的数据冲突。

#### 3. 时间戳追踪

所有表都包含`create_time`和`update_time`字段，自动追踪数据的创建和更新时间。

### 初始化数据

系统包含以下初始化数据：

#### 默认系统配置

- `borrow_duration_days`: 30天（默认借阅期限）
- `max_borrow_count`: 5本（单用户最大借阅数量）
- `max_renewal_count`: 2次（单本书最大续借次数）
- `overdue_fine_per_day`: 10元（逾期罚金）
- `system_name`: 图书管理系统
- `system_version`: 1.0.0

#### 默认管理员账户

- **邮箱**: admin@system.com
- **密码**: admin123（首次登录需修改）
- **角色**: SUPER_ADMIN
- **状态**: 活跃
