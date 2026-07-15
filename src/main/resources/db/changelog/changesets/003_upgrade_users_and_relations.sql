CREATE TABLE user_relationships (
    user_id BIGINT NOT NULL, --
    trusted_user_id BIGINT NOT NULL,
    nickname_for_trusted VARCHAR(100), -- how user names his trusted user
    nickname_for_supervised VARCHAR(100), -- how trusted user names his supervised user

    PRIMARY KEY (user_id, trusted_user_id),
    CONSTRAINT fk_relationship_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_relationship_trusted FOREIGN KEY (trusted_user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_relationship_trusted ON user_relationships(trusted_user_id);

-- old data migration
INSERT INTO user_relationships (user_id, trusted_user_id, nickname_for_trusted, nickname_for_supervised)
SELECT id, trusted_user_id, 'My Guardian', 'My Supervised User' -- domain nicks
FROM users
WHERE trusted_user_id IS NOT NULL;

-- remove association & old column from users table
ALTER TABLE users DROP CONSTRAINT fk_users_trusted_user;
ALTER TABLE users DROP COLUMN trusted_user_id;