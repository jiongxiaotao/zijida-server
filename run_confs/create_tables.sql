/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : zijida

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 29/04/2019 11:17:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `union_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '微信开放平台联合id',
  `open_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户唯一id',
  `cus_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户关注状态：9-已关注',
  `province` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所在省份',
  `city` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所在城市',
  `avatarUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像url',
  `update_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `max_count` int(10) NOT NULL DEFAULT 5 COMMENT '最大项目个数',
  PRIMARY KEY (`union_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_session
-- ----------------------------
DROP TABLE IF EXISTS `t_session`;
CREATE TABLE `t_session`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `union_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `open_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `session_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `login_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `login_status` char(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `session_user_id`(`union_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户表外键',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态：1-未发布，2-已发布，3-已开放评分，9-已结束',
  `invite_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邀请码，用户进入时的密码',
  `amount` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '预计收集量',
  `done_amount` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '已完成评分个数',
  `create_time` datetime(6) NULL DEFAULT NULL COMMENT '新建时间',
  `update_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '项目最近更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `project_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `project_user_id` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`union_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_subject
-- ----------------------------
DROP TABLE IF EXISTS `t_subject`;
CREATE TABLE `t_subject`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` bigint(11) NOT NULL COMMENT '所在项目id',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评分项内容',
  `max_score` int(10) NULL DEFAULT NULL COMMENT '最大分数',
  `create_time` timestamp(6) NULL DEFAULT NULL COMMENT '生成时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `subject_project_id`(`project_id`) USING BTREE,
  CONSTRAINT `subject_project_id` FOREIGN KEY (`project_id`) REFERENCES `t_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_votee
-- ----------------------------
DROP TABLE IF EXISTS `t_votee`;
CREATE TABLE `t_votee`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` bigint(10) NOT NULL COMMENT '所在项目id',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被评人名称',
  `create_time` timestamp(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `votee_project_id`(`project_id`) USING BTREE,
  CONSTRAINT `votee_project_id` FOREIGN KEY (`project_id`) REFERENCES `t_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_score
-- ----------------------------
DROP TABLE IF EXISTS `t_score`;
CREATE TABLE `t_score`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `subject_id` bigint(10) NOT NULL,
  `votee_id` bigint(10) NOT NULL,
  `score` int(10) NULL DEFAULT NULL COMMENT '某人给某评分项某被评人打的分数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `score_user_id`(`user_id`) USING BTREE,
  INDEX `score_subject_id`(`subject_id`) USING BTREE,
  INDEX `score_votee_id`(`votee_id`) USING BTREE,
  CONSTRAINT `score_subject_id` FOREIGN KEY (`subject_id`) REFERENCES `t_subject` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `score_user_id` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`union_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `score_votee_id` FOREIGN KEY (`votee_id`) REFERENCES `t_votee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '留言用户id',
  `type` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0:语音留言',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '语音文件名',
  `create_time` timestamp(6) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '留言时间',
  `duration` int(10) NULL DEFAULT NULL COMMENT '时长秒',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `message_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `message_user_id` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`union_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;