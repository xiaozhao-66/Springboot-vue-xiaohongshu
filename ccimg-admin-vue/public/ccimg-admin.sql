/*
Navicat MySQL Data Transfer

Source Server         : mogu
Source Server Version : 80022
Source Host           : 192.168.57.128:3306
Source Database       : ccimg-admin

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2023-03-27 23:11:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
INSERT INTO `qrtz_cron_triggers` VALUES ('RenrenScheduler', 'TASK_1067246875800000076', 'DEFAULT', '0 0/30 * * * ?', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint NOT NULL,
  `SCHED_TIME` bigint NOT NULL,
  `PRIORITY` int NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
INSERT INTO `qrtz_job_details` VALUES ('RenrenScheduler', 'TASK_1067246875800000076', 'DEFAULT', null, 'com.xz.admin.modules.job.utils.ScheduleJob', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B455973720031636F6D2E787A2E61646D696E2E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200074C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B4C000A757064617465446174657400104C6A6176612F7574696C2F446174653B4C0007757064617465727400104C6A6176612F6C616E672F4C6F6E673B78720022636F6D2E787A2E61646D696E2E6170692E656E746974792E42617365456E7469747947165D783C412BD50200034C000A6372656174654461746571007E000B4C000763726561746F7271007E000C4C0002696471007E000C78707372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000186D6C4C850787372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700ECF9F6107B456017371007E00110ECF9F6107B4564C740008746573745461736B74000E3020302F3330202A202A202A203F74000672656E72656E740025E69C89E58F82E6B58BE8AF95EFBC8CE5A49AE4B8AAE58F82E695B0E4BDBFE794A86A736F6E737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0012000000007371007E000F770800000186D6C4C850787371007E00110ECF9F6107B456017800);

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('RenrenScheduler', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('RenrenScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint NOT NULL,
  `CHECKIN_INTERVAL` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state` VALUES ('RenrenScheduler', 'DESKTOP-CB2AGTE1679929020184', '1679929894836', '15000');

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint NOT NULL,
  `REPEAT_INTERVAL` bigint NOT NULL,
  `TIMES_TRIGGERED` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int DEFAULT NULL,
  `INT_PROP_2` int DEFAULT NULL,
  `LONG_PROP_1` bigint DEFAULT NULL,
  `LONG_PROP_2` bigint DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint DEFAULT NULL,
  `PREV_FIRE_TIME` bigint DEFAULT NULL,
  `PRIORITY` int DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint NOT NULL,
  `END_TIME` bigint DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
INSERT INTO `qrtz_triggers` VALUES ('RenrenScheduler', 'TASK_1067246875800000076', 'DEFAULT', 'TASK_1067246875800000076', 'DEFAULT', null, '1678671000000', '-1', '5', 'PAUSED', 'CRON', '1678669351000', '0', null, '2', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B455973720031636F6D2E787A2E61646D696E2E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200074C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B4C000A757064617465446174657400104C6A6176612F7574696C2F446174653B4C0007757064617465727400104C6A6176612F6C616E672F4C6F6E673B7872001F636F6D2E787A2E636F6D6D6F6E2E656E746974792E42617365456E74697479D65FD9B309C198B10200034C000A6372656174654461746571007E000B4C000763726561746F7271007E000C4C0002696471007E000C78707372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000186D6C4C850787372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700ECF9F6107B456017371007E00110ECF9F6107B4564C740008746573745461736B74000E3020302F3330202A202A202A203F74000672656E72656E740025E69C89E58F82E6B58BE8AF95EFBC8CE5A49AE4B8AAE58F82E695B0E4BDBFE794A86A736F6E737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0012000000007371007E000F770800000186D6C4C850787371007E00110ECF9F6107B456017800);

-- ----------------------------
-- Table structure for schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job` (
  `id` bigint NOT NULL COMMENT 'id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(100) DEFAULT NULL COMMENT 'cron表达式',
  `status` tinyint unsigned DEFAULT NULL COMMENT '任务状态  0：暂停  1：正常',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` bigint DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务';

-- ----------------------------
-- Records of schedule_job
-- ----------------------------
INSERT INTO `schedule_job` VALUES ('1067246875800000076', 'testTask', 'renren', '0 0/30 * * * ?', '0', '有参测试，多个参数使用json', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');

-- ----------------------------
-- Table structure for schedule_job_log
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job_log`;
CREATE TABLE `schedule_job_log` (
  `id` bigint NOT NULL COMMENT 'id',
  `job_id` bigint NOT NULL COMMENT '任务id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean名称',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `status` tinyint unsigned NOT NULL COMMENT '任务状态    0：失败    1：成功',
  `error` varchar(2000) DEFAULT NULL COMMENT '失败信息',
  `times` int NOT NULL COMMENT '耗时(单位：毫秒)',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_job_id` (`job_id`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务日志';

-- ----------------------------
-- Records of schedule_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL COMMENT 'id',
  `pid` bigint DEFAULT NULL COMMENT '上级ID',
  `pids` varchar(500) DEFAULT NULL COMMENT '所有上级ID，用逗号分开',
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `sort` int unsigned DEFAULT NULL COMMENT '排序',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` bigint DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_pid` (`pid`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1067246875800000062', '1067246875800000063', '1067246875800000066,1067246875800000063', '技术部', '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dept` VALUES ('1067246875800000063', '1067246875800000066', '1067246875800000066', '长沙分公司', '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dept` VALUES ('1067246875800000064', '1067246875800000066', '1067246875800000066', '上海分公司', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dept` VALUES ('1067246875800000065', '1067246875800000064', '1067246875800000066,1067246875800000064', '市场部', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dept` VALUES ('1067246875800000066', '0', '0', '人人开源集团', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dept` VALUES ('1067246875800000067', '1067246875800000064', '1067246875800000066,1067246875800000064', '销售部', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dept` VALUES ('1067246875800000068', '1067246875800000063', '1067246875800000066,1067246875800000063', '产品部', '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id` bigint NOT NULL COMMENT 'id',
  `dict_type_id` bigint NOT NULL COMMENT '字典类型ID',
  `dict_label` varchar(255) NOT NULL COMMENT '字典标签',
  `dict_value` varchar(255) DEFAULT NULL COMMENT '字典值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `sort` int unsigned DEFAULT NULL COMMENT '排序',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` bigint DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_type_value` (`dict_type_id`,`dict_value`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES ('1160061112075464705', '1160061077912858625', '男', '0', '', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dict_data` VALUES ('1160061146967879681', '1160061077912858625', '女', '1', '', '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dict_data` VALUES ('1160061190127267841', '1160061077912858625', '保密', '2', '', '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dict_data` VALUES ('1225814069634195457', '1225813644059140097', '公告', '0', '', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dict_data` VALUES ('1225814107559092225', '1225813644059140097', '会议', '1', '', '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dict_data` VALUES ('1225814271879340034', '1225813644059140097', '其他', '2', '', '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` bigint NOT NULL COMMENT 'id',
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
  `dict_name` varchar(255) NOT NULL COMMENT '字典名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `sort` int unsigned DEFAULT NULL COMMENT '排序',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` bigint DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES ('1160061077912858625', 'gender', '性别', '', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_dict_type` VALUES ('1225813644059140097', 'notice_type', '站内通知-类型', '', '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');

-- ----------------------------
-- Table structure for sys_log_error
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_error`;
CREATE TABLE `sys_log_error` (
  `id` bigint NOT NULL COMMENT 'id',
  `request_uri` varchar(200) DEFAULT NULL COMMENT '请求URI',
  `request_method` varchar(20) DEFAULT NULL COMMENT '请求方式',
  `request_params` text COMMENT '请求参数',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `ip` varchar(32) DEFAULT NULL COMMENT '操作IP',
  `error_info` text COMMENT '异常信息',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='异常日志';

-- ----------------------------
-- Records of sys_log_error
-- ----------------------------
INSERT INTO `sys_log_error` VALUES ('1635083996159049730', '/renren-admin/sys/menu/nav', 'GET', '{\"_t\":\"1678669377645\"}', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'org.springframework.jdbc.BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'t1.menu_type\' in \'where clause\'\r\n### The error may exist in file [D:\\project\\xzcc-root\\ccimgcloud\\ccimgcloud-admin\\target\\classes\\mapper\\sys\\SysMenuDao.xml]\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: select t1.* from sys_menu t1    WHERE t1.menu_type = ?    order by t1.sort asc\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'t1.menu_type\' in \'where clause\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'t1.menu_type\' in \'where clause\'\r\n	at org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator.doTranslate(SQLErrorCodeSQLExceptionTranslator.java:235)\r\n	at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:72)\r\n	at org.mybatis.spring.MyBatisExceptionTranslator.translateExceptionIfPossible(MyBatisExceptionTranslator.java:91)\r\n	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:441)\r\n	at com.sun.proxy.$Proxy91.selectList(Unknown Source)\r\n	at org.mybatis.spring.SqlSessionTemplate.selectList(SqlSessionTemplate.java:224)\r\n	at com.baomidou.mybatisplus.core.override.MybatisMapperMethod.executeForMany(MybatisMapperMethod.java:166)\r\n	at com.baomidou.mybatisplus.core.override.MybatisMapperMethod.execute(MybatisMapperMethod.java:77)\r\n	at com.baomidou.mybatisplus.core.override.MybatisMapperProxy$PlainMethodInvoker.invoke(MybatisMapperProxy.java:148)\r\n	at com.baomidou.mybatisplus.core.override.MybatisMapperProxy.invoke(MybatisMapperProxy.java:89)\r\n	at com.sun.proxy.$Proxy92.getMenuList(Unknown Source)\r\n	at com.xz.admin.modules.sys.service.impl.SysMenuServiceImpl.getUserMenuList(SysMenuServiceImpl.java:92)\r\n	at com.xz.admin.modules.sys.service.impl.SysMenuServiceImpl$$FastClassBySpringCGLIB$$64a30fb8.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:687)\r\n	at com.xz.admin.modules.sys.service.impl.SysMenuServiceImpl$$EnhancerBySpringCGLIB$$c17558d2.getUserMenuList(<generated>)\r\n	at com.xz.admin.modules.sys.controller.SysMenuController.nav(SysMenuController.java:51)\r\n	at com.xz.admin.modules.sys.controller.SysMenuController$$FastClassBySpringCGLIB$$8b2ed69c.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:771)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:95)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:749)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:691)\r\n	at com.xz.admin.modules.sys.controller.SysMenuController$$EnhancerBySpringCGLIB$$d53d5f1a.nav(<generated>)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:105)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:878)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:792)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\r\n	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:626)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:733)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:61)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.executeChain(AdviceFilter.java:108)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:137)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:154)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:66)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.executeChain(AdviceFilter.java:108)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:137)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:154)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:66)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.executeChain(AbstractShiroFilter.java:458)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter$1.call(AbstractShiroFilter.java:373)\r\n	at org.apache.shiro.subject.support.SubjectCallable.doCall(SubjectCallable.java:90)\r\n	at org.apache.shiro.subject.support.SubjectCallable.call(SubjectCallable.java:83)\r\n	at org.apache.shiro.subject.support.DelegatingSubject.execute(DelegatingSubject.java:387)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.doFilterInternal(AbstractShiroFilter.java:370)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:154)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.xz.admin.api.xss.XssFilter.doFilter(XssFilter.java:30)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:61)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.executeChain(AdviceFilter.java:108)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:137)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:154)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:66)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.executeChain(AdviceFilter.java:108)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:137)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:154)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:66)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.executeChain(AbstractShiroFilter.java:458)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter$1.call(AbstractShiroFilter.java:373)\r\n	at org.apache.shiro.subject.support.SubjectCallable.doCall(SubjectCallable.java:90)\r\n	at org.apache.shiro.subject.support.SubjectCallable.call(SubjectCallable.java:83)\r\n	at org.apache.shiro.subject.support.DelegatingSubject.execute(DelegatingSubject.java:387)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.doFilterInternal(AbstractShiroFilter.java:370)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:154)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:358)\r\n	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:271)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)\r\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)\r\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:541)\r\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139)\r\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\r\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\r\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\r\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:373)\r\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\r\n	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:868)\r\n	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1589)\r\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\r\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\r\n	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)\r\n	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\r\n	at java.lang.Thread.run(Thread.java:748)\r\nCaused by: java.sql.SQLSyntaxErrorException: Unknown column \'t1.menu_type\' in \'where clause\'\r\n	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:120)\r\n	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:97)\r\n	at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122)\r\n	at com.mysql.cj.jdbc.ClientPreparedStatement.executeInternal(ClientPreparedStatement.java:953)\r\n	at com.mysql.cj.jdbc.ClientPreparedStatement.execute(ClientPreparedStatement.java:370)\r\n	at com.alibaba.druid.pool.DruidPooledPreparedStatement.execute(DruidPooledPreparedStatement.java:483)\r\n	at sun.reflect.GeneratedMethodAccessor94.invoke(Unknown Source)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.logging.jdbc.PreparedStatementLogger.invoke(PreparedStatementLogger.java:59)\r\n	at com.sun.proxy.$Proxy172.execute(Unknown Source)\r\n	at org.apache.ibatis.executor.statement.PreparedStatementHandler.query(PreparedStatementHandler.java:64)\r\n	at org.apache.ibatis.executor.statement.RoutingStatementHandler.query(RoutingStatementHandler.java:79)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:64)\r\n	at com.sun.proxy.$Proxy170.query(Unknown Source)\r\n	at org.apache.ibatis.executor.SimpleExecutor.doQuery(SimpleExecutor.java:63)\r\n	at org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:325)\r\n	at org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156)\r\n	at com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor.intercept(MybatisPlusInterceptor.java:81)\r\n	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:62)\r\n	at com.sun.proxy.$Proxy169.query(Unknown Source)\r\n	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:151)\r\n	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:145)\r\n	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:140)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:427)\r\n	... 114 more\r\n', '1067246875800000001', '2023-03-13 09:02:58');

-- ----------------------------
-- Table structure for sys_log_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_login`;
CREATE TABLE `sys_log_login` (
  `id` bigint NOT NULL COMMENT 'id',
  `operation` tinyint unsigned DEFAULT NULL COMMENT '用户操作   0：用户登录   1：用户退出',
  `status` tinyint unsigned NOT NULL COMMENT '状态  0：失败    1：成功    2：账号已锁定',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `ip` varchar(32) DEFAULT NULL COMMENT '操作IP',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='登录日志';

-- ----------------------------
-- Records of sys_log_login
-- ----------------------------
INSERT INTO `sys_log_login` VALUES ('1635083995680899073', '0', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-13 09:02:58');
INSERT INTO `sys_log_login` VALUES ('1635084620221222914', '0', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-13 09:05:26');
INSERT INTO `sys_log_login` VALUES ('1635092579944853506', '1', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-13 09:37:04');
INSERT INTO `sys_log_login` VALUES ('1635092608218656770', '0', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-13 09:37:11');
INSERT INTO `sys_log_login` VALUES ('1636203286941728769', '0', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-16 11:10:37');
INSERT INTO `sys_log_login` VALUES ('1636204147868766209', '1', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-16 11:14:03');
INSERT INTO `sys_log_login` VALUES ('1636204174544539649', '0', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-16 11:14:09');
INSERT INTO `sys_log_login` VALUES ('1636205288056754177', '1', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-16 11:18:35');
INSERT INTO `sys_log_login` VALUES ('1636205315248427010', '0', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-16 11:18:41');
INSERT INTO `sys_log_login` VALUES ('1636232322241859586', '0', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-16 13:06:00');
INSERT INTO `sys_log_login` VALUES ('1636549631707754498', '0', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-17 10:06:52');
INSERT INTO `sys_log_login` VALUES ('1636563596408098818', '0', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-17 11:02:22');
INSERT INTO `sys_log_login` VALUES ('1640368752169951234', '0', '1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', 'admin', '1067246875800000001', '2023-03-27 23:02:42');

-- ----------------------------
-- Table structure for sys_log_operation
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_operation`;
CREATE TABLE `sys_log_operation` (
  `id` bigint NOT NULL COMMENT 'id',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `request_uri` varchar(200) DEFAULT NULL COMMENT '请求URI',
  `request_method` varchar(20) DEFAULT NULL COMMENT '请求方式',
  `request_params` text COMMENT '请求参数',
  `request_time` int unsigned NOT NULL COMMENT '请求时长(毫秒)',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `ip` varchar(32) DEFAULT NULL COMMENT '操作IP',
  `status` tinyint unsigned NOT NULL COMMENT '状态  0：失败   1：成功',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志';

-- ----------------------------
-- Records of sys_log_operation
-- ----------------------------
INSERT INTO `sys_log_operation` VALUES ('1636204558520487938', '保存', '/ccimgcloud-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":0,\"children\":[],\"name\":\"平台管理\",\"url\":\"\",\"menuType\":null,\"icon\":\"icon-layout\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"一级菜单\"}', '23', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:15:41');
INSERT INTO `sys_log_operation` VALUES ('1636204647490064386', '修改', '/ccimgcloud-admin/sys/menu', 'PUT', '{\"id\":1067246875800000046,\"pid\":0,\"children\":[],\"name\":\"日志管理\",\"url\":null,\"menuType\":0,\"icon\":\"icon-container\",\"permissions\":null,\"sort\":10,\"createDate\":null,\"parentName\":\"一级菜单\"}', '23', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:16:02');
INSERT INTO `sys_log_operation` VALUES ('1636204668843266049', '修改', '/ccimgcloud-admin/sys/menu', 'PUT', '{\"id\":1636204558470156289,\"pid\":0,\"children\":[],\"name\":\"平台管理\",\"url\":\"\",\"menuType\":null,\"icon\":\"icon-layout\",\"permissions\":\"\",\"sort\":11,\"createDate\":null,\"parentName\":\"一级菜单\"}', '20', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:16:07');
INSERT INTO `sys_log_operation` VALUES ('1636204711503532034', '修改', '/ccimgcloud-admin/sys/menu', 'PUT', '{\"id\":1636204558470156289,\"pid\":0,\"children\":[],\"name\":\"平台管理\",\"url\":\"\",\"menuType\":null,\"icon\":\"icon-layout\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"一级菜单\"}', '13', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:16:17');
INSERT INTO `sys_log_operation` VALUES ('1636204742579130370', '修改', '/ccimgcloud-admin/sys/menu', 'PUT', '{\"id\":1067246875800000053,\"pid\":0,\"children\":[],\"name\":\"系统监控\",\"url\":null,\"menuType\":0,\"icon\":\"icon-desktop\",\"permissions\":null,\"sort\":11,\"createDate\":null,\"parentName\":\"一级菜单\"}', '12', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:16:25');
INSERT INTO `sys_log_operation` VALUES ('1636205223628050433', '修改', '/ccimgcloud-admin/sys/menu', 'PUT', '{\"id\":1636204558470156289,\"pid\":0,\"children\":[],\"name\":\"平台管理\",\"url\":\"\",\"menuType\":null,\"icon\":\"icon-layout\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"一级菜单\"}', '14', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:18:19');
INSERT INTO `sys_log_operation` VALUES ('1636205519280345089', '保存', '/ccimgcloud-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1067246875800000035,\"children\":[],\"name\":\"1\",\"url\":\"\",\"menuType\":null,\"icon\":\"icon-codelibrary\",\"permissions\":\"\",\"sort\":5,\"createDate\":null,\"parentName\":\"一级菜单\"}', '11', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:19:30');
INSERT INTO `sys_log_operation` VALUES ('1636205776034664450', '删除', '/ccimgcloud-admin/sys/menu/1636205519259373569', 'DELETE', '1636205519259373569', '30', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:20:31');
INSERT INTO `sys_log_operation` VALUES ('1636205845978877954', '保存', '/ccimgcloud-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":0,\"children\":[],\"name\":\"1\",\"url\":\"\",\"menuType\":null,\"icon\":\"icon-location\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"一级菜单\"}', '13', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:20:48');
INSERT INTO `sys_log_operation` VALUES ('1636205950442213377', '删除', '/ccimgcloud-admin/sys/menu/1636205845962100737', 'DELETE', '1636205845962100737', '8', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:21:12');
INSERT INTO `sys_log_operation` VALUES ('1636210341660397569', '保存', '/ccimgcloud-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1636204558470156289,\"children\":[],\"name\":\"图片管理\",\"url\":\"/platform/imgdetails\",\"menuType\":null,\"icon\":\"icon-image\",\"permissions\":\"\",\"sort\":1,\"createDate\":null,\"parentName\":\"平台管理\"}', '11', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:38:39');
INSERT INTO `sys_log_operation` VALUES ('1636210620480950273', '保存', '/ccimgcloud-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1636204558470156289,\"children\":[],\"name\":\"分类管理\",\"url\":\"platform/category\",\"menuType\":null,\"icon\":\"icon-up-circle\",\"permissions\":\"\",\"sort\":3,\"createDate\":null,\"parentName\":\"一级菜单\"}', '10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:39:46');
INSERT INTO `sys_log_operation` VALUES ('1636210656707153922', '修改', '/ccimgcloud-admin/sys/menu', 'PUT', '{\"id\":1234312324343454423,\"pid\":1636204558470156289,\"children\":[],\"name\":\"用户管理\",\"url\":\"platform/user\",\"menuType\":0,\"icon\":\"icon-user\",\"permissions\":null,\"sort\":0,\"createDate\":null,\"parentName\":\"平台管理\"}', '18', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:39:55');
INSERT INTO `sys_log_operation` VALUES ('1636210669218762754', '修改', '/ccimgcloud-admin/sys/menu', 'PUT', '{\"id\":1636210341643620353,\"pid\":1636204558470156289,\"children\":[],\"name\":\"图片管理\",\"url\":\"platform/imgdetails\",\"menuType\":null,\"icon\":\"icon-image\",\"permissions\":\"\",\"sort\":1,\"createDate\":null,\"parentName\":\"平台管理\"}', '25', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:39:58');
INSERT INTO `sys_log_operation` VALUES ('1636210886873780226', '保存', '/ccimgcloud-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1636204558470156289,\"children\":[],\"name\":\"评论管理\",\"url\":\"platform/comment\",\"menuType\":null,\"icon\":\"icon-Import\",\"permissions\":\"\",\"sort\":4,\"createDate\":null,\"parentName\":\"一级菜单\"}', '10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:40:49');
INSERT INTO `sys_log_operation` VALUES ('1636210991223869441', '保存', '/ccimgcloud-admin/sys/menu', 'POST', '{\"id\":null,\"pid\":1636204558470156289,\"children\":[],\"name\":\"标签管理\",\"url\":\"platform/tag\",\"menuType\":null,\"icon\":\"icon-trademark\",\"permissions\":\"\",\"sort\":5,\"createDate\":null,\"parentName\":\"一级菜单\"}', '12', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36', '0:0:0:0:0:0:0:1', '1', 'admin', '1067246875800000001', '2023-03-16 11:41:14');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL COMMENT 'id',
  `pid` bigint DEFAULT NULL COMMENT '上级ID，一级菜单为0',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `permissions` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:list,sys:user:save)',
  `menu_type` tinyint unsigned DEFAULT NULL COMMENT '类型   0：菜单   1：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `sort` int DEFAULT NULL COMMENT '排序',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` bigint DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_pid` (`pid`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1067246875800000002', '0', '权限管理', null, null, '0', 'icon-safetycertificate', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000003', '1067246875800000055', '新增', null, 'sys:user:save,sys:dept:list,sys:role:list', '1', null, '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000004', '1067246875800000055', '修改', null, 'sys:user:update,sys:dept:list,sys:role:list', '1', null, '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000005', '1067246875800000055', '删除', null, 'sys:user:delete', '1', null, '3', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000006', '1067246875800000055', '导出', null, 'sys:user:export', '1', null, '4', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000007', '1067246875800000002', '角色管理', 'sys/role', null, '0', 'icon-team', '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000008', '1067246875800000007', '查看', null, 'sys:role:page,sys:role:info', '1', null, '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000009', '1067246875800000007', '新增', null, 'sys:role:save,sys:menu:select,sys:dept:list', '1', null, '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000010', '1067246875800000007', '修改', null, 'sys:role:update,sys:menu:select,sys:dept:list', '1', null, '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000011', '1067246875800000007', '删除', null, 'sys:role:delete', '1', null, '3', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000012', '1067246875800000002', '部门管理', 'sys/dept', null, '0', 'icon-apartment', '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000014', '1067246875800000012', '查看', null, 'sys:dept:list,sys:dept:info', '1', null, '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000015', '1067246875800000012', '新增', null, 'sys:dept:save', '1', null, '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000016', '1067246875800000012', '修改', null, 'sys:dept:update', '1', null, '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000017', '1067246875800000012', '删除', null, 'sys:dept:delete', '1', null, '3', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000025', '1067246875800000035', '菜单管理', 'sys/menu', null, '0', 'icon-unorderedlist', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000026', '1067246875800000025', '查看', null, 'sys:menu:list,sys:menu:info', '1', null, '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000027', '1067246875800000025', '新增', null, 'sys:menu:save', '1', null, '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000028', '1067246875800000025', '修改', null, 'sys:menu:update', '1', null, '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000029', '1067246875800000025', '删除', null, 'sys:menu:delete', '1', null, '3', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000030', '1067246875800000035', '定时任务', 'job/schedule', null, '0', 'icon-dashboard', '3', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000031', '1067246875800000030', '查看', null, 'sys:schedule:page,sys:schedule:info', '1', null, '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000032', '1067246875800000030', '新增', null, 'sys:schedule:save', '1', null, '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000033', '1067246875800000030', '修改', null, 'sys:schedule:update', '1', null, '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000034', '1067246875800000030', '删除', null, 'sys:schedule:delete', '1', null, '3', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000035', '0', '系统设置', null, null, '0', 'icon-setting', '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000036', '1067246875800000030', '暂停', null, 'sys:schedule:pause', '1', null, '4', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000037', '1067246875800000030', '恢复', null, 'sys:schedule:resume', '1', null, '5', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000038', '1067246875800000030', '立即执行', null, 'sys:schedule:run', '1', null, '6', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000039', '1067246875800000030', '日志列表', null, 'sys:schedule:log', '1', null, '7', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000040', '1067246875800000035', '参数管理', 'sys/params', '', '0', 'icon-fileprotect', '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000041', '1067246875800000035', '字典管理', 'sys/dict-type', null, '0', 'icon-golden-fill', '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000042', '1067246875800000041', '查看', null, 'sys:dict:page,sys:dict:info', '1', null, '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000043', '1067246875800000041', '新增', null, 'sys:dict:save', '1', null, '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000044', '1067246875800000041', '修改', null, 'sys:dict:update', '1', null, '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000045', '1067246875800000041', '删除', null, 'sys:dict:delete', '1', null, '3', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000046', '0', '日志管理', null, null, '0', 'icon-container', '10', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-16 11:16:02');
INSERT INTO `sys_menu` VALUES ('1067246875800000047', '1067246875800000035', '文件上传', 'oss/oss', 'sys:oss:all', '0', 'icon-upload', '4', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000048', '1067246875800000046', '登录日志', 'sys/log-login', 'sys:log:login', '0', 'icon-filedone', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000049', '1067246875800000046', '操作日志', 'sys/log-operation', 'sys:log:operation', '0', 'icon-solution', '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000050', '1067246875800000046', '异常日志', 'sys/log-error', 'sys:log:error', '0', 'icon-file-exception', '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000051', '1067246875800000053', 'SQL监控', '{{ window.SITE_CONFIG[\"apiURL\"] }}/druid/sql.html', null, '0', 'icon-database', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000053', '0', '系统监控', null, null, '0', 'icon-desktop', '11', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-16 11:16:24');
INSERT INTO `sys_menu` VALUES ('1067246875800000055', '1067246875800000002', '用户管理', 'sys/user', null, '0', 'icon-user', '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000056', '1067246875800000055', '查看', null, 'sys:user:page,sys:user:info', '1', null, '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000057', '1067246875800000040', '新增', null, 'sys:params:save', '1', null, '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000058', '1067246875800000040', '导出', null, 'sys:params:export', '1', null, '4', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000059', '1067246875800000040', '查看', '', 'sys:params:page,sys:params:info', '1', null, '0', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000060', '1067246875800000040', '修改', null, 'sys:params:update', '1', null, '2', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1067246875800000061', '1067246875800000040', '删除', '', 'sys:params:delete', '1', '', '3', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1156748733921165314', '1067246875800000053', '接口文档', '{{ window.SITE_CONFIG[\"apiURL\"] }}/doc.html', '', '0', 'icon-file-word', '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');
INSERT INTO `sys_menu` VALUES ('1234312324343454423', '1636204558470156289', '用户管理', 'platform/user', null, '0', 'icon-user', '0', '1067246875800000001', '2023-03-16 11:23:47', '1067246875800000001', '2023-03-16 11:39:55');
INSERT INTO `sys_menu` VALUES ('1636204558470156289', '0', '平台管理', '', '', '0', 'icon-layout', '3', '1067246875800000001', '2023-03-16 11:15:41', '1067246875800000001', '2023-03-16 11:18:19');
INSERT INTO `sys_menu` VALUES ('1636210341643620353', '1636204558470156289', '图片管理', 'platform/imgdetails', '', '0', 'icon-image', '1', '1067246875800000001', '2023-03-16 11:38:39', '1067246875800000001', '2023-03-16 11:39:58');
INSERT INTO `sys_menu` VALUES ('1636210620468367362', '1636204558470156289', '分类管理', 'platform/category', '', '0', 'icon-up-circle', '3', '1067246875800000001', '2023-03-16 11:39:46', '1067246875800000001', '2023-03-16 11:39:46');
INSERT INTO `sys_menu` VALUES ('1636210886857003009', '1636204558470156289', '评论管理', 'platform/comment', '', '0', 'icon-Import', '4', '1067246875800000001', '2023-03-16 11:40:49', '1067246875800000001', '2023-03-16 11:40:49');
INSERT INTO `sys_menu` VALUES ('1636210991202897921', '1636204558470156289', '标签管理', 'platform/tag', '', '0', 'icon-trademark', '5', '1067246875800000001', '2023-03-16 11:41:14', '1067246875800000001', '2023-03-16 11:41:14');

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss` (
  `id` bigint NOT NULL COMMENT 'id',
  `url` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件上传';

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_params
-- ----------------------------
DROP TABLE IF EXISTS `sys_params`;
CREATE TABLE `sys_params` (
  `id` bigint NOT NULL COMMENT 'id',
  `param_code` varchar(32) DEFAULT NULL COMMENT '参数编码',
  `param_value` varchar(2000) DEFAULT NULL COMMENT '参数值',
  `param_type` tinyint unsigned DEFAULT '1' COMMENT '类型   0：系统参数   1：非系统参数',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` bigint DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_param_code` (`param_code`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参数管理';

-- ----------------------------
-- Records of sys_params
-- ----------------------------
INSERT INTO `sys_params` VALUES ('1067246875800000073', 'CLOUD_STORAGE_CONFIG_KEY', '{\"type\":1,\"qiniuDomain\":\"http://test.oss.renren.io\",\"qiniuPrefix\":\"upload\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"qiniuBucketName\":\"renren-oss\",\"aliyunDomain\":\"\",\"aliyunPrefix\":\"\",\"aliyunEndPoint\":\"\",\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qcloudBucketName\":\"\"}', '0', '云存储配置信息', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL COMMENT 'id',
  `name` varchar(50) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` bigint DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色管理';

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_data_scope`;
CREATE TABLE `sys_role_data_scope` (
  `id` bigint NOT NULL COMMENT 'id',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色数据权限';

-- ----------------------------
-- Records of sys_role_data_scope
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL COMMENT 'id',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单ID',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色菜单关系';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` bigint NOT NULL COMMENT 'id',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色用户关系';

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL COMMENT 'id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `head_url` varchar(200) DEFAULT NULL COMMENT '头像',
  `gender` tinyint unsigned DEFAULT NULL COMMENT '性别   0：男   1：女    2：保密',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `super_admin` tinyint unsigned DEFAULT NULL COMMENT '超级管理员   0：否   1：是',
  `status` tinyint DEFAULT NULL COMMENT '状态  0：停用   1：正常',
  `creator` bigint DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` bigint DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1067246875800000001', 'admin', '$2a$10$012Kx2ba5jzqr9gLlG4MX.bnQJTD9UWqF57XDo2N3.fPtLne02u/m', '管理员', null, '0', 'root@renren.io', '13612345678', null, '1', '1', '1067246875800000001', '2023-03-13 01:01:06', '1067246875800000001', '2023-03-13 01:01:06');

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token` (
  `id` bigint NOT NULL COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `token` varchar(100) NOT NULL COMMENT '用户token',
  `expire_date` datetime DEFAULT NULL COMMENT '过期时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户Token';

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
INSERT INTO `sys_user_token` VALUES ('1635083995731230722', '1067246875800000001', '94bf887a2b39773f6e709b9da272341d', '2023-03-28 11:02:42', '2023-03-27 23:02:42', '2023-03-13 09:02:58');
