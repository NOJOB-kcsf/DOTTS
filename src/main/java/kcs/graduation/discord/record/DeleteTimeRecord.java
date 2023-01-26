package kcs.graduation.discord.record;

import lombok.Data;

@Data
public class DeleteTimeRecord {
    private long server_id;
    private long text_channel_id;
    private long time;
    private String unit;
}
