package kcs.graduation.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NamePresetParameters {
    S_ID("SERVER_ID"),
    NAME("NAME");

    private final String param;
}
