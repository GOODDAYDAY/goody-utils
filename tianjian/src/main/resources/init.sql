-- auto-generated definition
CREATE TABLE toy
(
    id        bigint           NOT NULL COMMENT '主键' PRIMARY KEY,
    tenant_id bigint DEFAULT 0 NOT NULL COMMENT '租户id',
    user_id   bigint           NOT NULL,
    CONSTRAINT uk_toy_user_id UNIQUE (user_id)
) COMMENT 'toy表' CHARSET = utf8mb4;

CREATE INDEX idx_task_uid_status ON toy(tenant_id);

