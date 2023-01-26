package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventChannelParameters {
    S_ID("SERVER_ID"),
    ID("ID"),
    VC_ID("VOICE_CHANNEL_ID"),
    TC_ID("TEXT_CHANNEL_ID"),
    ;

    private final String param;
}
