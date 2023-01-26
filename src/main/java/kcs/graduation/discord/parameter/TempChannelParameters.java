package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TempChannelParameters {
    S_ID("SERVER_ID"),
    C_ID("CATEGORY_ID"),
    VC_ID("VOICE_CHANNEL_ID"),
    TC_ID("TEXT_CHANNEL_ID"),
    INFO_M_ID("INFO_MESSAGE_ID"),
    OWNER_U_ID("OWNER_USER_ID"),
    HIDE_BY("HIDE_BY"),
    LOCK_BY("LOCK_BY"),
    N_C_BY("NEW_CATEGORY_BY");


    private final String param;
}
