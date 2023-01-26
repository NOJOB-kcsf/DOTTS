package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DAOParameters {
    CONNECT_STRING("jdbc:mysql://localhost:3306/KCS_DOTTS"),
    USER_ID("NO_JOB"),
    PASS_WORD("eWMsc9Y58AwY"),
    TABLE_BOT_DATA("BOT_DATA"),
    TABLE_SERVER_SETTING("SERVER_SETTING"),
    TABLE_EVENT_CHANNEL("EVENT_CHANNEL"),
    TABLE_EVENT_SETTING("EVENT_DATA"),
    TABLE_STATIC_CHANNEL("STATIC_CHANNEL"),
    TABLE_TEMP_CHANNEL("TEMP_CHANNEL"),
    TABLE_MENTION_MESSAGE("MENTION_MESSAGE"),
    TABLE_NAME_PRESET("NAME_PRESET"),
    TABLE_DELETE_TIME("DELETE_TIME"),
    TABLE_DELETE_MESSAGE("DELETE_MESSAGE"),
    TABLE_REACT_MESSAGE("REACT_MESSAGE"),
    TABLE_REACT_EMOJI_AND_ROLE("REACT_EMOJI_AND_ROLE");

    private final String param;
}
