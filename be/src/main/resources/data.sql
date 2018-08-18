INSERT INTO public.folder (gid, name, parent_folder_gid) VALUES ('f2b63895-3887-47e5-ad3c-d2234b9cab7b', 'Root', NULL);

INSERT INTO public.document (gid, content, title, parent_folder_gid) VALUES ('1c0d5926-b6ea-476a-a031-2c64482aed9a', 'This is a welcome to document', 'Welcome', 'f2b63895-3887-47e5-ad3c-d2234b9cab7b');

INSERT INTO public.user_profile (gid, email, login_token, password) VALUES ('55cf20bf-b8d8-4ab1-aef9-4c410a492190', 'wanilliabear+test@gmail.com', 'exXCJwXMmeWubHFQEmSF', '$2a$10$cDImfFfDa3YoHkcXqhroS.5uLp8.Ove4GkpIPnw2JDvozWU7pIrge'); --haslo jak email

INSERT INTO public.user_space (gid, is_current, name, password, root_folder_gid, user_profile_gid) VALUES ('539afa3d-3d8d-4494-b31a-d8601f773888', true, 'default', '$2a$10$cDImfFfDa3YoHkcXqhroS.5uLp8.Ove4GkpIPnw2JDvozWU7pIrge', 'f2b63895-3887-47e5-ad3c-d2234b9cab7b', '55cf20bf-b8d8-4ab1-aef9-4c410a492190');