# 下载链接

链接：https://pan.baidu.com/s/1aPj_C7azGLpWanKHpxCEvQ?pwd=kkkk 

![image](https://github.com/user-attachments/assets/559d79cd-e0e9-4989-a74e-f70ea9ddbff1)

## 1. 项目概述

本项目是一个用于开发者的团队即时通讯应用，用户可以通过该应用进行好友管理、群组管理、消息发送与接收、AI助手交互等功能。项目还提供了后台管理系统，用于用户管理、系统设置等功能。

### 1.1 项目背景

在软件开发过程中，团队成员之间的即时沟通和协作至关重要。并且，许多项目在开发阶段涉及敏感信息，无法公开讨论或使用开源的通讯工具。为了保护项目隐私和确保信息安全，我们设计了这款开发者内部聊天软件。



### 1.2 项目意义

本项目旨在为开发团队提供一个安全、高效的内部通讯工具，确保项目开发过程中的隐私和信息安全。通过提供灵活的好友和群组管理、AI助手辅助、后台管理系统等功能，提升团队协作效率，促进项目的顺利进行。

 

### 1.3 技术特点

数据库及可视化工具：

数据库：mysql5.6 

可视化工具：Navicat16 

开发工具包：JDK1.8

浏览器：Microsoft Edge 

服务器：Tomcat8

技术栈： 

语言：java,javascript 

框架：springboot + netty

前端：vue 

其他技术： 

springBoot + redis + mybatis-plus + websocket

 

## 2. 用户注册与登录

### 2.1 注册

步骤：

1. 进入注册页面。
2. 输入用户名、密码、邮箱等信息。
3. 点击“注册”按钮。
4. 系统会自动生成一个AI助手，并分配默认头像和默认消息。

![image](https://github.com/user-attachments/assets/31d646ea-7b77-41b2-86d7-31872f80850d)


​      (注册功能)

### 2.2 登录

步骤：

1. 进入登录页面
2. 输入用户名和密码
3. 点击“登录”按钮
4. 登录成功后，进入主界面

![image](https://github.com/user-attachments/assets/787ba023-0ee4-4f3a-a1f3-568165fe60fd)

## 3. 好友管理

### 3.1 添加好友

步骤：

 

1. 在主界面点击“添加好友”按钮。
2. 输入好友的用户名或ID。
3. 点击“搜索”按钮，找到目标用户。
4. 点击“添加”按钮，发送好友请求。
5. 对方同意后，好友关系建立。

![image](https://github.com/user-attachments/assets/b5a9c9cf-cf57-4f02-b5b2-9ec8ade439ca)

 

### 3.2 删除好友

步骤

​	1. 在好友列表中找到目标好友。

​	2. 点击好友头像或用户名。

​	3. 在弹出的菜单中选择“删除好友”。

​	4. 确认删除后，好友关系解除。

![image](https://github.com/user-attachments/assets/1f649ece-bed1-4cee-8631-5dd716a27c22)

 

### 3.3 拉黑好友

 

步骤：

​	1. 在好友列表中找到目标好友。

​	2. 点击好友头像或用户名。

​	3. 在弹出的菜单中选择“拉黑”。

​	4. 确认拉黑后，该好友将无法与你进行任何互动。

![image](https://github.com/user-attachments/assets/c70d6795-59c0-4518-ac2c-a9a1748f251e)

 

##  4. 群组管理

 

###  4.1 创建群组

 

 步骤：

​	1. 在主界面点击“创建群组”按钮。

​	2. 输入群组名称和描述。

​	3. 选择群组成员（可选）。

​	4. 点击“创建”按钮，群组创建成功。

![image](https://github.com/user-attachments/assets/0880fc99-e843-4f6f-bb92-260d5afd82ca)

![image](https://github.com/user-attachments/assets/0b7e0fcd-4028-4a4a-98dd-28aaa043004b)

 

###  4.2 加入群组

 

 步骤：

​	1. 在主界面点击“加入群组”按钮。

​	2. 输入群组ID或扫描群组二维码。

​	3. 点击“加入”按钮，等待群主或管理员审核。

​	4. 审核通过后，成功加入群组。

![image](https://github.com/user-attachments/assets/d7fae97b-614f-45db-b63d-ebbcd3d86bea)

![image](https://github.com/user-attachments/assets/b5069f09-245d-4f35-9455-9fd0db0b0b40)

###  4.3 退出群组

 

 步骤：

​	1. 在群组列表中找到目标群组。

​	2. 点击群组名称。

​	3. 在弹出的菜单中选择“退出群组”。

​	4. 确认退出后，退出群组。

![image](https://github.com/user-attachments/assets/c11824be-be91-4cfa-bf7a-9e56c1464307)

###  4.4 群组消息发送

 

 步骤：

​	1. 在群组聊天界面中，输入消息内容。

​	2. 点击“发送”按钮，消息发送成功。

​	3. 群组成员可以实时接收并查看消息。

 

![image](https://github.com/user-attachments/assets/1334e4ab-ec82-4336-9301-a8f1ee7af1d8)

 

##  5. 消息功能

 

###  5.1 私聊消息

 

 步骤：

​	1. 在好友列表中找到目标好友。

​	2. 点击好友头像或用户名，进入私聊界面。

​	3. 输入消息内容，点击“发送”按钮。

​	4. 对方可以实时接收并查看消息。

![image](https://github.com/user-attachments/assets/47a92cb0-8a39-4d73-b9fc-f53ce9318137)

###  5.2 群聊消息

 

 步骤：

​	1. 在群组列表中找到目标群组。

​	2. 点击群组名称，进入群聊界面。

​	3. 输入消息内容，点击“发送”按钮。

​	4. 群组成员可以实时接收并查看消息。

![image](https://github.com/user-attachments/assets/377c5fd0-7710-4645-aec5-2f7f10eb1ef0)

###  5.3 消息文件下载路径设置\

 

 步骤：

​	1. 进入“设置”页面。

​	2. 找到“消息文件下载路径”选项。

​	3. 点击“更改路径”按钮，选择新的下载路径。

​	4. 确认设置后，消息文件将自动下载到指定路径。

 

 

![image](https://github.com/user-attachments/assets/fbc8812b-68e7-4da7-a5ea-30e98a7546bf)

##  6. AI助手

###  6.1 AI助手功能介绍

 

 功能：

​	 可以自动回复消息，对用户问题进行讲解

![image](https://github.com/user-attachments/assets/43f86116-c4ce-48c3-a615-8ad1fa258a24)

###  6.2 AI助手默认设置

 

 设置：

​	 默认头像：系统自动分配。

​	 默认消息：系统预设的欢迎消息。

​	 默认功能：自动回复、信息查询等。

 

![image](https://github.com/user-attachments/assets/d6c13588-b130-4bdf-b05a-68dddb3aa3b7)

 

##  7. 后台管理

 

###  7.1 用户管理

 

 功能：

​	 查看用户列表。

​	 禁用/启用用户账号。

​	 修改用户信息。

![image](https://github.com/user-attachments/assets/7526cfa7-c3a0-4f14-be4a-c66dde1c8d9a)

###  7.2 群组管理

 功能：

​	 查看群组列表。

​	 解散群组。

​	 修改群组信息。

![image](https://github.com/user-attachments/assets/f03a9bc8-bb89-4c4e-9f9d-90b0f6a60b87)

###  7.3 系统设置

 设置：

​	 最大文件上传大小。

​	 AI助手默认头像。

​	 AI助手默认消息。

![image](https://github.com/user-attachments/assets/ac82d8b0-6cb8-4755-b160-f728762dada2)

 

 
