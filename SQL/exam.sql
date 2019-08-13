/*
Navicat MySQL Data Transfer

Source Server         : xxhb
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : exam

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2019-08-13 09:19:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for options
-- ----------------------------
DROP TABLE IF EXISTS `options`;
CREATE TABLE `options` (
  `options_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `op` varchar(10) NOT NULL COMMENT '选项',
  `content` text NOT NULL COMMENT '选项内容',
  `question_bank_id` int(11) NOT NULL,
  PRIMARY KEY (`options_id`)
) ENGINE=InnoDB AUTO_INCREMENT=455 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of options
-- ----------------------------

-- ----------------------------
-- Table structure for question_bank
-- ----------------------------
DROP TABLE IF EXISTS `question_bank`;
CREATE TABLE `question_bank` (
  `question_bank_id` int(11) NOT NULL AUTO_INCREMENT,
  `stem` text COMMENT '题目',
  `answer` varchar(255) DEFAULT NULL COMMENT '正确答案',
  `tests_type` int(5) DEFAULT NULL COMMENT '题目类型（判断 0或者选择 1）',
  `state` int(5) DEFAULT NULL COMMENT '状态 0 删除 1有效',
  PRIMARY KEY (`question_bank_id`)
) ENGINE=InnoDB AUTO_INCREMENT=239 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of question_bank
-- ----------------------------

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `score_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分数ID',
  `users_id` varchar(30) DEFAULT NULL COMMENT '用户ID',
  `testpaper_id` int(11) DEFAULT NULL,
  `fraction` double(255,0) DEFAULT NULL COMMENT '分数',
  `img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`score_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of score
-- ----------------------------

-- ----------------------------
-- Table structure for testpaper
-- ----------------------------
DROP TABLE IF EXISTS `testpaper`;
CREATE TABLE `testpaper` (
  `testpaper_id` int(11) NOT NULL AUTO_INCREMENT,
  `testpaper_name` varchar(255) NOT NULL COMMENT '试卷名',
  `testpaper_state` int(5) NOT NULL COMMENT '试卷状态 0为无效  1为有效',
  `start_date` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_date` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',
  `is_start` int(5) DEFAULT '1' COMMENT '是否开始考试 0 关闭 1开始',
  PRIMARY KEY (`testpaper_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10014 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of testpaper
-- ----------------------------

-- ----------------------------
-- Table structure for testpaper_tests
-- ----------------------------
DROP TABLE IF EXISTS `testpaper_tests`;
CREATE TABLE `testpaper_tests` (
  `testpaper_tests_id` int(11) NOT NULL AUTO_INCREMENT,
  `testpaper_id` int(11) DEFAULT NULL,
  `question_bank_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`testpaper_tests_id`)
) ENGINE=InnoDB AUTO_INCREMENT=289 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of testpaper_tests
-- ----------------------------

-- ----------------------------
-- Table structure for times
-- ----------------------------
DROP TABLE IF EXISTS `times`;
CREATE TABLE `times` (
  `times_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `testpaper_id` int(11) NOT NULL COMMENT '试卷_id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `data_min` double DEFAULT NULL,
  `times_state` int(5) DEFAULT NULL COMMENT '数据状态',
  PRIMARY KEY (`times_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of times
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` varchar(30) NOT NULL COMMENT '用户id',
  `user_name` varchar(25) NOT NULL COMMENT '用户真实姓名',
  `user_pass` varchar(25) NOT NULL COMMENT '登陆密码',
  `permission` int(5) NOT NULL COMMENT '用户权限',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('10000', '管理员', 'admin', '1');
INSERT INTO `users` VALUES ('10001', '王谦', 'admin', '0');
