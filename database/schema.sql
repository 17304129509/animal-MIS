CREATE DATABASE IF NOT EXISTS campus_animal DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_animal;

CREATE TABLE user (
  user_id     INT          PRIMARY KEY AUTO_INCREMENT COMMENT '用户唯一ID',
  username    VARCHAR(20)  NOT NULL UNIQUE            COMMENT '登录账号',
  password    VARCHAR(64)  NOT NULL                   COMMENT '登录密码',
  nickname    VARCHAR(20)  NOT NULL                   COMMENT '用户昵称',
  phone       VARCHAR(11)                             COMMENT '联系方式',
  role        VARCHAR(10)  NOT NULL DEFAULT 'student' COMMENT '角色',
  love_points INT          NOT NULL DEFAULT 100       COMMENT '爱心积分',
  create_time DATETIME     NOT NULL DEFAULT NOW()     COMMENT '注册时间'
);

CREATE TABLE animal (
  animal_id   INT           PRIMARY KEY AUTO_INCREMENT COMMENT '动物档案ID',
  animal_name VARCHAR(20)   NOT NULL                   COMMENT '动物昵称',
  type        VARCHAR(10)   NOT NULL                   COMMENT '类型',
  gender      VARCHAR(10),
  health      VARCHAR(20),
  color       VARCHAR(20)                              COMMENT '毛色',
  location    VARCHAR(50)   NOT NULL                   COMMENT '常驻位置',
  status      VARCHAR(15)   NOT NULL DEFAULT '健康'    COMMENT '状态',
  photo_url   VARCHAR(200)                             COMMENT '照片URL',
  remark      VARCHAR(200)                             COMMENT '备注',
  is_approved TINYINT(1)    NOT NULL DEFAULT 0         COMMENT '是否审核通过',
  create_user INT           NOT NULL                   COMMENT '创建人',
  create_time DATETIME      NOT NULL DEFAULT NOW()     COMMENT '建档时间',
  FOREIGN KEY (create_user) REFERENCES user(user_id)
);

CREATE TABLE feed_point (
  point_id         INT          PRIMARY KEY AUTO_INCREMENT COMMENT '点位ID',
  point_name       VARCHAR(30)  NOT NULL                   COMMENT '点位名称',
  location         VARCHAR(50)  NOT NULL                   COMMENT '位置描述',
  food_status      VARCHAR(10)  NOT NULL DEFAULT '充足'    COMMENT '物资状态',
  last_update_user INT                                     COMMENT '最后更新人',
  update_time      DATETIME     NOT NULL DEFAULT NOW()     COMMENT '更新时间',
  FOREIGN KEY (last_update_user) REFERENCES user(user_id)
);

CREATE TABLE feed_record (
  record_id INT            PRIMARY KEY AUTO_INCREMENT COMMENT '打卡ID',
  user_id   INT            NOT NULL                   COMMENT '用户ID',
  point_id  INT            NOT NULL                   COMMENT '点位ID',
  food_type VARCHAR(20)                               COMMENT '食物类型',
  amount    DECIMAL(10,2)                             COMMENT '捐赠金额',
  quantity  INT            NOT NULL DEFAULT 1         COMMENT '投喂数量',
  remark    VARCHAR(200)                              COMMENT '备注',
  feed_time DATETIME       NOT NULL DEFAULT NOW()     COMMENT '打卡时间',
  FOREIGN KEY (user_id)  REFERENCES user(user_id),
  FOREIGN KEY (point_id) REFERENCES feed_point(point_id)
);

CREATE TABLE rescue_order (
  rescue_id      INT           PRIMARY KEY AUTO_INCREMENT COMMENT '工单ID',
  animal_id      INT                                      COMMENT '关联动物',
  publish_user   INT           NOT NULL                   COMMENT '发布人',
  rescue_content VARCHAR(300)  NOT NULL                   COMMENT '救助描述',
  location       VARCHAR(50)   NOT NULL                   COMMENT '位置',
  urgency        VARCHAR(10),
  order_status   VARCHAR(15)   NOT NULL DEFAULT '待处理'  COMMENT '工单状态',
  deal_user      INT                                      COMMENT '处理人',
  result         VARCHAR(200)                             COMMENT '救助结果',
  create_time    DATETIME      NOT NULL DEFAULT NOW()     COMMENT '创建时间',
  FOREIGN KEY (animal_id)    REFERENCES animal(animal_id),
  FOREIGN KEY (publish_user) REFERENCES user(user_id),
  FOREIGN KEY (deal_user)    REFERENCES user(user_id)
);

CREATE TABLE adoption (
  id              INT          PRIMARY KEY AUTO_INCREMENT COMMENT '申请ID',
  animal_id       INT          NOT NULL                   COMMENT '动物ID',
  applicant_id    INT          NOT NULL                   COMMENT '申请人ID',
  reason          VARCHAR(500) NOT NULL                   COMMENT '申请理由',
  contact         VARCHAR(20)                             COMMENT '联系方式',
  status          VARCHAR(20)  NOT NULL DEFAULT '待审核'  COMMENT '状态',
  review_comment  VARCHAR(500)                            COMMENT '审核意见',
  review_user_id  INT                                     COMMENT '审核人ID',
  apply_time      DATETIME     NOT NULL DEFAULT NOW()     COMMENT '申请时间',
  review_time     DATETIME                                COMMENT '审核时间',
  FOREIGN KEY (applicant_id)  REFERENCES user(user_id),
  FOREIGN KEY (review_user_id) REFERENCES user(user_id)
);
