package kcs.graduation.discord.system;

import kcs.graduation.discord.dao.EventChannelDAO;
import kcs.graduation.discord.record.EventChannelRecord;
import kcs.graduation.discord.record.EventDataRecord;
import kcs.graduation.discord.system.core.Processing;
import kcs.graduation.discord.system.core.SystemMaster;
import kcs.graduation.discord.system.embed.create.CancelEmbed;
import kcs.graduation.discord.system.embed.create.CompleteEmbed;
import kcs.graduation.discord.system.permission.PermissionCreate;
import kcs.graduation.processing.time.CycleStart;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.*;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import static kcs.graduation.discord.system.embed.Constant.*;

public class EventChannel extends SystemMaster {
    DiscordApi api = getAPI();

    public EventChannel() {

        CycleStart eventOneHourAgo = new CycleStart();
        eventOneHourAgo.everySecondExecute(
                new TimerTask() {
                    @Override
                    public void run() {
                        EventChannelDAO dao = new EventChannelDAO();
                        Date date = new Date();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.HOUR, 1);
                        ArrayList<EventDataRecord> eventDataRecords = dao.getEventOneHourAgoData(calendar.getTime());
                        for (EventDataRecord record : eventDataRecords) {
                            if (record.getMentionChannelId() != 0
                                    && api.getTextChannelById(record.getMentionChannelId()).isPresent()
                                    && (record.getRoleId() == -1 || api.getRoleById(record.getRoleId()).isPresent())) {
                                TextChannel textChannel = api.getTextChannelById(record.getMentionChannelId()).get();
                                CompleteEmbed completeEmbed = new CompleteEmbed();
                                String messageLink = textChannel.sendMessage(completeEmbed.createEmbedOneHourAgo(record)).join().getLink().toString();
                                record.setMentionMessageLink(messageLink);
                            }
                        }
                        dao.setEventMentionMessage(eventDataRecords);
                    }
                }
        );

        CycleStart eventExecute = new CycleStart();
        eventExecute.everySecondExecute(
                new TimerTask() {
                    @Override
                    public void run() {
                        EventChannelDAO dao = new EventChannelDAO();
                        ArrayList<EventDataRecord> eventDataRecords = dao.getEventExecute(new Date());
                        eventDataRecords.forEach(eventDataRecord -> {
                            if (api.getChannelCategoryById(eventDataRecord.getEventCategoryId()).isPresent()
                                    && api.getServerById(eventDataRecord.getServerId()).isPresent()
                                    && api.getTextChannelById(eventDataRecord.getMentionChannelId()).isPresent()) {
                                ChannelCategory category = api.getChannelCategoryById(eventDataRecord.getEventCategoryId()).get();
                                Server server = api.getServerById(eventDataRecord.getServerId()).get();
                                PermissionCreate permissionCreate = new PermissionCreate();
                                ServerTextChannelBuilder textChannelBuilder = new ServerTextChannelBuilder(server).setCategory(category).setName(eventDataRecord.getChannelName()).addPermissionOverwrite(server.getEveryoneRole(), permissionCreate.createHidePermission(true).build());
                                if (!eventDataRecord.getEventDescription().isEmpty())
                                    textChannelBuilder.setTopic(eventDataRecord.getEventDescription());
                                long textChannelId = textChannelBuilder.create().join().getId();
                                long voiceChannelId = new ServerVoiceChannelBuilder(server).setCategory(category).setName(eventDataRecord.getChannelName()).setUserlimit((int) eventDataRecord.getDefaultSize()).create().join().getId();
                                TextChannel textChannel = api.getTextChannelById(eventDataRecord.getMentionChannelId()).get();
                                try {
                                    api.getMessageByLink(eventDataRecord.getMentionMessageLink()).orElseThrow().join().delete();
                                } catch (IllegalArgumentException ignored) {
                                }
                                CompleteEmbed completeEmbed = new CompleteEmbed();
                                eventDataRecord.setVoiceChannelId(voiceChannelId);
                                String messageLink = new MessageBuilder().setEmbed(completeEmbed.createEmbedExecute(eventDataRecord)).send(textChannel).join().getLink().toString();
                                eventDataRecord.setMentionMessageLink(messageLink);
                                EventChannelRecord eventChannelRecord = new EventChannelRecord();
                                eventChannelRecord.setText_channel_id(textChannelId);
                                eventChannelRecord.setVoice_channel_id(voiceChannelId);
                                eventChannelRecord.setServer_id(server.getId());
                                eventChannelRecord.setId(eventDataRecord.getId());
                                dao.setEventExecute(eventDataRecord);
                                dao.setEventMentionMessage(eventDataRecord);
                                dao.addEventChannel(eventChannelRecord);
                            }
                        });
                        dao.setEventMentionMessage(eventDataRecords);
                    }
                });

        CycleStart eventBeforeEnd = new CycleStart();
        eventBeforeEnd.everySecondExecute(
                new TimerTask() {
                    @Override
                    public void run() {
                        EventChannelDAO dao = new EventChannelDAO();
                        Date date = new Date();
                        Calendar now = Calendar.getInstance();
                        now.setTime(date);
                        now.add(Calendar.MINUTE, 15);
                        ArrayList<EventDataRecord> eventDataRecords = dao.getEventBeforeEnd();
                        for (EventDataRecord eventDataRecord : eventDataRecords) {
                            Calendar event = Calendar.getInstance();
                            event.setTime(eventDataRecord.getNextStartTime());
                            int unit = switch (eventDataRecord.getEndUnit()) {
                                case "M" -> Calendar.MINUTE;
                                case "H" -> Calendar.HOUR;
                                case "D" -> Calendar.DAY_OF_MONTH;
                                case "Y" -> Calendar.YEAR;
                                case "S" -> Calendar.SECOND;
                                default -> -1;
                            };
                            if (unit != -1) {
                                event.add(unit, (int) eventDataRecord.getEndTime());
                                if (now.getTime().toInstant().getEpochSecond() == event.getTime().toInstant().getEpochSecond() && api.getTextChannelById(eventDataRecord.getTextChannelId()).isPresent()) {
                                    TextChannel textChannel = api.getTextChannelById(eventDataRecord.getTextChannelId()).get();
                                    new MessageBuilder().setEmbed(new CompleteEmbed().createEmbedBeforeEnd(now.getTime())).send(textChannel).join();
                                }
                            }
                        }
                    }
                });

        CycleStart eventEnd = new CycleStart();
        eventEnd.everySecondExecute(
                new TimerTask() {
                    @Override
                    public void run() {
                        Date nowTime = new Date();
                        EventChannelDAO dao = new EventChannelDAO();
                        ArrayList<EventDataRecord> eventDataRecords = dao.getEventEnd();
                        eventDataRecords.forEach(eventDataRecord -> {
                            Calendar nextEventStart = Calendar.getInstance();
                            Calendar endTime = Calendar.getInstance();
                            endTime.setTime(eventDataRecord.getNextStartTime());
                            nextEventStart.setTime(eventDataRecord.getNextStartTime());
                            int unit = switch (eventDataRecord.getEndUnit()) {
                                case "M" -> Calendar.MINUTE;
                                case "H" -> Calendar.HOUR;
                                case "D" -> Calendar.DAY_OF_MONTH;
                                case "Y" -> Calendar.YEAR;
                                case "S" -> Calendar.SECOND;
                                default -> -1;
                            };
                            int dUnit = switch (eventDataRecord.getDelayUnit()) {
                                case "M" -> Calendar.MINUTE;
                                case "H" -> Calendar.HOUR;
                                case "D" -> Calendar.DAY_OF_MONTH;
                                case "Y" -> Calendar.YEAR;
                                case "S" -> Calendar.SECOND;
                                default -> -1;
                            };
                            if (unit != -1 && dUnit != -1) {
                                endTime.add(unit, (int) eventDataRecord.getEndTime());
                                nextEventStart.add(dUnit, (int) eventDataRecord.getDelayTime());
                                if (nowTime.toInstant().getEpochSecond() == endTime.getTime().toInstant().getEpochSecond()
                                        && api.getServerById(eventDataRecord.getServerId()).isPresent()) {
                                    eventDataRecord.setNextStartTime(nextEventStart.getTime());
                                    Server server = api.getServerById(eventDataRecord.getServerId()).get();
                                    if (api.getTextChannelById(eventDataRecord.getMentionChannelId()).isPresent()
                                            && api.getMessageByLink(eventDataRecord.getMentionMessageLink()).isPresent()) {
                                        api.getMessageByLink(eventDataRecord.getMentionMessageLink()).orElseThrow().join().delete();
                                    }
                                    if (server.getTextChannelById(eventDataRecord.getTextChannelId()).isPresent())
                                        server.getTextChannelById(eventDataRecord.getTextChannelId()).get().delete();
                                    if (server.getVoiceChannelById(eventDataRecord.getVoiceChannelId()).isPresent())
                                        server.getVoiceChannelById(eventDataRecord.getVoiceChannelId()).get().delete();
                                    dao.setEventEnd(eventDataRecord);
                                    dao.deleteEventChannel(eventDataRecord);
                                    dao.updateEventNextStartTime(eventDataRecord);
                                }
                            }
                        });
                    }
                }
        );

        api.addSlashCommandCreateListener(slashCommandCreateEvent -> {
            EventChannelDAO dao = new EventChannelDAO();
            CompleteEmbed completeEmbed = new CompleteEmbed();
            SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
            String cmd = interaction.getCommandName();
            if ("set".equalsIgnoreCase(cmd)
                    && interaction.getOptionByIndex(0).isPresent()
                    && interaction.getServer().isPresent()) {
                SlashCommandInteractionOption slashCommandInteractionOption = interaction.getOptionByIndex(0).get();
                if ("event_category".equalsIgnoreCase(slashCommandInteractionOption.getName())
                        && slashCommandInteractionOption.getOptionChannelValueByName("category").isPresent()
                        && slashCommandInteractionOption.getOptionChannelValueByName("category").get().asChannelCategory().isPresent()) {
                    ChannelCategory channelCategory = slashCommandInteractionOption.getOptionChannelValueByName("category").get().asChannelCategory().get();
                    long categoryId = channelCategory.getId();
                    dao.setEventChannelCategory(interaction.getServer().get().getId(), categoryId);
                    interaction.createImmediateResponder()
                            .setFlags(MessageFlag.EPHEMERAL)
                            .addEmbed(completeEmbed.createEmbed(channelCategory, EVENT_CATEGORY_SET_COMPLETE))
                            .respond();
                }
            }
        });

        api.addSlashCommandCreateListener(slashCommandCreateEvent -> {
            EventChannelDAO dao = new EventChannelDAO();
            CompleteEmbed completeEmbed = new CompleteEmbed();
            SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
            String cmd = interaction.getCommandName();
            if ("add".equalsIgnoreCase(cmd)
                    && interaction.getOptionByIndex(0).isPresent()
                    && interaction.getServer().isPresent()) {
                SlashCommandInteractionOption option = interaction.getOptionByIndex(0).get();
                InteractionImmediateResponseBuilder responseBuilder = interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL);
                if ("event_data".equalsIgnoreCase(option.getName())
                        && option.getOptionStringValueByName("event_name").isPresent()
                        && option.getOptionStringValueByName("channel_name").isPresent()
                        && option.getOptionStringValueByName("next_start_time").isPresent()
                        && option.getOptionLongValueByName("delay_time").isPresent()
                        && option.getOptionStringValueByName("delay_unit").isPresent()
                        && option.getOptionLongValueByName("end_time").isPresent()
                        && option.getOptionStringValueByName("end_unit").isPresent()) {
                    EventDataRecord eventDataRecord = new EventDataRecord();
                    eventDataRecord.setServerId(interaction.getServer().get().getId());
                    eventDataRecord.setEventName(option.getOptionStringValueByName("event_name").get());
                    eventDataRecord.setChannelName(option.getOptionStringValueByName("channel_name").get());
                    eventDataRecord.setDelayTime(option.getOptionLongValueByName("delay_time").get());
                    eventDataRecord.setDelayUnit(option.getOptionStringValueByName("delay_unit").get());
                    eventDataRecord.setEndTime(option.getOptionLongValueByName("end_time").get());
                    eventDataRecord.setEndUnit(option.getOptionStringValueByName("end_unit").get());
                    Processing processing = new Processing();
                    boolean checkTime = processing.checkTimeAndUnit(option.getOptionLongValueByName("delay_time").get(),
                            option.getOptionStringValueByName("delay_unit").get(),
                            option.getOptionLongValueByName("end_time").get(),
                            option.getOptionStringValueByName("end_unit").get());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        java.util.Date nowDate = new java.util.Date();
                        java.util.Date inputDate = sdf.parse(option.getOptionStringValueByName("next_start_time").get());
                        int dateComparison = nowDate.compareTo(inputDate);
                        boolean checkCategory = dao.checkEventCategoryId(eventDataRecord.getServerId());
                        boolean checkNameCovering = dao.checkEventNameIsCovering(option.getOptionStringValueByName("event_name").get());
                        if (dateComparison < 0 && !checkCategory && !checkNameCovering && checkTime) {
                            eventDataRecord.setNextStartTime(inputDate);
                            if (option.getOptionStringValueByName("event_description").isPresent())
                                eventDataRecord.setEventDescription(option.getOptionStringValueByName("event_description").get());
                            if (option.getOptionRoleValueByName("mention").isPresent())
                                eventDataRecord.setRoleId(option.getOptionRoleValueByName("mention").get().getId());
                            if (option.getOptionLongValueByName("channel_default_size").isPresent())
                                eventDataRecord.setDefaultSize(option.getOptionLongValueByName("channel_default_size").get());
                            eventDataRecord.setMentionMessageLink("");
                            dao.setEventData(eventDataRecord);
                            responseBuilder.addEmbed(completeEmbed.createEmbed(eventDataRecord, EVENT_DATA_SET_COMPLETE));
                        } else if (checkCategory) {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            responseBuilder.addEmbed(cancelEmbed.createEmbed(EVENT_DATA_NOT_CATEGORY_ERROR));
                        } else if (checkNameCovering) {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            responseBuilder.addEmbed(cancelEmbed.createEmbed(EVENT_DATA_COVERING_NAME_ERROR));
                        } else if (!checkTime) {
                            CancelEmbed cancelEmbed = new CancelEmbed();
                            responseBuilder.addEmbed(cancelEmbed.createEmbed(EVENT_DATA_DELAY_OVER_END_TIME_ERROR));
                        } else {
                            responseBuilder.addEmbed(new CancelEmbed().createEmbed(EVENT_SET_DATE_EXCESS_ERROR));
                        }
                    } catch (ParseException e) {
                        CancelEmbed cancelEmbed = new CancelEmbed();
                        responseBuilder.addEmbed(cancelEmbed.createEmbed(EVENT_DATA_SET_CANCEL_NOT_DATE_FORMAT));
                    }
                    responseBuilder.respond();
                }
            }
        });

// 通話の表示非表示が未実装

        api.addServerVoiceChannelMemberJoinListener(serverVoiceChannelMemberJoinEvent -> {
            EventChannelDAO dao = new EventChannelDAO();
            PermissionCreate permissionCreate = new PermissionCreate();
            Processing processing = new Processing();
            ServerVoiceChannel serverVoiceChannel = serverVoiceChannelMemberJoinEvent.getChannel();
            if (dao.checkEventData(serverVoiceChannel.getId())) {
                User user = serverVoiceChannelMemberJoinEvent.getUser();
                Server server = serverVoiceChannelMemberJoinEvent.getServer();
                long textChannelId = dao.getEventTextChannelData(serverVoiceChannel.getId());
                if (server.getTextChannelById(textChannelId).isPresent()
                        && serverVoiceChannel.canConnect(user)
                        && processing.checkServerTextChannelAllowedPermission(server.getTextChannelById(textChannelId).get(), PermissionType.MANAGE_CHANNELS, true)) {
                    ServerTextChannelUpdater textChannelUpdater = server.getTextChannelById(textChannelId).get().createUpdater();
                    textChannelUpdater.addPermissionOverwrite(user, permissionCreate.createHidePermission(false).build());
                    textChannelUpdater.update();
                }
            }
        });

        // 固定通話を抜けたらリンク先のテキストチャンネルの権限編集して見えないようにする
        api.addServerVoiceChannelMemberLeaveListener(serverVoiceChannelMemberLeaveEvent -> {
            EventChannelDAO dao = new EventChannelDAO();
            Processing processing = new Processing();
            ServerVoiceChannel serverVoiceChannel = serverVoiceChannelMemberLeaveEvent.getChannel();
            if (dao.checkEventData(serverVoiceChannel.getId())) {
                User user = serverVoiceChannelMemberLeaveEvent.getUser();
                Server server = serverVoiceChannelMemberLeaveEvent.getServer();
                long textChannelId = dao.getEventTextChannelData(serverVoiceChannel.getId());
                if (server.getTextChannelById(textChannelId).isPresent()
                        && serverVoiceChannel.canConnect(user)
                        && processing.checkServerTextChannelAllowedPermission(server.getTextChannelById(textChannelId).get(), PermissionType.MANAGE_CHANNELS, true)) {
                    ServerTextChannelUpdater textChannelUpdater = server.getTextChannelById(textChannelId).get().createUpdater();
                    textChannelUpdater.addPermissionOverwrite(user, new PermissionsBuilder().setUnset(PermissionType.VIEW_CHANNEL).build());
                    textChannelUpdater.update();
                }
            }
        });
    }
}
