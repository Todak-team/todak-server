-- MySQL 8.0
-- 스키마 생성
CREATE DATABASE IF NOT EXISTS todak
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE todak;

CREATE TABLE IF NOT EXISTS `user` (
    user_id    BIGINT       NOT NULL AUTO_INCREMENT,
    kakao_id   VARCHAR(100) NOT NULL,
    nickname   VARCHAR(50)  NOT NULL,
    created_at DATETIME     NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE KEY uq_user_kakao_id (kakao_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS daily_emotion (
    emotion_id    BIGINT   NOT NULL AUTO_INCREMENT,
    user_id       BIGINT   NOT NULL,
    emotion_score INT      NOT NULL,
    checked_date  DATE     NOT NULL,
    updated_at    DATETIME NOT NULL,
    PRIMARY KEY (emotion_id),
    UNIQUE KEY uq_daily_emotion_user_date (user_id, checked_date),
    CONSTRAINT fk_daily_emotion_user FOREIGN KEY (user_id) REFERENCES `user` (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS routine (
    routine_id   BIGINT       NOT NULL AUTO_INCREMENT,
    user_id      BIGINT       NOT NULL,
    plan_a_title VARCHAR(255) NOT NULL,
    plan_b_title VARCHAR(255),
    created_at   DATETIME     NOT NULL,
    PRIMARY KEY (routine_id),
    CONSTRAINT fk_routine_user FOREIGN KEY (user_id) REFERENCES `user` (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS routine_log (
    log_id         BIGINT   NOT NULL AUTO_INCREMENT,
    routine_id     BIGINT   NOT NULL,
    user_id        BIGINT   NOT NULL,
    log_date       DATE     NOT NULL,
    is_completed   BOOLEAN  NOT NULL,
    completed_plan CHAR(1),
    emotion_score  INT      NOT NULL,
    created_at     DATETIME NOT NULL,
    PRIMARY KEY (log_id),
    UNIQUE KEY uq_routine_log_routine_date (routine_id, log_date),
    CONSTRAINT fk_routine_log_routine FOREIGN KEY (routine_id) REFERENCES routine (routine_id),
    CONSTRAINT fk_routine_log_user FOREIGN KEY (user_id) REFERENCES `user` (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS goal (
    goal_id        BIGINT       NOT NULL AUTO_INCREMENT,
    user_id        BIGINT       NOT NULL,
    plan_a_title   VARCHAR(255) NOT NULL,
    plan_b_title   VARCHAR(255),
    due_date       DATE,
    is_completed   BOOLEAN      NOT NULL DEFAULT FALSE,
    completed_plan CHAR(1),
    created_at     DATETIME     NOT NULL,
    PRIMARY KEY (goal_id),
    CONSTRAINT fk_goal_user FOREIGN KEY (user_id) REFERENCES `user` (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
