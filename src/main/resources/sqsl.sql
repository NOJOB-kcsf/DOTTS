CREATE DATABASE KCS_DOTTS;

CREATE USER 'NO_JOB'@'localhost' IDENTIFIED BY 'eWMsc9Y58AwY';

GRANT ALL PRIVILEGES ON KCS_DOTTS.* TO 'NO_JOB'@'localhost';

USE KCS_DOTTS;

create table BOT_DATA
(
    BOT_NAME    text not null,
    BOT_TOKEN   text not null,
    DEEPL_TOKEN text not null,
    DEEPL_PERSONAL_TRANSLATE_MAX_STRING_LENGTH text not null
);

CREATE TABLE DISCORD_USER_SETTING
(
    USER_ID     bigint not null,
    DEFAULT_TRANSLATE_COUNT bigint default 0 not null,
    CUSTOM_TRANSLATE_TOKEN  text null,
    CUSTOM_TRANSLATE_COUNT bigint default 0 not null
);

create table DELETE_MESSAGE
(
    SERVER_ID       bigint null,
    MESSAGE_ID      bigint null,
    MESSAGE_LINK    text   null,
    DELETE_TIME     bigint null,
    TEXT_CHANNEL_ID bigint null
);

create table DELETE_TIME
(
    SERVER_ID       bigint null,
    TEXT_CHANNEL_ID bigint null,
    TIME            bigint null,
    UNIT            char   null
);

create table EVENT_CHANNEL
(
    SERVER_ID        bigint not null,
    ID               bigint not null,
    VOICE_CHANNEL_ID bigint null,
    TEXT_CHANNEL_ID  bigint null
);

create table EVENT_DATA
(
    SERVER_ID             bigint               null,
    NEXT_START_TIME       datetime             null,
    DELAY_TIME            bigint               null,
    DELAY_UNIT            char                 null,
    END_TIME              bigint               null,
    END_UNIT              char                 null,
    EVENT_NAME            text                 null,
    CHANNEL_NAME          text                 null,
    EVENT_DESCRIPTION     text                 null,
    EVENT_MENTION_ROLE_ID bigint               null,
    CHANNEL_SIZE          int                  null,
    ID                    int                  null,
    MENTION_MESSAGE_LINK  text                 null,
    EVENT_EXECUTE         tinyint(1) default 0 null
);

create table MENTION_MESSAGE
(
    SERVER_ID        bigint null,
    MESSAGE_ID       bigint null,
    TEXT_CHANNEL_ID  bigint null,
    VOICE_CHANNEL_ID bigint null,
    MESSAGE_LINK     text   null
);

create table NAME_PRESET
(
    SERVER_ID bigint null,
    NAME      text   null
);

create table REACT_EMOJI_AND_ROLE
(
    SERVER_ID  bigint null,
    MESSAGE_ID bigint null,
    EMOJI      text   null,
    ROLE_ID    bigint null
);

create table REACT_MESSAGE
(
    SERVER_ID       bigint null,
    TEXT_CHANNEL_ID bigint null,
    MESSAGE_ID      bigint null,
    MESSAGE_LINK    text   null
);

create table SERVER_SETTING
(
    SERVER_ID                 bigint                                 not null,
    MENTION_CHANNEL_ID        bigint                                 null,
    FIRST_CHANNEL_ID          bigint                                 null,
    STATIC_CATEGORY_ID        bigint                                 null,
    EVENT_CATEGORY_ID         bigint                                 null,
    CREATED_UNDER_CATEGORY_ID bigint                                 null,
    TEMP_VOICE_CATEGORY_ID    bigint                                 null,
    TEMP_TEXT_CATEGORY_ID     bigint                                 null,
    TEMP_CREATED_BY           tinyint(1)    default 1                null,
    TEMP_TEXT_CREATED_BY      tinyint(1)    default 1                null,
    NEW_UNDER_CATEGORY_BY     tinyint(1)    default 0                null,
    DEFAULT_SIZE              int           default 0                null,
    STEREO_TYPED              varchar(3000) default 'mention'        null,
    DEFAULT_NAME              varchar(100)  default '&name&-channel' null
);

create table STATIC_CHANNEL
(
    SERVER_ID        bigint null,
    VOICE_CHANNEL_ID bigint null,
    TEXT_CHANNEL_ID  bigint null
);

create table TEMP_CHANNEL
(
    SERVER_ID        bigint               null,
    CATEGORY_ID      bigint               null,
    VOICE_CHANNEL_ID bigint               null,
    TEXT_CHANNEL_ID  bigint               null,
    INFO_MESSAGE_ID  bigint               null,
    OWNER_USER_ID    bigint               null,
    HIDE_BY          tinyint(1) default 0 null,
    LOCK_BY          tinyint(1) default 0 null,
    NEW_CATEGORY_BY  tinyint(1) default 0 null
);

