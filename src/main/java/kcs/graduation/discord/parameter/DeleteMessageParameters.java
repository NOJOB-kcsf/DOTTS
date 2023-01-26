package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeleteMessageParameters {
    S_ID("SERVER_ID"),
    M_ID("MESSAGE_ID"),
    M_LINK("MESSAGE_LINK"),
    D_TIME("DELETE_TIME"),
    TC_ID("TEXT_CHANNEL_ID");

    private final String param;
}
