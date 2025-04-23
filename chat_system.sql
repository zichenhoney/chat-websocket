/*
 Navicat Premium Dump SQL

 Source Server         : zichen
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : chat_system

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 10/04/2025 22:28:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends`  (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                            `friend_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `fk_friend_user`(`user_name` ASC) USING BTREE,
                            INDEX `fk_friend_friend`(`friend_name` ASC) USING BTREE,
                            CONSTRAINT `fk_friend_friend` FOREIGN KEY (`friend_name`) REFERENCES `users` (`username`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                            CONSTRAINT `fk_friend_user` FOREIGN KEY (`user_name`) REFERENCES `users` (`username`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of friends
-- ----------------------------
INSERT INTO `friends` VALUES (42, 'zichen', 'root');
INSERT INTO `friends` VALUES (43, 'root', 'zichen');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `senduser` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                            `receiveuser` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                            `title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                            `detail` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                            `createdate` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                            `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (26, 'zichen', 'root', NULL, 'hello', '2025-04-03 12:57:11', NULL);
INSERT INTO `message` VALUES (27, 'root', 'zichen', NULL, 'hi', '2025-04-03 12:57:21', NULL);
INSERT INTO `message` VALUES (28, 'zichen', 'root', NULL, 'hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh', '2025-04-03 19:27:05', NULL);
INSERT INTO `message` VALUES (29, 'zichen', 'root', NULL, ',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,', '2025-04-03 19:27:16', NULL);
INSERT INTO `message` VALUES (30, 'zichen', 'root', NULL, '你好', '2025-04-03 20:01:05', NULL);
INSERT INTO `message` VALUES (31, 'zichen', 'root', NULL, '11', '2025-04-05 14:34:39', NULL);
INSERT INTO `message` VALUES (32, 'root', 'zichen', NULL, '11', '2025-04-05 14:37:01', NULL);
INSERT INTO `message` VALUES (33, 'root', 'zichen', NULL, '11', '2025-04-07 13:28:06', NULL);
INSERT INTO `message` VALUES (34, 'zichen', 'root', NULL, '11', '2025-04-07 13:28:24', NULL);
INSERT INTO `message` VALUES (35, 'zichen', 'root', NULL, '00', '2025-04-10 21:20:21', '0');
INSERT INTO `message` VALUES (36, 'root', 'zichen', NULL, 'hello', '2025-04-10 21:20:56', '0');
INSERT INTO `message` VALUES (37, 'zichen', 'root', NULL, '哈哈', '2025-04-10 22:03:17', '0');
INSERT INTO `message` VALUES (38, 'root', 'zichen', NULL, 'hh', '2025-04-10 22:03:39', '0');
INSERT INTO `message` VALUES (39, 'zichen', 'root', NULL, '哈哈', '2025-04-10 22:03:50', '0');
INSERT INTO `message` VALUES (40, 'zichen', 'root', NULL, '哈哈', '2025-04-10 22:10:01', '0');
INSERT INTO `message` VALUES (41, 'root', 'zichen', NULL, 'hh', '2025-04-10 22:10:29', '0');
INSERT INTO `message` VALUES (42, 'root', 'zichen', NULL, 'hh', '2025-04-10 22:10:31', '0');
INSERT INTO `message` VALUES (43, 'zichen', 'root', NULL, '哈哈', '2025-04-10 22:10:40', '0');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
                          `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                          `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                          `sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                          `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                          `signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                          PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('libing', '123456', '男', 'avatar_10.jpg', '这个人很神秘，什么也没有留下');
INSERT INTO `users` VALUES ('lifang', '123456', '男', 'avatar_13.jpg', '这个人很神秘，什么也没有留下');
INSERT INTO `users` VALUES ('liming', '123456', '男', 'avatar_3.jpg', '这个人很神秘，什么也没有留下');
INSERT INTO `users` VALUES ('root', '123456', '男', 'avatar_1.jpg', '这个人很神秘，什么也没有留下');
INSERT INTO `users` VALUES ('zichen', '123456', '男', 'avatar_1.jpg', '这个人很神秘，什么也没有留下');

SET FOREIGN_KEY_CHECKS = 1;
