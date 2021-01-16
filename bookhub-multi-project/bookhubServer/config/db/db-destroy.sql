-- db ddl commands for removal

USE `bookhub`;

DELETE FROM `preferences`;
DROP TABLE `preferences`;

DELETE FROM `users`;
DROP TABLE `users`;

DELETE FROM `book`;
DROP TABLE `book`;

DROP DATABASE IF EXISTS `bookhub`;
