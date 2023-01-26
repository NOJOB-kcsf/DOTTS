package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeleteTimeParameters {
    S_ID("SERVER_ID"),
    TC_ID("TEXT_CHANNEL_ID"),
    TIME("TIME"),
    UNIT("UNIT");

    private final String param;
}
