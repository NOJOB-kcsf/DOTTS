package kcs.graduation.discord.record;

import lombok.Data;

import java.util.Date;

@Data
public class DeleteMessageRecord {
    private long server_id;
    private long message_id;
    private String message_link;
    private Date delete_time;
    private long text_channel_id;
}
