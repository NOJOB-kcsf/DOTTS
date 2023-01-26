package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReactMessageParameters {
    S_ID("SERVER_ID"),
    TC_ID("TEXT_CHANNEL_ID"),
    M_ID("MESSAGE_ID"),
    M_LINK("MESSAGE_LINK");

    private final String param;
}
