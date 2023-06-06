/*
 Navicat Premium Data Transfer

 Source Server         : linux
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 192.168.111.115:3306
 Source Schema         : yygh_hosp

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 06/06/2023 17:35:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hospital_set
-- ----------------------------
DROP TABLE IF EXISTS `hospital_set`;
CREATE TABLE `hospital_set`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `hosname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院名称',
  `hoscode` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院编号',
  `api_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'api基础路径',
  `sign_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名秘钥',
  `contacts_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contacts_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人手机',
  `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_hoscode`(`hoscode`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '医院设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hospital_set
-- ----------------------------
INSERT INTO `hospital_set` VALUES (1, '解放军总医院', '10000', '9998', '1', '张参先', NULL, 0, '2021-07-29 15:18:08', '2023-05-16 16:11:07', 0);
INSERT INTO `hospital_set` VALUES (44, '同济大医院', 'HOS001', 'http://api.example.com', 'signkey1', '张德萨', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-09 09:26:30', 1);
INSERT INTO `hospital_set` VALUES (45, '北京协和医院', 'HOS002', 'http://api.example.com', 'signkey2', '李喜第', '1234567890', 0, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (46, '上海交通大学医学院附属瑞金医院', 'HOS021', 'http://api.example.com', '安东尼', '联系人3', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:48:58', 1);
INSERT INTO `hospital_set` VALUES (47, '中国医学科学院肿瘤医院', 'HOS003', 'http://api.example.com', 'signkey3', '阿森纳', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (48, '复旦大学附属华山医院', 'HOS004', 'http://api.example.com', 'signkey3', '陈飒', '1234567890', 0, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (49, '浙江大学医学院附属第一医院', 'HOS005', 'http://api.example.com', 'signkey3', '王笑', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (50, '广州医科大学附属第一医院', 'HOS006', 'http://api.example.com', 'signkey3', '陈伟', '1234567890', 1, '2023-05-08 12:16:14', '2023-06-01 11:42:14', 1);
INSERT INTO `hospital_set` VALUES (51, '武汉大学人民医院', 'HOS007', 'http://api.example.com', 'signkey3', '刘易斯', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (52, '南京医科大学第一附属医院', 'HOS008', 'http://api.example.com', 'signkey3', '王设', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (53, '中山大学附属第一医院', 'HOS009', 'http://api.example.com', 'signkey3', '赖里斯', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (54, '中山大学附属第一医院', 'HOS010', 'http://api.example.com', 'signkey3', '何漫', '1234567890', 1, '2023-05-08 12:16:14', '2023-06-01 11:42:14', 1);
INSERT INTO `hospital_set` VALUES (55, '重庆医科大学附属第一医院', 'HOS011', 'http://api.example.com', 'signkey3', '李绢', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (56, '西安交通大学第一附属医院', 'HOS012', 'http://api.example.com', 'signkey3', '张诗', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (57, '吉林大学第一医院', 'HOS013', 'http://api.example.com', 'signkey3', '奥维', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (58, '天津医科大学总医院', 'HOS014', 'http://api.example.com', 'signkey3', '史丽', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (59, '湖南大学湘雅医院', 'HOS015', 'http://api.example.com', 'signkey3', '杨春雪', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (60, '郑州大学第一附属医院', 'HOS016', 'http://api.example.com', 'signkey3', '武户', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (61, '河北医科大学第四医院', 'HOS017', 'http://api.example.com', 'signkey3', '张呼', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (62, '黑龙江大学医学院附属第一医院', 'HOS018', 'http://api.example.com', 'signkey3', '王刚', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (63, '贵州医科大学附属医院', 'HOS019', 'http://api.example.com', 'signkey3', '李继', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (64, '山东大学齐鲁医院', 'HOS020', 'http://api.example.com', 'signkey20', '奥申', '1234567890', 1, '2023-05-08 12:16:14', '2023-05-08 12:16:14', 0);
INSERT INTO `hospital_set` VALUES (65, '安贞医院', '10001', 'http://localhost:9001/', '11cf25d7ab7dc8b835f3f6b44a735188', '李无锡', '19277182399', 1, '2023-05-08 16:12:52', '2023-05-08 16:12:52', 0);
INSERT INTO `hospital_set` VALUES (66, '天坛医院', '10002', 'http://localhost:9821/ttyy', '062162187c7d06c35f156c55a8b721e8', '张立宪', '19277320981', 1, '2023-05-08 16:16:57', '2023-05-08 16:16:57', 0);

SET FOREIGN_KEY_CHECKS = 1;
