package kcs.graduation.discord.record;

import lombok.Data;
import org.javacord.api.entity.user.User;

@Data
public class TranslateRecord {
    boolean complete;
    User user;
    String text;
    String result;
    String language;
}
