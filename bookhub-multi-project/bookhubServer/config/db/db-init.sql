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
    `id`                 VARCHAR(255) NOT NULL,
    `publisher`          VARCHAR(255) NOT NULL,
    `publishedDate`      VARCHAR(255) NOT NULL,
    `description`        TEXT NOT NULL,
    `smallThumbnailLink` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `preferences`
(
    `username`       VARCHAR(255) NOT NULL,
    `bookId`         VARCHAR(255) NOT NULL,
    `preferenceType` ENUM ('Favourite', 'Want to read', 'Have read', 'Currently reading') NOT NULL,
     CONSTRAINT PK_Preferences PRIMARY KEY (`username`, `bookID`)
);

