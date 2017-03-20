/*
MySQL Data Transfer
Source Host: 118.67.72.136
Source Database: splog_learning
Target Host: 118.67.72.136
Target Database: splog_learning
Date: 2009/06/16 23:06:37
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for learner
-- ----------------------------
DROP TABLE IF EXISTS `learner`;
CREATE TABLE `learner` (
  `id` int(11) NOT NULL auto_increment,
  `training_id` int(11) default NULL,
  `classifier` varchar(255) NOT NULL,
  `reliability` double NOT NULL,
  `creation_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `learner` VALUES ('1', null, 'content', '0.101442767895859', '2009-06-14 20:51:09');
INSERT INTO `learner` VALUES ('2', null, 'waisetsu', '0.595767856153692', '2009-06-14 20:51:09');
INSERT INTO `learner` VALUES ('3', null, 'speech_balance', '0.664414198789015', '2009-06-14 20:51:09');
INSERT INTO `learner` VALUES ('4', null, 'naive_bayes', '0.37233860615955', '2009-06-14 20:51:09');
