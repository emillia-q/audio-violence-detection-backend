CREATE TABLE user_relationships (
    user_id BIGINT NOT NULL, --
    trusted_user_id BIGINT NOT NULL,
    custom_nickname VARCHAR(100),

    PRIMARY KEY (user_id, trusted_user_id),
    CONSTRAINT fk_relationship_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_relationship_trusted FOREIGN KEY (trusted_user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_relationship_trusted ON user_relationships(trusted_user_id);

-- old data migration
INSERT INTO user_relationships (user_id, trusted_user_id, custom_nickname)
SELECT id, trusted_user_id, 'Supervised User' -- domain nick
FROM users
WHERE trusted_user_id IS NOT NULL;

-- remove association & old column from users table
ALTER TABLE users DROP CONSTRAINT fk_users_trusted_user;
ALTER TABLE users DROP COLUMN trusted_user_id;