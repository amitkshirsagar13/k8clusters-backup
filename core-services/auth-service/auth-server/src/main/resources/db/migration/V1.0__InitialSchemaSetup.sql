SET @auth_schema_name = concat(substring_index(database(), "_", 1), "_auth_db");

-- CREATE SCHEMA AND TABLES IF NOT EXIST
SET @q = CONCAT('CREATE SCHEMA IF NOT EXISTS ', @auth_schema_name);
PREPARE stmt FROM @q;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @q = CONCAT("CREATE TABLE IF NOT EXISTS ", @auth_schema_name, ".K8CLUSTER_ID_MAPPER (
  `EXTERNAL_ID` varchar(40) NOT NULL,
  `ENTITY_ID` varchar(40) NOT NULL,
  PRIMARY KEY (`EXTERNAL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8");
PREPARE stmt FROM @q;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Create the needed tables in the new schema
SET @q = CONCAT("CREATE TABLE IF NOT EXISTS ", @auth_schema_name, ".USER (
  `id` varchar(40) NOT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `organization` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lqjrcobrh9jc8wpcar64q1bfh` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8");
PREPARE stmt FROM @q;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @q = CONCAT("CREATE TABLE IF NOT EXISTS ", @auth_schema_name, ".ROLE (
  `id` varchar(40) NOT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8");
PREPARE stmt FROM @q;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @q = CONCAT("CREATE TABLE IF NOT EXISTS ", @auth_schema_name, ".USERS_ROLES (
  `user_id` varchar(40) NOT NULL,
  `role_id` varchar(40) NOT NULL,
  KEY `FKt4v0rrweyk393bdgt107vdx0x` (`role_id`),
  KEY `FKgd3iendaoyh04b95ykqise6qh` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8");
PREPARE stmt FROM @q;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
