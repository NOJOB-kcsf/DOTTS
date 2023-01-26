package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventDataParameters {
    S_ID("SERVER_ID"),
    N_ST("NEXT_START_TIME"),
    D_T("DELAY_TIME"),
    D_U("DELAY_UNIT"),
    E_T("END_TIME"),
    E_U("END_UNIT"),
    E_N("EVENT_NAME"),
    C_N("CHANNEL_NAME"),
    E_D("EVENT_DESCRIPTION"),
    EM_RI("EVENT_MENTION_ROLE_ID"),
    C_S("CHANNEL_SIZE"),
    ID("ID"),
    M_M_L("MENTION_MESSAGE_LINK"),
    E_E("EVENT_EXECUTE");

    private final String param;
}
