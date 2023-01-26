package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReactEmojiAndRoleParameters {
    S_ID("SERVER_ID"),
    M_ID("MESSAGE_ID"),
    EMOJI("EMOJI"),
    R_ID("ROLE_ID");

    private final String param;
}
