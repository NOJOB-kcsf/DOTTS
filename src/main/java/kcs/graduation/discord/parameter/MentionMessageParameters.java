package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MentionMessageParameters {
    S_ID("SERVER_ID"),
    M_ID("MESSAGE_ID"),
    M_LINK("MESSAGE_LINK"),
    TC_ID("TEXT_CHANNEL_ID"),
    VC_ID("VOICE_CHANNEL_ID");

    private final String param;
}
