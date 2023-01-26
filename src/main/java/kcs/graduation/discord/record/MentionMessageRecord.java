package kcs.graduation.discord.record;

import lombok.Data;

@Data
public class MentionMessageRecord {
    private long server_id;
    private long message_id;
    private String message_link;
    private long text_channel_id;
    private long voice_channel_id;
}
