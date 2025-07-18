-- 图书管理系统数据库建表语句
-- 创建时间: 2025-07-18
-- 数据库: book_manger
-- 字符集: UTF8MB4
-- 引擎: InnoDB

CREATE DATABASE IF NOT EXISTS `book_manger` 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE `book_manger`;

-- ===========================================
-- 用户表 (users)
-- ===========================================
CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `name` VARCHAR(100) NOT NULL COMMENT '用户姓名',
    `email` VARCHAR(255) NOT NULL COMMENT '邮箱地址',
    `google_id` VARCHAR(255) NULL COMMENT 'Google OAuth ID',
    `password` VARCHAR(255) NULL COMMENT '密码',
    `role` VARCHAR(64) NOT NULL DEFAULT 'user' COMMENT '用户角色 (admin, user, super_admin)',
    `admin_level` VARCHAR(64) NULL COMMENT '管理员级别',
    `join_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '账户状态（1:活跃 0:冻结）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标志',
    `version` INT NOT NULL DEFAULT 1 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_google_id` (`google_id`)
) COMMENT='用户表';

-- ===========================================
-- 图书表 (books)
-- ===========================================
CREATE TABLE `books` (
    `isbn` VARCHAR(20) NOT NULL COMMENT 'ISBN码（国际标准书号）',
    `title` VARCHAR(500) NOT NULL COMMENT '书名',
    `publisher` VARCHAR(200) NOT NULL COMMENT '出版社',
    `publish_year` YEAR NOT NULL COMMENT '出版年份',
    `description` TEXT NULL COMMENT '图书描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标志',
    `version` INT NOT NULL DEFAULT 1 COMMENT '乐观锁版本号',
    PRIMARY KEY (`isbn`)
) COMMENT='图书表';

-- ===========================================
-- 作者表 (authors)
-- ===========================================
CREATE TABLE `authors` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '作者ID',
    `name` VARCHAR(100) NOT NULL COMMENT '作者姓名',
    `bio` TEXT NULL COMMENT '作者简介',
    `user_id` BIGINT NULL COMMENT '关联用户ID（可选）',
    `birth_date` DATE NULL COMMENT '出生日期',
    `nationality` VARCHAR(100) NULL COMMENT '国籍',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标志',
    `version` INT NOT NULL DEFAULT 1 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`)
) COMMENT='作者表';

-- ===========================================
-- 图书副本表 (book_copies)
-- ===========================================
CREATE TABLE `book_copies` (
    `copy_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '副本ID',
    `book_isbn` VARCHAR(20) NOT NULL COMMENT '图书ISBN',
    `status` VARCHAR(20) NOT NULL DEFAULT 'available' COMMENT '副本状态',
    `location` VARCHAR(200) NULL COMMENT '存放位置',
    `condition_notes` TEXT NULL COMMENT '状态备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标志',
    `version` INT NOT NULL DEFAULT 1 COMMENT '乐观锁版本号',
    PRIMARY KEY (`copy_id`)
) COMMENT='图书副本表';

-- ===========================================
-- 借阅记录表 (borrow_records)
-- ===========================================
CREATE TABLE `borrow_records` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '借阅记录ID',
    `user_id` BIGINT NOT NULL COMMENT '借阅用户ID',
    `copy_id` BIGINT NOT NULL COMMENT '图书副本ID',
    `borrow_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅时间',
    `due_date` DATETIME NOT NULL COMMENT '应还时间',
    `return_date` DATETIME NULL COMMENT '实际归还时间',
    `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '借阅状态 (active, returned, overdue)',
    `renewal_count` INT NOT NULL DEFAULT 0 COMMENT '续借次数',
    `fine_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '罚金金额',
    `notes` TEXT NULL COMMENT '备注信息',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标志',
    `version` INT NOT NULL DEFAULT 1 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`)
) COMMENT='借阅记录表';

-- ===========================================
-- 图书作者关联表 (book_authors)
-- ===========================================
CREATE TABLE `book_authors` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `book_isbn` VARCHAR(20) NOT NULL COMMENT '图书ISBN',
    `author_id` BIGINT NOT NULL COMMENT '作者ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标志',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_book_author` (`book_isbn`, `author_id`)
) COMMENT='图书作者关联表';

-- ===========================================
-- 系统配置表 (system_configs)
-- ===========================================
CREATE TABLE `system_configs` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` varchar(255) NOT NULL COMMENT '配置值',
    `config_type` varchar(20) NOT NULL DEFAULT 'STRING' COMMENT '配置类型 (string, number, boolean)',
    `description` VARCHAR(500) NULL COMMENT '配置描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_enabled` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否开启（1:开启 0:关闭）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) COMMENT='系统配置表';

-- ===========================================
-- 操作日志表 (operation_logs)
-- ===========================================
CREATE TABLE `operation_logs` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT NULL COMMENT '操作用户ID',
    `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
    `operation_desc` VARCHAR(500) NOT NULL COMMENT '操作描述',
    `target_type` VARCHAR(50) NULL COMMENT '目标类型',
    `target_id` VARCHAR(100) NULL COMMENT '目标ID',
    `ip_address` VARCHAR(45) NULL COMMENT 'IP地址',
    `user_agent` VARCHAR(500) NULL COMMENT '用户代理',
    `request_params` TEXT NULL COMMENT '请求参数',
    `response_result` TEXT NULL COMMENT '响应结果',
    `execution_time` INT NULL COMMENT '执行时间(ms)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) COMMENT='操作日志表';

-- ===========================================
-- 初始化数据
-- ===========================================

-- 插入默认系统配置
INSERT INTO `system_configs` (`config_key`, `config_value`, `config_type`, `description`, `is_system`) VALUES
('borrow_duration_days', '30', 'NUMBER', '默认借阅期限（天）', 1),
('max_borrow_count', '5', 'NUMBER', '单用户最大借阅数量', 1),
('max_renewal_count', '2', 'NUMBER', '单本书最大续借次数', 1),
('overdue_fine_per_day', '0.50', 'NUMBER', '逾期罚金（元/天）', 1),
('system_name', '图书管理系统', 'STRING', '系统名称', 1),
('system_version', '1.0.0', 'STRING', '系统版本', 1);

-- 插入默认超级管理员账户
INSERT INTO `users` (
    `name`, 
    `email`, 
    `password_hash`, 
    `role`, 
    `admin_level`, 
    `is_active`
) VALUES (
    'System Administrator', 
    'admin@system.com', 
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EH.za7iWQGqkRrHqOLyMN2', -- 默认密码：admin123
    'SUPER_ADMIN', 
    'SUPER_ADMIN', 
    1
);

-- ===========================================
-- 创建视图
-- ===========================================

-- 用户借阅统计视图
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

-- 图书库存统计视图
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


