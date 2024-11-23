/*
 Navicat Premium Data Transfer

 Source Server         : 本机MySQL
 Source Server Type    : MySQL
 Source Server Version : 50719 (5.7.19)
 Source Host           : localhost:3306
 Source Schema         : kkchat

 Target Server Type    : MySQL
 Target Server Version : 50719 (5.7.19)
 File Encoding         : 65001

 Date: 23/11/2024 16:09:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app_update
-- ----------------------------
DROP TABLE IF EXISTS `app_update`;
CREATE TABLE `app_update`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `version` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `update_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '0:未发布 1:灰度发布 2:全网发布',
  `grayscale_uid` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '灰度uid',
  `file_type` tinyint(1) NULL DEFAULT NULL COMMENT '文件类型0:本地文件 1:外链',
  `outer_link` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外链地址',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_key`(`version`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'app发布' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of app_update
-- ----------------------------

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message`  (
  `message_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息自增ID',
  `session_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会话ID',
  `message_type` tinyint(1) NOT NULL COMMENT '消息类型',
  `message_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息内容',
  `send_user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送人ID',
  `send_user_nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送人昵称',
  `send_time` bigint(20) NULL DEFAULT NULL COMMENT '发送时间',
  `contact_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接收联系人ID',
  `contact_type` tinyint(1) NULL DEFAULT NULL COMMENT '联系人类型 0:单聊 1:群聊',
  `file_size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小',
  `file_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `file_type` tinyint(1) NULL DEFAULT NULL COMMENT '文件类型',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态 0:正在发送 1:已发送',
  PRIMARY KEY (`message_id`) USING BTREE,
  INDEX `idx_session_id`(`session_id`) USING BTREE,
  INDEX `idx_send_user_id`(`send_user_id`) USING BTREE,
  INDEX `idx_receive_contact_id`(`contact_id`) USING BTREE,
  INDEX `idx_send_time`(`send_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 97 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天消息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_message
-- ----------------------------
INSERT INTO `chat_message` VALUES (1, '709d4f77e571f028f3d950c8d3585873', 2, '欢迎使用EasyChat', 'Urobot', 'EasyChat', 1732101599514, 'U85149933849', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (2, '7b05378801282b3a8add43d7137001a4', 2, '欢迎使用EasyChat', 'Urobot', 'EasyChat', 1732105039410, 'U36136615598', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (3, 'c9298f1fa8c3b87742e9412a548214a1', 1, '我是test02', 'U36136615598', 'test02', 1732156164292, 'U85149933849', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (4, 'c591e547369a6b305550b27d405cf760', 2, '欢迎使用EasyChat', 'Urobot', 'EasyChat', 1732156207914, 'U56107229997', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (5, 'e553e6f5416181b0e015b771d6964312', 1, '我是test03', 'U56107229997', 'test03', 1732156435302, 'U36136615598', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (6, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '欢迎使用EasyChat', 'Urobot', 'EasyChat', 1732173779850, 'U37753670744', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (7, 'b108f5f6604c8d8e2887580e70673f61', 9, 'test03加入了群组', NULL, NULL, 1732175219628, 'G17126508884', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (8, '9356cad72de22cbfa92fe0ea21a1edfa', 3, '群组已经创建好，可以和好友一起畅聊了', NULL, NULL, 1732175326165, 'G90599744028', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (9, '9356cad72de22cbfa92fe0ea21a1edfa', 9, 'test03加入了群组', NULL, NULL, 1732175395009, 'G90599744028', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (10, '1b41f99dda4ac8d76268b5f8f9cfe0ec', 3, '群组已经创建好，可以和好友一起畅聊了', NULL, NULL, 1732176353815, 'G63435417992', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (11, '1b41f99dda4ac8d76268b5f8f9cfe0ec', 9, 'test03加入了群组', NULL, NULL, 1732176377611, 'G63435417992', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (12, 'c317fe4d72cfa403c43397ef419fa85d', 2, '欢迎使用EasyChat', 'Urobot', 'EasyChat', 1732176443069, 'U63196547460', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (13, 'd2d62f5b145bcd43055e6a6c7c433593', 3, '群组已经创建好，可以和好友一起畅聊了', NULL, NULL, 1732176507673, 'G15902038947', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (14, 'd2d62f5b145bcd43055e6a6c7c433593', 9, 'test06加入了群组', NULL, NULL, 1732176547322, 'G15902038947', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (15, 'e56a3c9e18757772883cde6706344a26', 3, '群组已经创建好，可以和好友一起畅聊了', NULL, NULL, 1732176924848, 'G52439044359', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (16, 'e56a3c9e18757772883cde6706344a26', 9, 'test06加入了群组', NULL, NULL, 1732176938964, 'G52439044359', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (17, 'd2d62f5b145bcd43055e6a6c7c433593', 9, 'test03加入了群组', NULL, NULL, 1732177303441, 'G15902038947', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (18, '76e740458f3b1193aad548172762ff7c', 1, '我是test05', 'U37753670744', 'test05', 1732237500104, 'U63196547460', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (19, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test05', 1732243649358, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (20, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好！有什么可以帮到你的吗？', 'Urobot', 'EasyChat', 1732243651415, 'U37753670744', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (21, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '帮我用java写一个快速排序', 'U37753670744', 'test05', 1732243671912, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (22, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '帮我用java写一个快速排序', 'U37753670744', 'test05', 1732243675046, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (23, 'c317fe4d72cfa403c43397ef419fa85d', 2, '你好', 'U63196547460', 'test06测试', 1732243773405, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (24, 'c317fe4d72cfa403c43397ef419fa85d', 2, '你好！有什么可以帮助你的吗？', 'Urobot', 'EasyChat', 1732243774848, 'U63196547460', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (25, 'c317fe4d72cfa403c43397ef419fa85d', 2, '帮我用java写一个快速排序', 'U63196547460', 'test06测试', 1732243789218, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (26, 'c317fe4d72cfa403c43397ef419fa85d', 2, '你好', 'U63196547460', 'test06测试', 1732243838728, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (27, 'c317fe4d72cfa403c43397ef419fa85d', 2, '你好！有什么可以帮到您的吗？', 'Urobot', 'EasyChat', 1732243839700, 'U63196547460', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (28, 'c317fe4d72cfa403c43397ef419fa85d', 2, '帮我用java写一个快速排序', 'U63196547460', 'test06测试', 1732243848673, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (29, 'c317fe4d72cfa403c43397ef419fa85d', 2, '帮我用java写一个快速排序', 'U63196547460', 'test06测试', 1732243852640, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (30, 'c317fe4d72cfa403c43397ef419fa85d', 2, '帮我用java写一个helloworld', 'U63196547460', 'test06测试', 1732243890232, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (31, 'c317fe4d72cfa403c43397ef419fa85d', 2, '```java<br>public class HelloWorld {<br>    public static void main(String[] args) {<br>        System.out.println(\"Hello, World!\");<br>    }<br>}<br>```', 'Urobot', 'EasyChat', 1732243891266, 'U63196547460', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (32, 'c317fe4d72cfa403c43397ef419fa85d', 2, '帮我用java写一个快速排序', 'U63196547460', 'test06测试', 1732243953934, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (33, 'c317fe4d72cfa403c43397ef419fa85d', 2, '```java<br>public class QuickSort {<br>    public void quickSort(int[] arr, int low, int high) {<br>        if (arr == null || arr.length == 0 || low >= high) {<br>            return;<br>        }<br><br>        int middle = low + (high - low) / 2;<br>        int pivot = arr[middle];<br><br>        int i = low, j = high;<br>        while (i &lt;= j) {<br>            while (arr[i] &lt; pivot) {<br>                i++;<br>            }<br>            while (arr[j] > pivot) {<br>                j--;<br>            }<br>            if (i &lt;= j) {<br>                int temp = arr[i];<br>                arr[i] = arr[j];<br>                arr[j] = temp;<br>                i++;<br>                j--;<br>            }<br>        }<br><br>        if (low &lt; j) {<br>            quickSort(arr, low, j);<br>        }<br>        if (high > i) {<br>            quickSort(arr, i, high);<br>        }<br>    }<br><br>    public static void main(String[] args) {<br>        int[] arr = {64, 34, 25, 12, 22, 11, 90};<br>        <br>        QuickSort sorter = new QuickSort();<br>        sorter.quickSort(arr, 0, arr.length - 1);<br>        <br>        for (int num : arr) {<br>            System.out.print(num + \" \");<br>        }<br>    }<br>}<br>```', 'Urobot', 'EasyChat', 1732243957971, 'U63196547460', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (34, 'c317fe4d72cfa403c43397ef419fa85d', 2, '帮我用java写一个快速排序<br>并介绍代码的含义', 'U63196547460', 'test06测试', 1732243987468, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (35, 'c317fe4d72cfa403c43397ef419fa85d', 2, '```java<br>public class QuickSort {<br>    public void quickSort(int[] arr, int low, int high) {<br>        if (low &lt; high) {<br>            int pivot = partition(arr, low, high);<br>            quickSort(arr, low, pivot - 1);<br>            quickSort(arr, pivot + 1, high);<br>        }<br>    }<br><br>    private int partition(int[] arr, int low, int high) {<br>        int pivot = arr[high];<br>        int i = low - 1;<br>        for (int j = low; j &lt; high; j++) {<br>            if (arr[j] &lt; pivot) {<br>                i++;<br>                int temp = arr[i];<br>                arr[i] = arr[j];<br>                arr[j] = temp;<br>            }<br>        }<br>        int temp = arr[i + 1];<br>        arr[i + 1] = arr[high];<br>        arr[high] = temp;<br>        return i + 1;<br>    }<br><br>    public static void main(String[] args) {<br>        int[] arr = {5, 2, 9, 3, 7, 6, 8};<br>        QuickSort sorter = new QuickSort();<br>        sorter.quickSort(arr, 0, arr.length - 1);<br>        for (int num : arr) {<br>            System.out.print(num + \" \");<br>        }<br>    }<br>}<br>```<br><br>代码含义：这段Java代码实现了快速排序算法。`quickSort`方法接收一个整数数组`arr`、起始索引`low`和结束索引`high`，对数组进行快速排序。在`quickSort`中，首先选择一个基准值（pivot），调用`partition`方法将数组分割成两部分，并递归地对这两部分进行快速排序。`partition`方法根据基准值将数组分为小于基准值的部分和大于等于基准值的部分，然后返回基准值的正确位置。最后，在`main`方法中创建一个示例数组，调用`quickSort`方法对其进行排序，并输出排序后的结果。', 'Urobot', 'EasyChat', 1732243993523, 'U63196547460', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (36, 'c317fe4d72cfa403c43397ef419fa85d', 2, '你是谁', 'U63196547460', 'test06测试', 1732244017037, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (37, 'c317fe4d72cfa403c43397ef419fa85d', 2, '我是 ChatGPT，一个由 OpenAI 训练的语言模型。有什么可以帮助你的吗？', 'Urobot', 'EasyChat', 1732244018151, 'U63196547460', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (38, '76e740458f3b1193aad548172762ff7c', 2, '你好', 'U63196547460', 'test06测试', 1732244149543, 'U37753670744', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (39, '76e740458f3b1193aad548172762ff7c', 2, '666', 'U37753670744', 'test05', 1732244156713, 'U63196547460', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (40, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U63196547460', 'test06测试', 1732244286538, 'U37753670744', 0, 141127, '1.png', 0, 0);
INSERT INTO `chat_message` VALUES (41, '76e740458f3b1193aad548172762ff7c', 2, '6966', 'U63196547460', 'test06测试', 1732272344088, 'U37753670744', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (42, '76e740458f3b1193aad548172762ff7c', 2, '你好', 'U37753670744', 'test05', 1732272351591, 'U63196547460', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (43, 'c317fe4d72cfa403c43397ef419fa85d', 2, '帮我写一个java快速排序', 'U63196547460', 'test06测试', 1732272369565, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (44, 'c317fe4d72cfa403c43397ef419fa85d', 2, '帮我写一个java快速排序', 'U63196547460', 'test06测试', 1732272370415, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (45, 'c317fe4d72cfa403c43397ef419fa85d', 2, '帮我写一个java快速排序', 'U63196547460', 'test06测试', 1732272372341, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (46, 'c317fe4d72cfa403c43397ef419fa85d', 2, '```java<br>public class QuickSort {<br>    public void sort(int[] arr) {<br>        if (arr == null || arr.length == 0) {<br>            return;<br>        }<br>        quickSort(arr, 0, arr.length - 1);<br>    }<br><br>    private void quickSort(int[] arr, int low, int high) {<br>        if (low &lt; high) {<br>            int pi = partition(arr, low, high);<br>            quickSort(arr, low, pi - 1);<br>            quickSort(arr, pi + 1, high);<br>        }<br>    }<br><br>    private int partition(int[] arr, int low, int high) {<br>        int pivot = arr[high];<br>        int i = low - 1;<br>        for (int j = low; j &lt; high; j++) {<br>            if (arr[j] &lt; pivot) {<br>                i++;<br>                int temp = arr[i];<br>                arr[i] = arr[j];<br>                arr[j] = temp;<br>            }<br>        }<br>        int temp = arr[i + 1];<br>        arr[i + 1] = arr[high];<br>        arr[high] = temp;<br>        return i + 1;<br>    }<br><br>    public static void main(String[] args) {<br>        int[] arr = {64, 34, 25, 12, 22, 11, 90};<br>        QuickSort qs = new QuickSort();<br>        qs.sort(arr);<br>        for (int num : arr) {<br>            System.out.print(num + \" \");<br>        }<br>    }<br>}<br>```', 'Urobot', 'EasyChat', 1732272374996, 'U63196547460', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (47, 'c317fe4d72cfa403c43397ef419fa85d', 2, '以下是一个Java中的快速排序算法实现：<br><br>```java<br>public class QuickSort {<br>    public static void sort(int[] arr) {<br>        if (arr == null || arr.length == 0) {<br>            return;<br>        }<br>        quickSort(arr, 0, arr.length - 1);<br>    }<br><br>    private static void quickSort(int[] arr, int low, int high) {<br>        if (low >= high) {<br>            return;<br>        }<br><br>        int pivot = arr[(low + high) / 2];<br>        int i = low, j = high;<br>        <br>        while (i &lt;= j) {<br>            while (arr[i] &lt; pivot) {<br>                i++;<br>            }<br>            while (arr[j] > pivot) {<br>                j--;<br>            }<br>            if (i &lt;= j) {<br>                int temp = arr[i];<br>                arr[i] = arr[j];<br>                arr[j] = temp;<br>                i++;<br>                j--;<br>            }<br>        }<br>        <br>        if (low &lt; j) {<br>            quickSort(arr, low, j);<br>        }<br>        if (high > i) {<br>            quickSort(arr, i, high);<br>        }<br>    }<br><br>    public static void main(String[] args) {<br>        int[] arr = {64, 34, 25, 12, 22, 11, 90};<br>        sort(arr);<br>        for (int num : arr) {<br>            System.out.print(num + \" \");<br>        }<br>    }<br>}<br>```<br><br>这段代码实现了一个简单的快速排序算法。', 'Urobot', 'EasyChat', 1732272375271, 'U63196547460', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (48, 'c317fe4d72cfa403c43397ef419fa85d', 2, '```java<br>public class QuickSort {<br>    public void sort(int[] arr) {<br>        if (arr == null || arr.length == 0) {<br>            return;<br>        }<br>        quickSort(arr, 0, arr.length - 1);<br>    }<br><br>    private void quickSort(int[] arr, int low, int high) {<br>        if (low &lt; high) {<br>            int partitionIndex = partition(arr, low, high);<br>            quickSort(arr, low, partitionIndex - 1);<br>            quickSort(arr, partitionIndex + 1, high);<br>        }<br>    }<br><br>    private int partition(int[] arr, int low, int high) {<br>        int pivot = arr[high];<br>        int i = low - 1;<br><br>        for (int j = low; j &lt; high; j++) {<br>            if (arr[j] &lt; pivot) {<br>                i++;<br>                int temp = arr[i];<br>                arr[i] = arr[j];<br>                arr[j] = temp;<br>            }<br>        }<br><br>        int temp = arr[i + 1];<br>        arr[i + 1] = arr[high];<br>        arr[high] = temp;<br><br>        return i + 1;<br>    }<br><br>    public static void main(String[] args) {<br>        int[] arr = {11, 2, 7, 13, 5, 6, 12, 4};<br>        QuickSort sorter = new QuickSort();<br>        sorter.sort(arr);<br><br>        for (int num : arr) {<br>            System.out.print(num + \" \");<br>        }<br>    }<br>}<br>```', 'Urobot', 'EasyChat', 1732272377403, 'U63196547460', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (49, '76e740458f3b1193aad548172762ff7c', 2, '666', 'U63196547460', 'test06测试', 1732272389865, 'U37753670744', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (50, 'd2d62f5b145bcd43055e6a6c7c433593', 2, '333', 'U37753670744', 'test05', 1732272397321, 'G15902038947', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (51, 'e56a3c9e18757772883cde6706344a26', 2, '666', 'U63196547460', 'test06测试', 1732272403470, 'G52439044359', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (52, 'e56a3c9e18757772883cde6706344a26', 2, '5', 'U63196547460', 'test06测试', 1732272871424, 'G52439044359', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (53, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U63196547460', 'test06测试', 1732272883934, 'U37753670744', 0, 141127, '1.png', 0, 0);
INSERT INTO `chat_message` VALUES (54, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U63196547460', 'test06测试', 1732273203623, 'U37753670744', 0, 141127, '1.png', 0, 0);
INSERT INTO `chat_message` VALUES (55, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U63196547460', 'test06测试', 1732273268027, 'U37753670744', 0, 90013, 'Cache_280af254c8ea39e4..jpg', 0, 0);
INSERT INTO `chat_message` VALUES (56, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U63196547460', 'test06测试', 1732273303353, 'U37753670744', 0, 90013, 'Cache_280af254c8ea39e4..jpg', 0, 0);
INSERT INTO `chat_message` VALUES (57, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U63196547460', 'test06测试', 1732273553237, 'U37753670744', 0, 141127, '1.png', 0, 0);
INSERT INTO `chat_message` VALUES (58, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U63196547460', 'test06测试', 1732273594994, 'U37753670744', 0, 141127, '1.png', 0, 0);
INSERT INTO `chat_message` VALUES (59, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U63196547460', 'test06测试', 1732273697556, 'U37753670744', 0, 141127, '1.png', 0, 0);
INSERT INTO `chat_message` VALUES (60, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U63196547460', 'test06测试', 1732273786744, 'U37753670744', 0, 141127, '1.png', 0, 0);
INSERT INTO `chat_message` VALUES (61, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U63196547460', 'test06测试', 1732273813800, 'U37753670744', 0, 141127, '1.png', 0, 0);
INSERT INTO `chat_message` VALUES (62, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U37753670744', 'test05', 1732274233090, 'U63196547460', 0, 141127, '1.png', 0, 0);
INSERT INTO `chat_message` VALUES (63, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U37753670744', 'test05', 1732274315264, 'U63196547460', 0, 141127, '1.png', 0, 0);
INSERT INTO `chat_message` VALUES (64, '76e740458f3b1193aad548172762ff7c', 2, '你好', 'U63196547460', 'test06测试', 1732325356720, 'U37753670744', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (65, '76e740458f3b1193aad548172762ff7c', 5, '[图片]', 'U63196547460', 'test06测试', 1732325362800, 'U37753670744', 0, 141127, '1.png', 0, 0);
INSERT INTO `chat_message` VALUES (66, 'e56a3c9e18757772883cde6706344a26', 2, '666', 'U63196547460', 'test06测试', 1732325384435, 'G52439044359', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (67, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732325436565, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (68, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '我只是一个机器人无法识别你的消息', 'Urobot', 'EasyChat', 1732325436576, 'U37753670744', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (69, '76e740458f3b1193aad548172762ff7c', 2, '6', 'U63196547460', 'test06测试', 1732329300058, 'U37753670744', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (70, 'e56a3c9e18757772883cde6706344a26', 2, '6', 'U37753670744', 'test06', 1732329309575, 'G52439044359', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (71, 'e56a3c9e18757772883cde6706344a26', 11, 'test06测试退出了群聊', NULL, NULL, 1732329334912, 'G52439044359', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (72, 'e56a3c9e18757772883cde6706344a26', 9, 'test06测试加入了群组', NULL, NULL, 1732329351230, 'G52439044359', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (73, '76e740458f3b1193aad548172762ff7c', 2, '你好', 'U37753670744', 'test06', 1732329683338, 'U63196547460', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (74, 'e56a3c9e18757772883cde6706344a26', 2, '6', 'U63196547460', 'test06测试', 1732329695031, 'G52439044359', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (75, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732329708500, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (76, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732329710202, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (77, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732329728501, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (78, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732329816043, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (79, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732329926229, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (80, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732329951258, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (81, '76e740458f3b1193aad548172762ff7c', 2, '6', 'U37753670744', 'test06', 1732329960641, 'U63196547460', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (82, 'd2d62f5b145bcd43055e6a6c7c433593', 2, '你好', 'U37753670744', 'test06', 1732329966486, 'G15902038947', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (83, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732330041805, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (84, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732330190538, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (85, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732330190538, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (86, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732331375144, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (87, 'd2d62f5b145bcd43055e6a6c7c433593', 2, '6', 'U37753670744', 'test06', 1732331453030, 'G15902038947', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (88, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732331466278, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (89, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732332551760, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (90, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好！有什么可以帮助你的吗？', 'Urobot', 'EasyChat', 1732332554266, 'U37753670744', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (91, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好', 'U37753670744', 'test06', 1732332558926, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (92, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '你好！有什么我可以帮忙的吗？', 'Urobot', 'EasyChat', 1732332559877, 'U37753670744', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (93, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '帮我用java写一个快速排序', 'U37753670744', 'test06', 1732332571748, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (94, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '帮我用java写一个快速排序', 'U37753670744', 'test06', 1732332574422, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (95, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '```java<br>public class QuickSort {<br>    public static void quickSort(int[] arr, int low, int high) {<br>        if (arr == null || arr.length == 0 || low >= high) {<br>            return;<br>        }<br><br>        // Choose the pivot element<br>        int middle = low + (high - low) / 2;<br>        int pivot = arr[middle];<br><br>        // Partition the array around the pivot<br>        int i = low, j = high;<br>        while (i &lt;= j) {<br>            while (arr[i] &lt; pivot) {<br>                i++;<br>            }<br>            while (arr[j] > pivot) {<br>                j--;<br>            }<br>            if (i &lt;= j) {<br>                int temp = arr[i];<br>                arr[i] = arr[j];<br>                arr[j] = temp;<br>                i++;<br>                j--;<br>            }<br>        }<br><br>        // Recursive calls for the partitions<br>        if (low &lt; j) {<br>            quickSort(arr, low, j);<br>        }<br>        if (high > i) {<br>            quickSort(arr, i, high);<br>        }<br>    }<br><br>    public static void main(String[] args) {<br>        int[] arr = {5, 3, 8, 6, 2, 7, 1, 4};<br>        quickSort(arr, 0, arr.length - 1);<br><br>        for (int num : arr) {<br>            System.out.print(num + \" \");<br>        }<br>    }<br>}<br>```', 'Urobot', 'EasyChat', 1732332575656, 'U37753670744', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (96, 'a105a9a9ae42d28c713cc8e7181ee158', 2, '```java<br>public class QuickSort {<br>    public void quickSort(int[] arr, int low, int high) {<br>        if (low &lt; high) {<br>            int partitionIndex = partition(arr, low, high);<br><br>            quickSort(arr, low, partitionIndex - 1);<br>            quickSort(arr, partitionIndex + 1, high);<br>        }<br>    }<br><br>    private int partition(int[] arr, int low, int high) {<br>        int pivot = arr[high];<br>        int i = low - 1;<br><br>        for (int j = low; j &lt; high; j++) {<br>            if (arr[j] &lt; pivot) {<br>                i++;<br>                int temp = arr[i];<br>                arr[i] = arr[j];<br>                arr[j] = temp;<br>            }<br>        }<br><br>        int temp = arr[i + 1];<br>        arr[i + 1] = arr[high];<br>        arr[high] = temp;<br><br>        return i + 1;<br>    }<br><br>    public static void main(String[] args) {<br>        int[] arr = {64, 34, 25, 12, 22, 11, 90};<br>        QuickSort sorter = new QuickSort();<br>        sorter.quickSort(arr, 0, arr.length - 1);<br><br>        for (int num : arr) {<br>            System.out.print(num + \" \");<br>        }<br>    }<br>}<br>```', 'Urobot', 'EasyChat', 1732332579075, 'U37753670744', 0, NULL, NULL, NULL, 1);

-- ----------------------------
-- Table structure for chat_session
-- ----------------------------
DROP TABLE IF EXISTS `chat_session`;
CREATE TABLE `chat_session`  (
  `session_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会话ID',
  `last_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '最后接受的消息',
  `last_receive_time` bigint(11) NULL DEFAULT NULL COMMENT '最后接受消息时间毫秒',
  PRIMARY KEY (`session_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会话信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_session
-- ----------------------------
INSERT INTO `chat_session` VALUES ('1b41f99dda4ac8d76268b5f8f9cfe0ec', 'test03加入了群组', 1732176377611);
INSERT INTO `chat_session` VALUES ('709d4f77e571f028f3d950c8d3585873', '欢迎使用EasyChat', 1732101599514);
INSERT INTO `chat_session` VALUES ('76e740458f3b1193aad548172762ff7c', '6', 1732329960641);
INSERT INTO `chat_session` VALUES ('7b05378801282b3a8add43d7137001a4', '欢迎使用EasyChat', 1732105039410);
INSERT INTO `chat_session` VALUES ('9356cad72de22cbfa92fe0ea21a1edfa', 'test03加入了群组', 1732175395009);
INSERT INTO `chat_session` VALUES ('a105a9a9ae42d28c713cc8e7181ee158', '```java<br>public class QuickSort {<br>    public void quickSort(int[] arr, int low, int high) {<br>        if (low &lt; high) {<br>            int partitionIndex = partition(arr, low, high);<br><br>            quickSort(arr, low, partitionIndex - 1);<br>            quickSort(arr, partitionIndex + 1, high);<br>        }<br>    }<br><br>    private int partition(int[] arr, int low, int high) {<br>        int pivot = arr[high];<br>        int i = low - 1;<br><br>        for (int j = low; j &lt; high; j++) {<br>            if (arr[j] &lt; pivot) {<br>                i++;<br>                int temp = arr[i];<br>                arr[i] = arr[j];<br>                arr[j] = temp;<br>            }<br>        }<br><br>        int temp = arr[i + 1];<br>        arr[i + 1] = arr[high];<br>        arr[high] = temp;<br><br>        return i + 1;<br>    }<br><br>    public static void main(String[] args) {<br>        int[] arr = {64, 34, 25, 12, 22, 11, 90};<br>        QuickSort sorter = new QuickSort();<br>        sorter.quickSort(arr, 0, arr.length - 1);<br><br>        for (int num : arr) {<br>            System.out.print(num + \" \");<br>        }<br>    }<br>}<br>```', 1732332579075);
INSERT INTO `chat_session` VALUES ('b108f5f6604c8d8e2887580e70673f61', 'test03加入了群组', 1732175219628);
INSERT INTO `chat_session` VALUES ('c317fe4d72cfa403c43397ef419fa85d', '```java<br>public class QuickSort {<br>    public void sort(int[] arr) {<br>        if (arr == null || arr.length == 0) {<br>            return;<br>        }<br>        quickSort(arr, 0, arr.length - 1);<br>    }<br><br>    private void quickSort(int[] arr, int low, int high) {<br>        if (low &lt; high) {<br>            int partitionIndex = partition(arr, low, high);<br>            quickSort(arr, low, partitionIndex - 1);<br>            quickSort(arr, partitionIndex + 1, high);<br>        }<br>    }<br><br>    private int partition(int[] arr, int low, int high) {<br>        int pivot = arr[high];<br>        int i = low - 1;<br><br>        for (int j = low; j &lt; high; j++) {<br>            if (arr[j] &lt; pivot) {<br>                i++;<br>                int temp = arr[i];<br>                arr[i] = arr[j];<br>                arr[j] = temp;<br>            }<br>        }<br><br>        int temp = arr[i + 1];<br>        arr[i + 1] = arr[high];<br>        arr[high] = temp;<br><br>        return i + 1;<br>    }<br><br>    public static void main(String[] args) {<br>        int[] arr = {11, 2, 7, 13, 5, 6, 12, 4};<br>        QuickSort sorter = new QuickSort();<br>        sorter.sort(arr);<br><br>        for (int num : arr) {<br>            System.out.print(num + \" \");<br>        }<br>    }<br>}<br>```', 1732272377403);
INSERT INTO `chat_session` VALUES ('c591e547369a6b305550b27d405cf760', '欢迎使用EasyChat', 1732156207914);
INSERT INTO `chat_session` VALUES ('c9298f1fa8c3b87742e9412a548214a1', '我是test02', 1732156164292);
INSERT INTO `chat_session` VALUES ('d2d62f5b145bcd43055e6a6c7c433593', 'test06：6', 1732331453030);
INSERT INTO `chat_session` VALUES ('e553e6f5416181b0e015b771d6964312', '我是test03', 1732156435302);
INSERT INTO `chat_session` VALUES ('e56a3c9e18757772883cde6706344a26', 'test06测试：6', 1732329695031);

-- ----------------------------
-- Table structure for chat_session_user
-- ----------------------------
DROP TABLE IF EXISTS `chat_session_user`;
CREATE TABLE `chat_session_user`  (
  `user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `contact_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人ID',
  `session_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会话ID',
  `contact_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人名称',
  PRIMARY KEY (`user_id`, `contact_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_session_id`(`session_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会话用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_session_user
-- ----------------------------
INSERT INTO `chat_session_user` VALUES ('U36136615598', 'U56107229997', 'e553e6f5416181b0e015b771d6964312', 'test03');
INSERT INTO `chat_session_user` VALUES ('U36136615598', 'U85149933849', 'c9298f1fa8c3b87742e9412a548214a1', 'test01');
INSERT INTO `chat_session_user` VALUES ('U36136615598', 'Urobot', '7b05378801282b3a8add43d7137001a4', 'EasyChat');
INSERT INTO `chat_session_user` VALUES ('U37753670744', 'G15902038947', 'd2d62f5b145bcd43055e6a6c7c433593', '测试群聊033');
INSERT INTO `chat_session_user` VALUES ('U37753670744', 'G52439044359', 'e56a3c9e18757772883cde6706344a26', '测试群聊5');
INSERT INTO `chat_session_user` VALUES ('U37753670744', 'G63435417992', '1b41f99dda4ac8d76268b5f8f9cfe0ec', '测试群聊02');
INSERT INTO `chat_session_user` VALUES ('U37753670744', 'G90599744028', '9356cad72de22cbfa92fe0ea21a1edfa', '测试群聊');
INSERT INTO `chat_session_user` VALUES ('U37753670744', 'U63196547460', '76e740458f3b1193aad548172762ff7c', 'test06测试');
INSERT INTO `chat_session_user` VALUES ('U37753670744', 'Urobot', 'a105a9a9ae42d28c713cc8e7181ee158', 'EasyChat');
INSERT INTO `chat_session_user` VALUES ('U56107229997', 'G15902038947', 'd2d62f5b145bcd43055e6a6c7c433593', '测试群聊033');
INSERT INTO `chat_session_user` VALUES ('U56107229997', 'G17126508884', 'b108f5f6604c8d8e2887580e70673f61', 'test');
INSERT INTO `chat_session_user` VALUES ('U56107229997', 'G63435417992', '1b41f99dda4ac8d76268b5f8f9cfe0ec', '测试群聊02');
INSERT INTO `chat_session_user` VALUES ('U56107229997', 'G90599744028', '9356cad72de22cbfa92fe0ea21a1edfa', '测试群聊');
INSERT INTO `chat_session_user` VALUES ('U56107229997', 'U36136615598', 'e553e6f5416181b0e015b771d6964312', 'test02');
INSERT INTO `chat_session_user` VALUES ('U56107229997', 'Urobot', 'c591e547369a6b305550b27d405cf760', 'EasyChat');
INSERT INTO `chat_session_user` VALUES ('U63196547460', 'G15902038947', 'd2d62f5b145bcd43055e6a6c7c433593', '测试群聊033');
INSERT INTO `chat_session_user` VALUES ('U63196547460', 'G52439044359', 'e56a3c9e18757772883cde6706344a26', '测试群聊5');
INSERT INTO `chat_session_user` VALUES ('U63196547460', 'U37753670744', '76e740458f3b1193aad548172762ff7c', 'test06');
INSERT INTO `chat_session_user` VALUES ('U63196547460', 'Urobot', 'c317fe4d72cfa403c43397ef419fa85d', 'EasyChat');
INSERT INTO `chat_session_user` VALUES ('U85149933849', 'U36136615598', 'c9298f1fa8c3b87742e9412a548214a1', 'test02');
INSERT INTO `chat_session_user` VALUES ('U85149933849', 'Urobot', '709d4f77e571f028f3d950c8d3585873', 'EasyChat');

-- ----------------------------
-- Table structure for group_info
-- ----------------------------
DROP TABLE IF EXISTS `group_info`;
CREATE TABLE `group_info`  (
  `group_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '群ID',
  `group_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群组名',
  `group_owner_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群主id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `group_notice` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群公告',
  `join_type` tinyint(1) NULL DEFAULT NULL COMMENT '0:直接加入 1:管理员同意后加入',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态 1:正常 0:解散',
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_info
-- ----------------------------
INSERT INTO `group_info` VALUES ('G15902038947', '测试群聊033', 'U37753670744', '2024-11-21 08:08:28', '', 1, 1);
INSERT INTO `group_info` VALUES ('G17126508884', 'test', 'U53143402272', '2024-11-15 07:42:14', 'rest', 1, 1);
INSERT INTO `group_info` VALUES ('G52439044359', '测试群聊5', 'U37753670744', '2024-11-21 08:15:25', '', 1, 1);
INSERT INTO `group_info` VALUES ('G58066140538', 'kkk', 'U53280353806', '2024-11-15 07:32:55', 'test', 1, 1);
INSERT INTO `group_info` VALUES ('G63435417992', '测试群聊02', 'U37753670744', '2024-11-21 08:05:54', NULL, 1, 1);
INSERT INTO `group_info` VALUES ('G90599744028', '测试群聊', 'U37753670744', '2024-11-21 07:48:46', NULL, 1, 1);

-- ----------------------------
-- Table structure for user_contact
-- ----------------------------
DROP TABLE IF EXISTS `user_contact`;
CREATE TABLE `user_contact`  (
  `user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `contact_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人ID或者群组ID',
  `contact_type` tinyint(1) NULL DEFAULT NULL COMMENT '联系人类型 0:好友 1:群组',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态 0:非好友 1:好友 2:已删除好友 3:被好友删除 4:已拉黑好友 5:被好友拉黑',
  `last_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_id`, `contact_id`) USING BTREE,
  INDEX `idx_contact_id`(`contact_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '联系人' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_contact
-- ----------------------------
INSERT INTO `user_contact` VALUES ('U36136615598', 'U56107229997', 0, '2024-11-21 02:33:55', 1, '2024-11-21 02:33:55');
INSERT INTO `user_contact` VALUES ('U36136615598', 'U85149933849', 0, '2024-11-21 01:20:24', 1, '2024-11-21 02:29:24');
INSERT INTO `user_contact` VALUES ('U36136615598', 'Urobot', 0, '2024-11-20 12:17:19', 1, '2024-11-20 12:17:19');
INSERT INTO `user_contact` VALUES ('U37753670744', 'G15902038947', 1, '2024-11-21 08:08:28', 1, '2024-11-21 08:08:28');
INSERT INTO `user_contact` VALUES ('U37753670744', 'G17126508884', 1, '2024-11-21 07:28:18', 1, '2024-11-21 07:28:18');
INSERT INTO `user_contact` VALUES ('U37753670744', 'G52439044359', 1, '2024-11-21 08:15:25', 1, '2024-11-21 08:15:25');
INSERT INTO `user_contact` VALUES ('U37753670744', 'G63435417992', 1, '2024-11-21 08:05:54', 1, '2024-11-21 08:05:54');
INSERT INTO `user_contact` VALUES ('U37753670744', 'G90599744028', 1, '2024-11-21 07:48:46', 1, '2024-11-21 07:48:46');
INSERT INTO `user_contact` VALUES ('U37753670744', 'U63196547460', 0, '2024-11-22 01:05:00', 1, '2024-11-22 01:05:00');
INSERT INTO `user_contact` VALUES ('U37753670744', 'Urobot', 0, '2024-11-21 07:23:00', 1, '2024-11-21 07:23:00');
INSERT INTO `user_contact` VALUES ('U53143402272', 'G17126508884', 1, '2024-11-15 07:42:14', 1, '2024-11-15 07:42:14');
INSERT INTO `user_contact` VALUES ('U53143402272', 'U53280353806', 0, '2024-11-16 07:44:32', 1, '2024-11-16 11:27:05');
INSERT INTO `user_contact` VALUES ('U53280353806', 'G58066140538', 1, '2024-11-15 07:32:55', 1, '2024-11-15 07:32:55');
INSERT INTO `user_contact` VALUES ('U53280353806', 'U53143402272', 0, '2024-11-16 11:27:05', 1, '2024-11-16 11:27:05');
INSERT INTO `user_contact` VALUES ('U56107229997', 'G15902038947', 1, '2024-11-21 08:21:43', 1, '2024-11-21 08:21:43');
INSERT INTO `user_contact` VALUES ('U56107229997', 'G17126508884', 1, '2024-11-21 07:47:00', 1, '2024-11-21 07:47:00');
INSERT INTO `user_contact` VALUES ('U56107229997', 'G63435417992', 1, '2024-11-21 08:06:18', 1, '2024-11-21 08:06:18');
INSERT INTO `user_contact` VALUES ('U56107229997', 'G90599744028', 1, '2024-11-21 07:49:55', 1, '2024-11-21 07:49:55');
INSERT INTO `user_contact` VALUES ('U56107229997', 'U36136615598', 0, '2024-11-21 02:33:55', 1, '2024-11-21 02:33:55');
INSERT INTO `user_contact` VALUES ('U56107229997', 'Urobot', 0, '2024-11-21 02:30:08', 1, '2024-11-21 02:30:08');
INSERT INTO `user_contact` VALUES ('U63196547460', 'G15902038947', 1, '2024-11-21 08:09:07', 1, '2024-11-21 08:09:07');
INSERT INTO `user_contact` VALUES ('U63196547460', 'G52439044359', 1, '2024-11-23 10:35:51', 1, '2024-11-23 10:35:51');
INSERT INTO `user_contact` VALUES ('U63196547460', 'U37753670744', 0, '2024-11-22 01:05:00', 1, '2024-11-22 01:05:00');
INSERT INTO `user_contact` VALUES ('U63196547460', 'Urobot', 0, '2024-11-21 08:07:23', 1, '2024-11-21 08:07:23');
INSERT INTO `user_contact` VALUES ('U85149933849', 'U36136615598', 0, '2024-11-21 01:20:24', 1, '2024-11-21 02:29:24');
INSERT INTO `user_contact` VALUES ('U85149933849', 'Urobot', 0, '2024-11-20 11:20:00', 1, '2024-11-20 11:20:00');

-- ----------------------------
-- Table structure for user_contact_apply
-- ----------------------------
DROP TABLE IF EXISTS `user_contact_apply`;
CREATE TABLE `user_contact_apply`  (
  `apply_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `apply_user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申请人id',
  `receive_user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接收人ID',
  `contact_type` tinyint(1) NOT NULL COMMENT '联系人类型 0:好友 1:群组',
  `contact_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人群组ID',
  `last_apply_time` bigint(20) NULL DEFAULT NULL COMMENT '最后申请时间',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态0:待处理 1:已同意  2:已拒绝 3:已拉黑',
  `apply_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请信息',
  PRIMARY KEY (`apply_id`) USING BTREE,
  UNIQUE INDEX `idx_key`(`apply_user_id`, `receive_user_id`, `contact_id`) USING BTREE,
  INDEX `idx_last_apply_time`(`last_apply_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '联系人申请' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_contact_apply
-- ----------------------------
INSERT INTO `user_contact_apply` VALUES (1, 'U53143402272', 'U53280353806', 0, 'U53280353806', 1731743097561, 0, '我是kkk');
INSERT INTO `user_contact_apply` VALUES (2, 'U53280353806', 'U53143402272', 0, 'U53143402272', 1731756425271, 1, '我是zzz');
INSERT INTO `user_contact_apply` VALUES (3, 'U36136615598', 'U85149933849', 0, 'U85149933849', 1732156164284, 1, '我是test02');
INSERT INTO `user_contact_apply` VALUES (4, 'U56107229997', 'U85149933849', 0, 'U85149933849', 1732156272279, 0, '我是test03');
INSERT INTO `user_contact_apply` VALUES (5, 'U56107229997', 'U36136615598', 0, 'U36136615598', 1732156435299, 1, '我是test03');
INSERT INTO `user_contact_apply` VALUES (6, 'U37753670744', 'U53143402272', 1, 'G17126508884', 1732174097983, 1, '我是test05');
INSERT INTO `user_contact_apply` VALUES (7, 'U56107229997', 'U53143402272', 1, 'G17126508884', 1732175219625, 1, '我是test03');
INSERT INTO `user_contact_apply` VALUES (8, 'U56107229997', 'U37753670744', 1, 'G90599744028', 1732175395005, 1, '我是test03');
INSERT INTO `user_contact_apply` VALUES (9, 'U56107229997', 'U37753670744', 1, 'G63435417992', 1732176377607, 1, '我是test03');
INSERT INTO `user_contact_apply` VALUES (10, 'U63196547460', 'U37753670744', 1, 'G15902038947', 1732176547319, 1, '我是test06');
INSERT INTO `user_contact_apply` VALUES (11, 'U63196547460', 'U37753670744', 1, 'G52439044359', 1732329351225, 1, '我是test06测试');
INSERT INTO `user_contact_apply` VALUES (12, 'U56107229997', 'U37753670744', 1, 'G15902038947', 1732177303425, 1, '我是test03');
INSERT INTO `user_contact_apply` VALUES (13, 'U37753670744', 'U63196547460', 0, 'U63196547460', 1732237500088, 1, '我是test05');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `join_type` tinyint(1) NULL DEFAULT NULL COMMENT '0:直接加入  1:同意后加好友',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别 0:女 1:男',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `personal_signature` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `area_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地区',
  `area_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地区编号',
  `last_off_time` bigint(13) NULL DEFAULT NULL COMMENT '最后离开时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `idx_key_email`(`email`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('U36136615598', 'test02@qq.com', 'test02', NULL, NULL, 'd2fe7987bd400d584cb77c8ba93c091b', NULL, 1, '2024-11-20 12:17:19', '2024-11-21 02:33:24', NULL, NULL, 1732156888111);
INSERT INTO `user_info` VALUES ('U37753670744', 'test05@qq.com', 'test06', 0, 1, 'd2fe7987bd400d584cb77c8ba93c091b', '', 1, '2024-11-21 07:23:00', '2024-11-23 03:29:01', '', '', 1732332631743);
INSERT INTO `user_info` VALUES ('U53143402272', '2765314967@qq.com', 'kkk', NULL, NULL, 'd2fe7987bd400d584cb77c8ba93c091b', NULL, 1, '2024-11-13 11:42:46', '2024-11-21 07:45:25', NULL, NULL, 1732175343078);
INSERT INTO `user_info` VALUES ('U53280353806', 'test@qq.com', 'kkkk', NULL, NULL, 'd2fe7987bd400d584cb77c8ba93c091b', NULL, 1, '2024-11-15 01:37:44', '2024-11-23 15:46:34', NULL, NULL, 1732348068593);
INSERT INTO `user_info` VALUES ('U56107229997', 'test04@qq.com', 'test03', NULL, NULL, 'd2fe7987bd400d584cb77c8ba93c091b', NULL, 1, '2024-11-21 02:30:08', '2024-11-21 08:36:45', NULL, NULL, 1732178505011);
INSERT INTO `user_info` VALUES ('U63196547460', 'test06@qq.com', 'test06测试', 0, 1, 'd2fe7987bd400d584cb77c8ba93c091b', '', 1, '2024-11-21 08:07:23', '2024-11-23 03:29:01', '', '', 1732332631743);
INSERT INTO `user_info` VALUES ('U85149933849', 'test01@qq.com', 'test01', NULL, NULL, 'd2fe7987bd400d584cb77c8ba93c091b', NULL, 1, '2024-11-20 11:19:59', '2024-11-21 02:31:33', NULL, NULL, 1732156313127);

-- ----------------------------
-- Table structure for user_info_beauty
-- ----------------------------
DROP TABLE IF EXISTS `user_info_beauty`;
CREATE TABLE `user_info_beauty`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '0：未使用 1：已使用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_key_user_id`(`user_id`) USING BTREE,
  UNIQUE INDEX `idx_key_email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '靓号表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_info_beauty
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
