package kcs.graduation.discord.record;

import lombok.Data;

@Data
public class StaticChannelRecord {
    private long server_id;
    private long voice_channel_id;
    private long text_channel_id;
    private long info_message_id;
}
