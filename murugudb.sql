-- --------------------------------------------------------
-- Host:                         
-- Server version:               5.1.41 - Source distribution
-- Server OS:                    Win32
-- HeidiSQL version:             7.0.0.4053
-- Date/time:                    2012-09-11 03:06:46
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

-- Dumping database structure for murugusms
CREATE DATABASE IF NOT EXISTS `murugusms` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `murugusms`;


-- Dumping structure for table murugusms.sms
CREATE TABLE IF NOT EXISTS `sms` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `text` varchar(50) NOT NULL,
  `response` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
