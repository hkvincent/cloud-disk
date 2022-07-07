CREATE DATABASE /*!32312 IF NOT EXISTS*/`drive` /*!40100 DEFAULT CHARACTER SET utf8 */;

/*Table structure for table `catalog` */

CREATE TABLE IF NOT EXISTS `catalog` (
  `cId` varchar(50) NOT NULL,
  `pId` varchar(50) DEFAULT NULL,
  `cName` varchar(50) DEFAULT NULL,
  `cDate` varchar(50) DEFAULT NULL,
  `cF` varchar(50) DEFAULT NULL,
  `isShare` varchar(2) DEFAULT NULL,
  `cLevel` int(11) DEFAULT NULL,
  `uId` varchar(255) NOT NULL,
  PRIMARY KEY (`cId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `catalog_file` */

CREATE TABLE IF NOT EXISTS `catalog_file` (
  `cf` varchar(50) NOT NULL,
  `fid` varchar(50) DEFAULT NULL,
  KEY `cf` (`cf`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `file` */

CREATE TABLE IF NOT EXISTS `file` (
  `fId` varchar(50) NOT NULL,
  `fPath` text,
  `fSize` int(50) DEFAULT NULL,
  `fType` varchar(50) DEFAULT NULL,
  `fName` varchar(50) DEFAULT NULL,
  `fHash` varchar(50) DEFAULT NULL,
  `fDowncount` int(11) DEFAULT NULL,
  `fDesc` varchar(50) DEFAULT NULL,
  `fUploadtime` date DEFAULT NULL,
  `isShare` bigint(2) DEFAULT NULL,
  `cId` varchar(50) DEFAULT NULL,
  `fDiskName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`fId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `info` */

CREATE TABLE IF NOT EXISTS `info` (
  `iId` varchar(50) NOT NULL,
  `iTitle` varchar(100) DEFAULT NULL,
  `iContent` text,
  `iTime` varchar(50) DEFAULT NULL,
  `iImage` varchar(500) DEFAULT NULL,
  `isImage` int(11) DEFAULT NULL,
  `iLocation` int(11) DEFAULT NULL,
  `iStart` int(11) DEFAULT NULL,
  PRIMARY KEY (`iId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `role` */

CREATE TABLE IF NOT EXISTS `role` (
  `role` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `user` */


CREATE TABLE IF NOT EXISTS `user` (
  `uId` varchar(55) NOT NULL,
  `userName` varchar(50) DEFAULT NULL,
  `uPassword` varchar(50) DEFAULT NULL,
  `cId` varchar(50) DEFAULT NULL,
  `uTime` varchar(50) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `fileSize` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;