/*
 Navicat Premium Data Transfer

 Source Server         : linux
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 192.168.111.115:3306
 Source Schema         : yygh_user

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 06/06/2023 17:35:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for patient
-- ----------------------------
DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `certificates_type` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件类型',
  `certificates_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件编号',
  `sex` tinyint(3) NULL DEFAULT NULL COMMENT '性别',
  `birthdate` date NULL DEFAULT NULL COMMENT '出生年月',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `is_marry` tinyint(3) NULL DEFAULT NULL COMMENT '是否结婚',
  `province_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省code',
  `city_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市code',
  `district_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区code',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详情地址',
  `contacts_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `contacts_certificates_type` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人证件类型',
  `contacts_certificates_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人证件号',
  `contacts_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人手机',
  `card_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '就诊卡号',
  `is_insure` tinyint(3) NULL DEFAULT 0 COMMENT '是否有医保',
  `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '状态（0：默认 1：已认证）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '就诊人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of patient
-- ----------------------------
INSERT INTO `patient` VALUES (4, 26, '1', '10', '1', 1, '2021-07-31', '1', 0, '110000', '110100', '110101', '1', '', '', '', '', NULL, 0, 0, '2021-08-01 20:13:11', '2021-08-01 20:13:11', 0);
INSERT INTO `patient` VALUES (5, 26, '2', '10', '2', 1, '2021-07-31', '2', 0, '140000', '140100', '140101', '2', '', '', '', '', NULL, 0, 0, '2021-08-01 20:13:25', '2021-08-01 20:13:25', 0);
INSERT INTO `patient` VALUES (6, 26, '3', '10', '3', 1, '2021-07-31', '3', 0, '110000', '110100', '110101', '3', '', '', '', '', NULL, 0, 0, '2021-08-01 20:13:39', '2021-08-01 20:13:39', 0);
INSERT INTO `patient` VALUES (7, 50, '张翠山', '10', '123456789099', 1, '2021-07-31', '15611248741', 0, '110000', '110100', '110101', '北京市昌平区', '测试1', '10', '222', '111', NULL, 0, 0, '2021-08-04 10:23:22', '2023-05-29 10:57:23', 0);
INSERT INTO `patient` VALUES (8, 50, '蔡徐困', '10', '2771899290798786211', 1, '2011-05-09', '15922778371', 1, '320000', '320100', '320104', '公园旁边', '王为', '10', '2796819798327819', '18273772102', NULL, 1, 0, '2023-05-27 16:18:52', '2023-05-29 10:54:59', 0);
INSERT INTO `patient` VALUES (9, 50, '张三丰', '10', '2378975893923425', 1, '2023-06-12', '18239921222', 1, '430000', '430800', '430802', '大勇桥公园', '王大', '10', '21879843093231231', '17723992312', NULL, 0, 0, '2023-06-01 11:23:49', '2023-06-01 11:39:46', 1);
INSERT INTO `patient` VALUES (10, 50, '李四', '10', '24872398473898742', 1, '2023-06-05', '18924390299', 1, '430000', '430800', '430802', '公园', '王五', '10', '281439489032840351', '18273294893', NULL, 1, 0, '2023-06-01 11:32:10', '2023-06-01 11:32:16', 1);
INSERT INTO `patient` VALUES (11, 50, '李四', '10', '2839214073897384', 1, '2023-06-03', '18922232834', 1, '430000', '430800', '430802', '公园', '王五', '10', '2173489237934312313', '18923321783', NULL, 1, 0, '2023-06-01 11:39:23', '2023-06-01 11:39:23', 0);

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `openid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信openid',
  `nick_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `certificates_type` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件类型',
  `certificates_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件编号',
  `certificates_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件路径',
  `auth_status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '认证状态（0：未认证 1：认证中 2：认证成功 -1：认证失败）',
  `status` tinyint(3) NOT NULL DEFAULT 1 COMMENT '状态（0：锁定 1：正常）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uk_mobile`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (40, NULL, 'tom', '15927800538', '汤姆', NULL, NULL, NULL, 1, 1, '2023-05-27 17:03:19', '2023-05-28 16:50:47', 0);
INSERT INTO `user_info` VALUES (41, NULL, 'jerry', '15927800539', '杰瑞', NULL, NULL, NULL, 1, 1, '2023-05-27 17:03:40', '2023-05-28 16:50:45', 0);
INSERT INTO `user_info` VALUES (42, NULL, 'jay', '15927800540', '周杰伦', NULL, NULL, NULL, 1, 0, '2023-05-27 17:03:52', '2023-05-28 16:50:55', 0);
INSERT INTO `user_info` VALUES (43, NULL, 'qi', '15927800541', '邓紫棋', NULL, NULL, NULL, 0, 0, '2023-05-27 17:04:16', '2023-05-27 17:04:16', 0);
INSERT INTO `user_info` VALUES (44, NULL, 'yu', '15927800542', '贾宝玉', NULL, NULL, NULL, 0, 1, '2023-05-27 17:04:33', '2023-05-27 17:04:33', 0);
INSERT INTO `user_info` VALUES (45, NULL, 'ken', '15927800543', '林肯', NULL, NULL, NULL, 0, 0, '2023-05-27 17:04:58', '2023-05-27 17:04:58', 0);
INSERT INTO `user_info` VALUES (46, NULL, 'hua', '15927800544', '刘德华', NULL, NULL, NULL, 0, 1, '2023-05-27 17:05:16', '2023-05-27 17:05:16', 0);
INSERT INTO `user_info` VALUES (47, NULL, 'guo', '15927800545', '陆小果', NULL, NULL, NULL, 0, 0, '2023-05-27 17:05:41', '2023-05-27 17:05:41', 0);
INSERT INTO `user_info` VALUES (48, NULL, 'dongmei', '15927800546', '马冬梅', NULL, NULL, NULL, 0, 1, '2023-05-27 17:06:02', '2023-05-27 17:06:02', 0);
INSERT INTO `user_info` VALUES (49, NULL, 'fa', '15927800547', '王德发', NULL, NULL, NULL, 0, 1, '2023-05-27 17:12:18', '2023-05-27 17:12:18', 0);
INSERT INTO `user_info` VALUES (50, 'o3_SC57U6UukolVrdzoVpaUzku9A', NULL, '15927800537', '王大锤', '身份证', '388741936278613841223', '', 2, 1, '2023-05-27 11:40:24', '2023-05-27 17:03:14', 0);

-- ----------------------------
-- Table structure for user_login_record
-- ----------------------------
DROP TABLE IF EXISTS `user_login_record`;
CREATE TABLE `user_login_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户登录记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_login_record
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
