-- db ddl commands for creation

CREATE DATABASE IF NOT EXISTS `bookhub`;

USE `bookhub`;

CREATE TABLE `users`
(
    `id`       INT(11)      NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `book`
(
    `title`              VARCHAR(255) NOT NULL,
    `publisher`          VARCHAR(255) NOT NULL,
    `publishedDate`      VARCHAR(255) NOT NULL,
    `description`        VARCHAR(255) NOT NULL,
    `smallThumbnailLink` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`title`)
);

CREATE TABLE `preferences`
(
    `username`       VARCHAR(255) NOT NULL,
    `title`          VARCHAR(255) NOT NULL,
    `preferenceType` ENUM ('favourites', 'wantToRead', 'haveRead', 'currentlyReading') NOT NULL
);

