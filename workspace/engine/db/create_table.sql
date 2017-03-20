-- MySQL dump 10.11
--
-- Host: localhost    Database: blogfeeds
-- ------------------------------------------------------
-- Server version	5.0.45-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `author_result`
--

DROP TABLE IF EXISTS `author_result`;
CREATE TABLE `author_result` (
  `authorId` varchar(255) NOT NULL,
  `title` int(10) default NULL,
  `content` int(10) default NULL,
  `intervals` int(10) default NULL,
  `score` int(10) default NULL,
  PRIMARY KEY  (`authorId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `blog_result`
--

DROP TABLE IF EXISTS `blog_result`;
CREATE TABLE `blog_result` (
  `documentId` varchar(128) NOT NULL,
  `score` int(10) default NULL,
  PRIMARY KEY  (`documentId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `blogfeed`
--

DROP TABLE IF EXISTS `blogfeed`;
CREATE TABLE `blogfeed` (
  `id` int(11) NOT NULL auto_increment,
  `authorId` varchar(255) NOT NULL,
  `documentId` varchar(128) NOT NULL,
  `url` text,
  `title` varchar(255) default NULL,
  `body` text,
  `date` datetime default NULL,
  `creation_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `document` (`documentId`),
  KEY `author` (`authorId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Table structure for table `specialletters`
--

DROP TABLE IF EXISTS `specialletters`;
CREATE TABLE `specialletters` (
  `serial` int(11) NOT NULL auto_increment,
  `type` int(11) default NULL,
  `str` text,
  PRIMARY KEY  (`serial`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2009-02-25  7:56:59
