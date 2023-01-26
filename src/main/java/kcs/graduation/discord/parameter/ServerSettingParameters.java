package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServerSettingParameters {
    S_ID("SERVER_ID"),
    M_C("MENTION_CHANNEL_ID"),
    FC_ID("FIRST_CHANNEL_ID"),
    SC_ID("STATIC_CATEGORY_ID"),
    EC_ID("EVENT_CATEGORY_ID"),
    UNDER_C_ID("CREATED_UNDER_CATEGORY_ID"),
    T_VC_ID("TEMP_VOICE_CATEGORY_ID"),
    T_TC_ID("TEMP_TEXT_CATEGORY_ID"),
    T_BY("TEMP_CREATED_BY"),
    T_TC_BY("TEMP_TEXT_CREATED_BY"),
    UNDER_C_BY("NEW_UNDER_CATEGORY_BY"),
    D_SIZE("DEFAULT_SIZE"),
    S_TYPE("STEREO_TYPED"),
    D_NAME("DEFAULT_NAME");

    private final String param;
}
