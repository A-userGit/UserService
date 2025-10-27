INSERT INTO users (id, name, surname, birth_date, email, external_id)
VALUES (-1, 'test1', 'testS1', now(), 'test1@mail.com', -1),
       (-2, 'test2', 'testS2', now(), 'test2@mail.com', -2),
       (-3, 'test3', 'testS3', now(), 'test3@mail.com', -3),
       (-4, 'test4', 'testS4', now(), 'test4@mail.com', -4)
       ON CONFLICT (id) DO NOTHING;
INSERT INTO card_info (id, user_id, holder, expiration_date, number)
VALUES (-1, -1, 'test1', now(), '4242424242424242'),
       (-2, -1, 'test2', now(), '4242424242424242'),
       (-3, -1, 'test3', now(), '4242424242424242'),
       (-4, -1, 'test4', now(), '4242424242424242')
       ON CONFLICT (id) DO NOTHING;
