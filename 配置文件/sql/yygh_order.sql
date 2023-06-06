/*
 Navicat Premium Data Transfer

 Source Server         : linux
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 192.168.111.115:3306
 Source Schema         : yygh_order

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 06/06/2023 17:35:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NULL DEFAULT NULL,
  `out_trade_no` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单交易号',
  `hoscode` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院编号',
  `hosname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院名称',
  `depcode` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科室编号',
  `depname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科室名称',
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医生职称',
  `hos_schedule_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排班编号（医院自己的排班主键）',
  `reserve_date` date NULL DEFAULT NULL COMMENT '安排日期',
  `reserve_time` tinyint(3) NULL DEFAULT 0 COMMENT '安排时间（0：上午 1：下午）',
  `patient_id` bigint(20) NULL DEFAULT NULL COMMENT '就诊人id',
  `patient_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '就诊人名称',
  `patient_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '就诊人手机',
  `hos_record_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预约记录唯一标识（医院预约记录主键）',
  `number` int(11) NULL DEFAULT NULL COMMENT '预约号序',
  `fetch_time` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '建议取号时间',
  `fetch_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '取号地点',
  `amount` decimal(10, 0) NULL DEFAULT NULL COMMENT '医事服务费',
  `quit_time` datetime NULL DEFAULT NULL COMMENT '退号时间',
  `order_status` tinyint(3) NULL DEFAULT NULL COMMENT '订单状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_out_trade_no`(`out_trade_no`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_hoscode`(`hoscode`) USING BTREE,
  INDEX `idx_hos_schedule_id`(`hos_schedule_id`) USING BTREE,
  INDEX `idx_hos_record_id`(`hos_record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES (19, 50, '168552701867761', '10000', '北京协和医院', '200040878', '多发性硬化专科门诊', '副主任医师', '32', '2023-06-01', 0, 7, '张翠山', '15611248741', '17', 35, '2023-06-0109:00前', '一层114窗口', 100, '2023-05-31 15:30:00', 0, '2023-05-31 17:56:57', '2023-05-31 17:56:57', 0);
INSERT INTO `order_info` VALUES (20, 50, '16855895144294', '10000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '34', '2023-06-02', 0, 7, '张翠山', '15611248741', '18', 12, '2023-06-0209:00前', '一层114窗口', 100, '2023-06-01 15:30:00', 0, '2023-06-01 11:18:34', '2023-06-01 11:18:34', 0);
INSERT INTO `order_info` VALUES (21, 50, '168558990797749', '10000', '北京协和医院', '200040878', '多发性硬化专科门诊', '副主任医师', '33', '2023-06-01', 1, 9, '张三丰', '18239921222', '19', 18, '2023-06-0109:00前', '一层114窗口', 100, '2023-05-31 15:30:00', 0, '2023-06-01 11:25:07', '2023-06-01 11:25:07', 0);
INSERT INTO `order_info` VALUES (22, 50, '168559083951367', '10000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '37', '2023-06-03', 0, 11, '李四', '18922232834', '20', 12, '2023-06-0309:00前', '一层114窗口', 100, '2023-06-02 15:30:00', 0, '2023-06-01 11:40:38', '2023-06-01 11:40:38', 0);
INSERT INTO `order_info` VALUES (23, 50, '168597111485146', '10000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '46', '2023-06-06', 0, 8, '蔡徐困', '15922778371', '21', 12, '2023-06-0609:00前', '一层114窗口', 100, '2023-06-05 15:30:00', 0, '2023-06-05 21:18:35', '2023-06-05 21:18:35', 0);
INSERT INTO `order_info` VALUES (24, 50, '168597135877077', '10000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '46', '2023-06-06', 0, 7, '张翠山', '15611248741', '22', 13, '2023-06-0609:00前', '一层114窗口', 100, '2023-06-05 15:30:00', 0, '2023-06-05 21:22:38', '2023-06-05 21:22:38', 0);
INSERT INTO `order_info` VALUES (25, 50, '168597162534483', '10000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '46', '2023-06-06', 0, 8, '蔡徐困', '15922778371', '23', 14, '2023-06-0609:00前', '一层114窗口', 100, '2023-06-05 15:30:00', 0, '2023-06-05 21:27:05', '2023-06-05 21:27:05', 0);
INSERT INTO `order_info` VALUES (26, 50, '168597173878883', '10000', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '46', '2023-06-06', 0, 7, '张翠山', '15611248741', '24', 15, '2023-06-0609:00前', '一层114窗口', 100, '2023-06-05 15:30:00', 0, '2023-06-05 21:28:58', '2023-06-05 21:28:58', 0);

-- ----------------------------
-- Table structure for payment_info
-- ----------------------------
DROP TABLE IF EXISTS `payment_info`;
CREATE TABLE `payment_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_trade_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对外业务编号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `payment_type` tinyint(1) NULL DEFAULT NULL COMMENT '支付类型（微信 支付宝）',
  `trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易编号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `subject` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易内容',
  `payment_status` tinyint(3) NULL DEFAULT NULL COMMENT '支付状态',
  `callback_time` datetime NULL DEFAULT NULL COMMENT '回调时间',
  `callback_content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_out_trade_no`(`out_trade_no`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_info
-- ----------------------------
INSERT INTO `payment_info` VALUES (8, '162825325628088', 14, 2, '4200001195202108062597287481', 100.00, '2021-08-07|北京协和医院|多发性硬化专科门诊|副主任医师', 2, '2021-08-06 20:34:34', '{transaction_id=4200001195202108062597287481, nonce_str=77VOukaGjAUQ6vwg, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuPGPKdUeOuzT0NB3vdfkjMA, sign=FF75F931A9A0A05DB1683051DBCE695B, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=162825325628088, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20210806203445, is_subscribe=N, return_code=SUCCESS}', '2021-08-06 20:34:18', '2021-08-06 20:34:33', 0);
INSERT INTO `payment_info` VALUES (9, '162829645831986', 15, 2, '4200001176202108075220715918', 100.00, '2021-08-08|北京协和医院|多发性硬化专科门诊|副主任医师', 2, '2021-08-07 08:35:17', '{transaction_id=4200001176202108075220715918, nonce_str=m9SHs0Lrvz6e2jZj, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuPGPKdUeOuzT0NB3vdfkjMA, sign=C1156F9B91B99A2B959F670E2E2C68E6, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=162829645831986, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20210807083527, is_subscribe=N, return_code=SUCCESS}', '2021-08-07 08:35:04', '2021-08-07 08:35:16', 0);
INSERT INTO `payment_info` VALUES (10, '162830304778619', 16, 2, '4200001155202108076308258973', 100.00, '2021-08-08|北京协和医院|多发性硬化专科门诊|副主任医师', 2, '2021-08-07 10:24:32', '{transaction_id=4200001155202108076308258973, nonce_str=RrV4VR2nnrVrIfGH, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuPGPKdUeOuzT0NB3vdfkjMA, sign=E3CE4A31F2C7C72F3B767CA2E7A5F58B, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=162830304778619, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20210807102444, is_subscribe=N, return_code=SUCCESS}', '2021-08-07 10:24:22', '2021-08-07 10:24:32', 0);
INSERT INTO `payment_info` VALUES (11, '16298108217849', 17, 2, NULL, 100.00, '2021-08-26|北京协和医院|多发性硬化专科门诊|副主任医师', 1, NULL, NULL, '2021-08-24 21:13:46', '2021-08-24 21:13:45', 0);
INSERT INTO `payment_info` VALUES (12, '162985687872461', 18, 2, NULL, 100.00, '2021-08-26|北京协和医院|多发性硬化专科门诊|副主任医师', 1, NULL, NULL, '2021-08-25 10:01:28', '2021-08-25 10:01:28', 0);
INSERT INTO `payment_info` VALUES (13, '168597111485146', 23, 2, NULL, 100.00, '2023-06-06|北京协和医院|多发性硬化专科门诊|医师', 1, NULL, NULL, '2023-06-05 21:18:42', '2023-06-05 21:18:41', 0);
INSERT INTO `payment_info` VALUES (14, '168597135877077', 24, 2, NULL, 100.00, '2023-06-06|北京协和医院|多发性硬化专科门诊|医师', 1, NULL, NULL, '2023-06-05 21:22:44', '2023-06-05 21:22:43', 0);
INSERT INTO `payment_info` VALUES (15, '168597162534483', 25, 2, NULL, 100.00, '2023-06-06|北京协和医院|多发性硬化专科门诊|医师', 1, NULL, NULL, '2023-06-05 21:27:08', '2023-06-05 21:27:08', 0);
INSERT INTO `payment_info` VALUES (16, '168597173878883', 26, 2, NULL, 100.00, '2023-06-06|北京协和医院|多发性硬化专科门诊|医师', 1, NULL, NULL, '2023-06-05 21:29:01', '2023-06-05 21:29:01', 0);

-- ----------------------------
-- Table structure for refund_info
-- ----------------------------
DROP TABLE IF EXISTS `refund_info`;
CREATE TABLE `refund_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对外业务编号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单编号',
  `payment_type` tinyint(3) NULL DEFAULT NULL COMMENT '支付类型（微信 支付宝）',
  `trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易编号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `subject` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易内容',
  `refund_status` tinyint(3) NULL DEFAULT NULL COMMENT '退款状态',
  `callback_content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调信息',
  `callback_time` datetime NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_out_trade_no`(`out_trade_no`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退款信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_info
-- ----------------------------
INSERT INTO `refund_info` VALUES (3, '162829645831986', 15, 2, NULL, 100.00, '2021-08-08|北京协和医院|多发性硬化专科门诊|副主任医师', 1, NULL, NULL, '2021-08-07 10:20:04', '2021-08-07 10:20:04', 0);
INSERT INTO `refund_info` VALUES (4, '162830304778619', 16, 2, '50301609052021080711349778191', 100.00, '2021-08-08|北京协和医院|多发性硬化专科门诊|副主任医师', 2, '{\"transaction_id\":\"4200001155202108076308258973\",\"nonce_str\":\"bmu2Rk0tlrjJirQ2\",\"out_refund_no\":\"tk162830304778619\",\"sign\":\"9BDCCE9B82D4304E5C3A42C658C0DC0D\",\"return_msg\":\"OK\",\"mch_id\":\"1558950191\",\"refund_id\":\"50301609052021080711349778191\",\"cash_fee\":\"1\",\"out_trade_no\":\"162830304778619\",\"coupon_refund_fee\":\"0\",\"refund_channel\":\"\",\"appid\":\"wx74862e0dfcf69954\",\"refund_fee\":\"1\",\"total_fee\":\"1\",\"result_code\":\"SUCCESS\",\"coupon_refund_count\":\"0\",\"cash_refund_fee\":\"1\",\"return_code\":\"SUCCESS\"}', '2021-08-07 10:25:27', '2021-08-07 10:25:25', '2021-08-07 10:25:27', 0);

SET FOREIGN_KEY_CHECKS = 1;
