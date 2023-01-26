package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StaticChannelParameters {
    S_ID("SERVER_ID"),
    VC_ID("VOICE_CHANNEL_ID"),
    TC_ID("TEXT_CHANNEL_ID"),
    INFO_M_ID("INFO_MESSAGE_ID");

    private final String param;
}
