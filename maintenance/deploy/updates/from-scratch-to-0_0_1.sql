create table document (gid varchar(255) not null, content bytea, title varchar(255) not null, parent_folder_gid varchar(255) not null, primary key (gid))
create table file (gid varchar(255) not null, content oid, name varchar(255), parent_folder_gid varchar(255) not null, primary key (gid))
create table folder (gid varchar(255) not null, name varchar(255), parent_folder_gid varchar(255), primary key (gid))
create table tag (gid varchar(255) not null, name varchar(255), document_gid varchar(255) not null, primary key (gid))
create table user_profile (gid varchar(255) not null, email varchar(255) not null, login_token varchar(255), password varchar(255) not null, primary key (gid))
create table user_space (gid varchar(255) not null, is_current boolean not null, name varchar(255), password varchar(1024), root_folder_gid varchar(255), user_profile_gid varchar(255) not null, primary key (gid))
alter table document add constraint FKfspqyb750v7pv7eggk73uwjf2 foreign key (parent_folder_gid) references folder
alter table file add constraint FKppwu4ujptv5uiq9kew2urhrsm foreign key (parent_folder_gid) references folder
alter table folder add constraint FKhcwko0btnf6ahar4kiy2o63gq foreign key (parent_folder_gid) references folder
alter table tag add constraint FKihof0cga5piy3jpru5nd8n962 foreign key (document_gid) references document
alter table user_space add constraint FKd8t8tbhgfvd5860sgb2wwvan5 foreign key (root_folder_gid) references folder
alter table user_space add constraint FKoqpd2bbbvolbv86i4k95wcc4f foreign key (user_profile_gid) references user_profile
