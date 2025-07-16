INSERT INTO roles (id,name) VALUES (-1,'client');
INSERT INTO roles (id,name) VALUES (-2,'employee');
INSERT INTO roles (id,name) VALUES (-3,'admin');

INSERT INTO public.users(enabled, id, address, city, country, name, password, phone, surname, username, blocked, activation_pending,using2FA,secret2FA, is_blocked_user)
VALUES (true, -1, 'glcAJwg8FenNhTqAq0OGVw==', '7N8PVIl22Z6QZlIJsszG/g==', 'OOQoFfq3TuqqXYhgbddYyA==', '09yuaa6Or1J+fBhbB6r0fQ==', '$2a$10$DE9tdj0AIpgAN1.TtfVHDuinqJqwU57fxejSZyCmertgqhzXJi90K', 'w7XMX5uRGHdomS9u8D0m0w==', 'pBHWEOTaARdo6d9LPAgYuA==', 'strahinja.banjanac@gmail.com', null, false, false, null, false);

INSERT INTO public.users(enabled, id, address, city, country, name, password, phone, surname, username, blocked, activation_pending,using2FA,secret2FA, is_blocked_user)
VALUES (true, -2, '0tZZGRhuRqzG+U0ALcJ4iA==', '7N8PVIl22Z6QZlIJsszG/g==', 'OOQoFfq3TuqqXYhgbddYyA==', 'bKu1ck9IURxsWWbkC1keoA==', '$2a$10$DE9tdj0AIpgAN1.TtfVHDuinqJqwU57fxejSZyCmertgqhzXJi90K', 'dZZnC3EavLAN6arlr72O8w==', 'UsxYVhGkN4oDUqKtRyz8qA==', 'employee@gmail.com', null, false, false, null, false);

INSERT INTO public.users(enabled, id, address, city, country, name, password, phone, surname, username, blocked, activation_pending, using2FA,secret2FA, is_blocked_user)
VALUES (true, -3, '0tZZGRhuRqzG+U0ALcJ4iA==', '7N8PVIl22Z6QZlIJsszG/g==', 'OOQoFfq3TuqqXYhgbddYyA==', 'Tdf/hDLWSSuhaAu84lqefw==', '$2a$10$DE9tdj0AIpgAN1.TtfVHDuinqJqwU57fxejSZyCmertgqhzXJi90K', '3SpMG1pKCCuH2TlXjUgUUg==', 'LL5ensFYS/dsrEjfzVzR7Q==', 'jblanusa@gmail.com', null, false, false, null, false);

INSERT INTO public.clients(client_type, service_package, client_id)
VALUES (0, 1, -3);

INSERT INTO public.users(enabled, id, address, city, country, name, password, phone, surname, username, blocked, activation_pending, using2FA,secret2FA, is_blocked_user)
VALUES (true, -4, '0tZZGRhuRqzG+U0ALcJ4iA==', '7N8PVIl22Z6QZlIJsszG/g==', 'OOQoFfq3TuqqXYhgbddYyA==', 'bdLnWLWQFglIMuBbqT5yqA==', '$2a$10$DE9tdj0AIpgAN1.TtfVHDuinqJqwU57fxejSZyCmertgqhzXJi90K', '3SpMG1pKCCuH2TlXjUgUUg==', 'zFQYhrxXuAiX/sLfjsywXw==', 'anaranic01@gmail.com', null, false, false, null, false);

INSERT INTO public.clients(client_type, service_package, client_id)
VALUES (0, 2, -4);

INSERT INTO public.users(enabled, id, address, city, country, password, phone, username, blocked, activation_pending, using2FA,secret2FA, is_blocked_user)
VALUES (false, -5, '0tZZGRhuRqzG+U0ALcJ4iA==', '7N8PVIl22Z6QZlIJsszG/g==', 'OOQoFfq3TuqqXYhgbddYyA==',  '$2a$10$DE9tdj0AIpgAN1.TtfVHDuinqJqwU57fxejSZyCmertgqhzXJi90K', '3SpMG1pKCCuH2TlXjUgUUg==', 'cli5@gmail.com', null, false, false, null, false);

INSERT INTO public.clients( client_type, service_package, client_id, company_name, tin)
VALUES (1, 0, -5, 'Kompanija 1', '3846296229');

INSERT INTO public.users(enabled, id, address, city, country, password, phone, username, blocked, activation_pending, using2FA,secret2FA, is_blocked_user)
VALUES (true, -6, '0tZZGRhuRqzG+U0ALcJ4iA==', '7N8PVIl22Z6QZlIJsszG/g==', 'OOQoFfq3TuqqXYhgbddYyA==',  '$2a$10$DE9tdj0AIpgAN1.TtfVHDuinqJqwU57fxejSZyCmertgqhzXJi90K', '3SpMG1pKCCuH2TlXjUgUUg==', 'cli6@gmail.com', null, false, false, null, false);

INSERT INTO public.clients( client_type, service_package, client_id, company_name, tin)
VALUES (1, 0, -6, 'Kompanija 2', '3856296222');

INSERT INTO public.user_role (user_id, role_id) VALUES (-1, -3);
INSERT INTO public.user_role (user_id, role_id) VALUES (-2, -2);
INSERT INTO public.user_role (user_id, role_id) VALUES (-3, -1);
INSERT INTO public.user_role (user_id, role_id) VALUES (-4, -1);
INSERT INTO public.user_role (user_id, role_id) VALUES (-5, -1);
INSERT INTO public.user_role (user_id, role_id) VALUES (-6, -1);

INSERT INTO public.permissions(
    id, description, name)
VALUES (-1, 'Deskripcija', 'Brisanje');
INSERT INTO public.permissions(
    id, description, name)
VALUES (-2, 'Deskripcija123', 'Pisanje');
INSERT INTO public.permissions(
    id, description, name)
VALUES (-3, 'Deskripcija123', 'SveVezanoZaDozvole');
INSERT INTO public.permissions(
    id, description, name)
VALUES (-4, 'Deskripcija123', 'SveVezanoZaRole');
INSERT INTO public.permissions(
    id, description, name)
VALUES (-5, 'Deskripcija123', 'SveVezanoZaKlijente');

INSERT INTO public.permission_role(
    permission_id, role_id)
VALUES (-3, -3);
INSERT INTO public.permission_role(
    permission_id, role_id)
VALUES (-5, -3);
INSERT INTO public.permission_role(
    permission_id, role_id)
VALUES (-2, -3);
INSERT INTO public.permission_role(
    permission_id, role_id)
VALUES (-1, -3);
INSERT INTO public.permission_role(
    permission_id, role_id)
VALUES (-4, -3);
INSERT INTO public.permission_role(
    permission_id, role_id)
VALUES (-3, -2);

INSERT INTO public.requests(id, client_id, deadline, active_from, active_to, description)
VALUES (-1, -5, '25-05-2024', '26-05-2024', '29-05-2024', 'Opis zahtjeva...');

INSERT INTO public.requests(id, client_id, deadline, active_from, active_to, description)
VALUES (-2, -4, '20-05-2024', '21-05-2024', '30-05-2024', 'Opis zahtjeva...');

INSERT INTO public.employees(employee_id, has_changed_password)
VALUES (-2, false);

INSERT INTO public.users(enabled, id, address, city, country, password, phone, username, blocked, activation_pending, using2FA,secret2FA, is_blocked_user)
VALUES (true, -7, '0tZZGRhuRqzG+U0ALcJ4iA==', '7N8PVIl22Z6QZlIJsszG/g==', 'OOQoFfq3TuqqXYhgbddYyA==',  '$2a$10$DE9tdj0AIpgAN1.TtfVHDuinqJqwU57fxejSZyCmertgqhzXJi90K', '3SpMG1pKCCuH2TlXjUgUUg==', 'employee1@gmail.com', null, false, false, null, false);

INSERT INTO public.user_role (user_id, role_id) VALUES (-7, -2);

INSERT INTO public.employees(employee_id, has_changed_password)
VALUES (-7, false);

INSERT INTO public.commercials(id, client_id, employee_id, duration, moto, description)
VALUES (-1, -5, -2, 2, 'Live laugh love', 'Opis zahtjeva...');

INSERT INTO public.commercials(id, client_id, employee_id, duration, moto, description)
VALUES (-2, -4, -2, 4, 'Hakuna matata', 'Opis zahtjeva...');

INSERT INTO public.commercials(id, client_id, employee_id, duration, moto, description)
VALUES (-3, -4, -7, 4, 'Nazivvv', 'Opis zahtjeva...');
