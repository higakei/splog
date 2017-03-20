/*
MySQL Data Transfer
Source Host: localhost
Source Database: splog_learning
Target Host: localhost
Target Database: splog_learning
Date: 2009/06/08 14:47:09
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for blog_training_data
-- ----------------------------
DROP TABLE IF EXISTS `blog_training_data`;
CREATE TABLE `blog_training_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` text,
  `title` text,
  `body` text,
  `correct` varchar(255) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `learner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `training_id` int(11) DEFAULT NULL,
  `classifier` varchar(255) NOT NULL,
  `reliability` double NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `trial_result` (
  `id` int(11) NOT NULL,
  `trial_id` int(11) DEFAULT NULL,
  `classifier` varchar(255) NOT NULL,
  `data_id` int(11) NOT NULL,
  `identification` text,
  `correct` varchar(255) DEFAULT NULL,
  `answer` varchar(255) NOT NULL,
  `matching` enum('true','false') DEFAULT NULL,
  `creation_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`data_id`,`classifier`),
  KEY `classifier` (`classifier`),
  KEY `data` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


