# --- !Ups

create table "User" ("ID" UUID NOT NULL PRIMARY KEY,"FIRSTNAME" VARCHAR NOT NULL,"LASTNAME" VARCHAR NOT NULL,"AGE" INTEGER NOT NULL,"USER_NAME" VARCHAR NOT NULL,"PASSWORD" VARCHAR NOT NULL,"CREATED_AT" timestamp NOT NULL);



# --- !Downs

drop table "User";
