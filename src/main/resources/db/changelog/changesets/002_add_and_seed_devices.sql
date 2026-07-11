CREATE EXTENSION IF NOT EXISTS pgcrypto;

ALTER TABLE devices ADD COLUMN device_secret VARCHAR(64) NOT NULL;

-- device seeding - hardcoded for development & testing
INSERT INTO devices (mac_address, device_secret, is_activated) VALUES
('AC:A7:04:26:AF:38', encode(digest('Megatajnyklucz1.', 'sha256'), 'hex'), false), -- my esp
('AA:BB:CC:DD:EE:02', encode(digest('Megatajnyklucz1.', 'sha256'), 'hex'), false),
('AA:BB:CC:DD:EE:03', encode(digest('Megatajnyklucz1.', 'sha256'), 'hex'), false),
('AA:BB:CC:DD:EE:04', encode(digest('Megatajnyklucz1.', 'sha256'), 'hex'), false),
('AA:BB:CC:DD:EE:05', encode(digest('Megatajnyklucz1.', 'sha256'), 'hex'), false),
('AA:BB:CC:DD:EE:06', encode(digest('Megatajnyklucz1.', 'sha256'), 'hex'), false),
('AA:BB:CC:DD:EE:07', encode(digest('Megatajnyklucz1.', 'sha256'), 'hex'), false),
('AA:BB:CC:DD:EE:08', encode(digest('Megatajnyklucz1.', 'sha256'), 'hex'), false),
('AA:BB:CC:DD:EE:09', encode(digest('Megatajnyklucz1.', 'sha256'), 'hex'), false),
('AA:BB:CC:DD:EE:10', encode(digest('Megatajnyklucz1.', 'sha256'), 'hex'), false);