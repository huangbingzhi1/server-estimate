/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.5.62 : Database - server_estimate
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `base_company` */

CREATE TABLE `base_company` (
  `company_id` varchar(36) NOT NULL,
  `company_code` varchar(50) DEFAULT NULL,
  `company_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `base_enterprise` */

CREATE TABLE `base_enterprise` (
  `enterprise_id` varchar(36) NOT NULL,
  `cis` varchar(50) DEFAULT NULL COMMENT '商家cis系统编码',
  `enterprise_name` varchar(100) DEFAULT NULL COMMENT '商家名称',
  `office` varchar(100) DEFAULT NULL COMMENT '办事处',
  `company_id` varchar(36) DEFAULT NULL,
  `company_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`enterprise_id`),
  KEY `cis_index` (`cis`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `base_role` */

CREATE TABLE `base_role` (
  `role_id` varchar(36) NOT NULL,
  `role_name` varchar(100) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `base_server` */

CREATE TABLE `base_server` (
  `server_id` varchar(36) NOT NULL,
  `server_code` varchar(50) DEFAULT NULL,
  `server_name` varchar(200) DEFAULT NULL,
  `company_name` varchar(50) DEFAULT NULL,
  `server_type` varchar(50) DEFAULT NULL,
  `manager` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `district` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`server_id`),
  KEY `server_code_index` (`server_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `base_user` */

CREATE TABLE `base_user` (
  `user_id` varchar(36) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `truename` varchar(50) DEFAULT NULL,
  `password` varchar(36) DEFAULT NULL,
  `company` varchar(36) DEFAULT NULL,
  `role_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name_unique` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `error_exam_detail` */

CREATE TABLE `error_exam_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `exam_result` longtext,
  `error_msg` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `exam_detail` */

CREATE TABLE `exam_detail` (
  `detail_id` varchar(36) NOT NULL,
  `main_qid` varchar(36) DEFAULT NULL,
  `enterprise_cis` varchar(50) DEFAULT NULL,
  `server_code` varchar(50) DEFAULT NULL,
  `submittime` datetime DEFAULT NULL,
  `timetaken` decimal(9,0) DEFAULT NULL,
  `score_array` varchar(300) DEFAULT NULL,
  `text_array` varchar(4000) DEFAULT NULL,
  `totle_score` decimal(10,2) DEFAULT NULL,
  `mean_score` decimal(10,2) DEFAULT NULL,
  `source_data` longtext,
  PRIMARY KEY (`detail_id`),
  KEY `detail_cis_index` (`enterprise_cis`),
  KEY `detail_qid_index` (`main_qid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `exam_main` */

CREATE TABLE `exam_main` (
  `main_id` varchar(36) NOT NULL,
  `exam_qid` varchar(36) DEFAULT NULL,
  `exam_name` varchar(200) DEFAULT NULL,
  `exam_domain` varchar(200) DEFAULT 'https://www.wjx.top/jq/',
  `begindate` datetime DEFAULT NULL,
  `answercount` decimal(10,0) DEFAULT NULL,
  `status` varchar(36) DEFAULT NULL COMMENT '开启，关闭，失效',
  `score_type_indexs` varchar(300) DEFAULT NULL COMMENT '得分题目序号，如果有多个，用逗号分隔',
  `text_type_indexs` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`main_id`),
  KEY `exam_main_qid` (`exam_qid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `exam_title` */

CREATE TABLE `exam_title` (
  `title_id` varchar(36) NOT NULL,
  `qid` varchar(36) DEFAULT NULL,
  `title_no` varchar(10) DEFAULT NULL,
  `title_name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`title_id`),
  KEY `exam_title_qid` (`qid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `server_enterprise_rel` */

CREATE TABLE `server_enterprise_rel` (
  `id` varchar(36) NOT NULL,
  `server_code` varchar(36) DEFAULT NULL,
  `enterprise_cis` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `servercode_cis_unique` (`server_code`,`enterprise_cis`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `xs_account` */

CREATE TABLE `xs_account` (
  `aid` varchar(36) NOT NULL,
  `cis_code` varchar(50) DEFAULT NULL,
  `mdm_code` varchar(50) DEFAULT NULL,
  `account` varchar(100) DEFAULT NULL,
  `mobile` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `full_name` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`aid`),
  UNIQUE KEY `account_unique` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
