-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema knowledgeBase
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema knowledgeBase
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `knowledgeBase` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `knowledgeBase` ;

-- -----------------------------------------------------
-- Table `knowledgeBase`.`articles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `knowledgeBase`.`articles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(150) NOT NULL,
  `subtitle` VARCHAR(250) NOT NULL,
  `status` ENUM('DRAFT', 'PUBLISH', 'CANCEL') NOT NULL,
  `viewers` INT NOT NULL,
  `liked` VARCHAR(15) NULL DEFAULT NULL,
  `slug` VARCHAR(100) NOT NULL,
  `created_by` VARCHAR(100) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(100) NOT NULL,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `content` TEXT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `knowledgeBase`.`tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `knowledgeBase`.`tags` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(150) NOT NULL,
  `slug` VARCHAR(100) NOT NULL,
  `created_by` VARCHAR(100) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(100) NOT NULL,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `knowledgeBase`.`article_tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `knowledgeBase`.`article_tags` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `article_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_article_tags_1_idx` (`article_id` ASC) VISIBLE,
  INDEX `fk_article_tags_2_idx` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `fk_article_tags_1`
    FOREIGN KEY (`article_id`)
    REFERENCES `knowledgeBase`.`articles` (`id`),
  CONSTRAINT `fk_article_tags_2`
    FOREIGN KEY (`tag_id`)
    REFERENCES `knowledgeBase`.`tags` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `knowledgeBase`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `knowledgeBase`.`categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(150) NOT NULL,
  `subtitle` VARCHAR(250) NULL,
  `slug` VARCHAR(100) NULL,
  `created_by` VARCHAR(100) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(100) NOT NULL,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `knowledgeBase`.`category_tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `knowledgeBase`.`category_tags` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `category_tag` INT NOT NULL,
  `tag_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_category_tags_1_idx` (`category_tag` ASC) VISIBLE,
  INDEX `fk_category_tags_2_idx` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `fk_category_tags_1`
    FOREIGN KEY (`category_tag`)
    REFERENCES `knowledgeBase`.`categories` (`id`),
  CONSTRAINT `fk_category_tags_2`
    FOREIGN KEY (`tag_id`)
    REFERENCES `knowledgeBase`.`tags` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `knowledgeBase`.`sections`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `knowledgeBase`.`sections` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `subtitle` VARCHAR(250) NOT NULL DEFAULT ' ',
  `slug` VARCHAR(100) NOT NULL,
  `created_by` VARCHAR(100) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(100) NOT NULL,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `knowledgeBase`.`section_categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `knowledgeBase`.`section_categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `section_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_section_categories_1_idx` (`section_id` ASC) VISIBLE,
  INDEX `fk_section_categories_2_idx` (`category_id` ASC) VISIBLE,
  CONSTRAINT `fk_section_categories_1`
    FOREIGN KEY (`section_id`)
    REFERENCES `knowledgeBase`.`sections` (`id`),
  CONSTRAINT `fk_section_categories_2`
    FOREIGN KEY (`category_id`)
    REFERENCES `knowledgeBase`.`categories` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `knowledgeBase`.`section_tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `knowledgeBase`.`section_tags` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `section_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_section_tags_1_idx` (`section_id` ASC) VISIBLE,
  INDEX `fk_section_tags_2_idx` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `fk_section_tags_1`
    FOREIGN KEY (`section_id`)
    REFERENCES `knowledgeBase`.`sections` (`id`),
  CONSTRAINT `fk_section_tags_2`
    FOREIGN KEY (`tag_id`)
    REFERENCES `knowledgeBase`.`tags` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `knowledgeBase`.`article_categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `knowledgeBase`.`article_categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `article_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_section_categories_2_idx` (`category_id` ASC) VISIBLE,
  INDEX `fk_section_categories_10_idx` (`article_id` ASC) VISIBLE,
  CONSTRAINT `fk_section_categories_10`
    FOREIGN KEY (`article_id`)
    REFERENCES `knowledgeBase`.`articles` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_section_categories_20`
    FOREIGN KEY (`category_id`)
    REFERENCES `knowledgeBase`.`categories` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `knowledgeBase`.`article_sections`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `knowledgeBase`.`article_sections` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `article_id` INT NOT NULL,
  `section_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_section_categories_10_idx` (`article_id` ASC) VISIBLE,
  INDEX `fk_section_categories_200_idx` (`section_id` ASC) VISIBLE,
  CONSTRAINT `fk_section_categories_100`
    FOREIGN KEY (`article_id`)
    REFERENCES `knowledgeBase`.`articles` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_section_categories_200`
    FOREIGN KEY (`section_id`)
    REFERENCES `knowledgeBase`.`sections` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `knowledgeBase`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `knowledgeBase`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(150) NOT NULL,
  `password` VARCHAR(150) NOT NULL,
  `profile` VARCHAR(15) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;

INSERT INTO users(name, email, password, profile) VALUES ('first admin', 'firstadmin@knowledgebase.com', '$2b$10$pEMqrMJK8J8U2wQ4W0etAO2ouCOA/m7JiVpLAa3Y1sLCy94QouUkm', 'ROLE_ADMIN');

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
