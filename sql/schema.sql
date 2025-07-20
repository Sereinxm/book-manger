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
                         `salt` VARCHAR(255) NULL COMMENT '密码盐值',
                         `role` VARCHAR(64) NOT NULL DEFAULT 'user' COMMENT '用户角色 (admin, user, super_admin)',
                         `join_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                         `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '账户状态（1:活跃 0:冻结）',
                         `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `uk_email` (`email`),
                         UNIQUE KEY `uk_google_id` (`google_id`)
) COMMENT='用户表';

-- ===========================================
-- 图书表 (books)
-- ===========================================
create table books
(
    isbn         varchar(20)                          not null comment 'ISBN码（国际标准书号）',
    title        varchar(500)                         not null comment '书名',
    publisher    varchar(200)                         not null comment '出版社',
    publish_year date                                 not null comment '出版年份',
    description  text                                 null comment '图书描述',
    author_id    bigint                               not null comment '作者ID',
    create_time  datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted      tinyint(1) default 0                 not null comment '逻辑删除标志',
    version      int        default 1                 not null comment '乐观锁版本号',
    id           bigint auto_increment
        primary key
)
    comment '图书表';

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
                                  PRIMARY KEY (`id`)
) COMMENT='借阅记录表';


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




