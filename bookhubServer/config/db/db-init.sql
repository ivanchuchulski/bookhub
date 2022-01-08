-- db ddl commands for creation

CREATE DATABASE IF NOT EXISTS `bookhub`;

USE `bookhub`;

CREATE TABLE `users`
(
    `username` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`username`)
);

CREATE TABLE `book`
(
    `title`              VARCHAR(255) NOT NULL,
    `id`                 VARCHAR(255) NOT NULL,
    `publisher`          VARCHAR(255) NOT NULL,
    `publishedDate`      VARCHAR(255) NOT NULL,
    `description`        TEXT         NOT NULL,
    `smallThumbnailLink` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `preferences`
(
    `username`       VARCHAR(255)                                                         NOT NULL,
    `bookId`         VARCHAR(255)                                                         NOT NULL,
    `preferenceType` ENUM ('Favourite', 'Want to read', 'Have read', 'Currently reading') NOT NULL,
    CONSTRAINT pk_preferences PRIMARY KEY (`username`, `bookID`),
    CONSTRAINT `preference_fk_username` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT `preference_fk_book` FOREIGN KEY (`bookId`) REFERENCES `book` (`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE

);

CREATE TABLE `admin`
(
    `username` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`username`)
);

