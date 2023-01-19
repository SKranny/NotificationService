CREATE TABLE IF NOT EXISTS settings (
    id                      BIGSERIAL PRIMARY KEY,
    friend_request          BOOLEAN NOT NULL DEFAULT false,
    friend_birthday         BOOLEAN NOT NULL DEFAULT false,
    post_comment            BOOLEAN NOT NULL DEFAULT false,
    comment_on_comment      BOOLEAN NOT NULL DEFAULT false,
    post                    BOOLEAN NOT NULL DEFAULT false,
    message                 BOOLEAN NOT NULL DEFAULT false,
    phone_notification      BOOLEAN NOT NULL DEFAULT false,
    email_notification      BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS content (
    id                      BIGSERIAL PRIMARY KEY,
    text                    VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS attach_files (
    id                      BIGSERIAL PRIMARY KEY,
    type                    VARCHAR(255) NOT NULL,
    text                    VARCHAR(255) NOT NULL,
    content_id              BIGINT REFERENCES content(id)
);

CREATE TABLE IF NOT EXISTS notification_profile (
    id                      BIGSERIAL PRIMARY KEY,
    setting_id              BIGINT REFERENCES settings(id),
    user_id                 BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS notification (
    id                      BIGSERIAL PRIMARY KEY,
    author_id               BIGINT NOT NULL,
    notification_type       VARCHAR(255),
    content_id              BIGINT REFERENCES content(id),
    is_sent                 BOOLEAN NOT NULL DEFAULT false,
    profile_id              BIGINT REFERENCES notification_profile(id),
    create_datetime         TIMESTAMP WITHOUT TIME ZONE,
    send_datetime           TIMESTAMP WITHOUT TIME ZONE
);
