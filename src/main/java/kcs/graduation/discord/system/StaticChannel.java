package kcs.graduation.discord.system;

import kcs.graduation.discord.dao.StaticChannelDAO;
import kcs.graduation.discord.record.StaticChannelRecord;
import kcs.graduation.discord.system.core.Processing;
import kcs.graduation.discord.system.core.SystemMaster;
import kcs.graduation.discord.system.embed.Constant;
import kcs.graduation.discord.system.embed.create.CancelEmbed;
import kcs.graduation.discord.system.embed.create.CompleteEmbed;
import kcs.graduation.discord.system.permission.PermissionCreate;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.channel.ServerTextChannelUpdater;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

public class StaticChannel extends SystemMaster {
    DiscordApi api = getAPI();

    public StaticChannel() {
        // 固定通話設定追加
        api.addSlashCommandCreateListener(slashCommandCreateEvent -> {
            StaticChannelDAO dao = new StaticChannelDAO();
            CompleteEmbed completeEmbed = new CompleteEmbed();
            CancelEmbed cancelEmbed = new CancelEmbed();
            PermissionCreate permissionCreate = new PermissionCreate();
            Processing processing = new Processing();
            SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
            if ("set".equalsIgnoreCase(interaction.getCommandName()) && interaction.getOptionByIndex(0).isPresent()) {
                SlashCommandInteractionOption option = interaction.getOptionByIndex(0).get();
                if ("static_channel".equalsIgnoreCase(option.getName())
                        && option.getOptionChannelValueByName("channel").isPresent()
                        && option.getOptionChannelValueByName("channel").get().asServerVoiceChannel().isPresent()
                        && interaction.getServer().isPresent()
                        && interaction.getChannel().isPresent()
                        && interaction.getChannel().get().asServerTextChannel().isPresent()) {
                    Server server = interaction.getServer().get();
                    ServerVoiceChannel voiceChannel = option.getOptionChannelValueByName("channel").get().asServerVoiceChannel().get();
                    ServerTextChannel textChannel = interaction.getChannel().get().asServerTextChannel().get();
                    InteractionImmediateResponseBuilder responseBuilder = interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL);
                    if (voiceChannel.getServer().getId() == server.getId()
                            && processing.checkServerTextChannelAllowedPermission(textChannel, PermissionType.MANAGE_CHANNELS, true)) {
                        textChannel.createUpdater().addPermissionOverwrite(server.getEveryoneRole(), permissionCreate.createHidePermission(true).build()).update();
                        StaticChannelRecord record = new StaticChannelRecord();
                        record.setServer_id(server.getId());
                        record.setText_channel_id(textChannel.getId());
                        record.setVoice_channel_id(voiceChannel.getId());
                        dao.addStaticChannelLink(record);
                        responseBuilder.addEmbed(completeEmbed.createEmbed(voiceChannel, Constant.STATIC_SETTING_COMPLETE));
                    } else if (!processing.checkServerTextChannelAllowedPermission(textChannel, PermissionType.MANAGE_CHANNELS, true)) {
                        responseBuilder.addEmbed(cancelEmbed.createEmbed(voiceChannel, Constant.STATIC_SETTING_NO_PERMISSION_NO_MANAGE_CANCEL));
                    } else {
                        responseBuilder.addEmbed(cancelEmbed.createEmbed(voiceChannel, Constant.STATIC_SETTING_CANCEL));
                    }
                    responseBuilder.respond();
                }
            }
        });

        // 固定通話に入ったらリンク先のテキストチャンネルに権限を追加して見えるようにする
        api.addServerVoiceChannelMemberJoinListener(serverVoiceChannelMemberJoinEvent -> {
            StaticChannelDAO dao = new StaticChannelDAO();
            PermissionCreate permissionCreate = new PermissionCreate();
            Processing processing = new Processing();
            ServerVoiceChannel serverVoiceChannel = serverVoiceChannelMemberJoinEvent.getChannel();
            if (dao.checkStaticData(serverVoiceChannel.getId())) {
                User user = serverVoiceChannelMemberJoinEvent.getUser();
                Server server = serverVoiceChannelMemberJoinEvent.getServer();
                long textChannelId = dao.getStaticChannelData(serverVoiceChannel.getId());
                System.out.println(textChannelId);
                if (server.getTextChannelById(textChannelId).isPresent()
                        && serverVoiceChannel.canConnect(user)
                        && processing.checkServerTextChannelAllowedPermission(server.getTextChannelById(textChannelId).get(), PermissionType.MANAGE_CHANNELS, true)) {
                    System.out.println(1);
                    ServerTextChannelUpdater textChannelUpdater = server.getTextChannelById(textChannelId).get().createUpdater();
                    textChannelUpdater.addPermissionOverwrite(user, permissionCreate.createHidePermission(false).build());
                    textChannelUpdater.update();
                }
            }
        });

        // 固定通話を抜けたらリンク先のテキストチャンネルの権限編集して見えないようにする
        api.addServerVoiceChannelMemberLeaveListener(serverVoiceChannelMemberLeaveEvent -> {
            StaticChannelDAO dao = new StaticChannelDAO();
            Processing processing = new Processing();
            ServerVoiceChannel serverVoiceChannel = serverVoiceChannelMemberLeaveEvent.getChannel();
            if (dao.checkStaticData(serverVoiceChannel.getId())) {
                User user = serverVoiceChannelMemberLeaveEvent.getUser();
                Server server = serverVoiceChannelMemberLeaveEvent.getServer();
                long textChannelId = dao.getStaticChannelData(serverVoiceChannel.getId());
                if (server.getTextChannelById(textChannelId).isPresent()
                        && serverVoiceChannel.canConnect(user)
                        && processing.checkServerTextChannelAllowedPermission(server.getTextChannelById(textChannelId).get(), PermissionType.MANAGE_CHANNELS, true)) {
                    if (serverVoiceChannel.getConnectedUserIds().size() != 0) {
                        ServerTextChannelUpdater textChannelUpdater = server.getTextChannelById(textChannelId).get().createUpdater();
                        textChannelUpdater.addPermissionOverwrite(user, new PermissionsBuilder().setUnset(PermissionType.VIEW_CHANNEL).build());
                        textChannelUpdater.update();
                    } else {
                        ServerTextChannel textChannel = server.getTextChannelById(textChannelId).get();
                        ServerTextChannelBuilder serverTextChannelBuilder = new ServerTextChannelBuilder(server);
                        serverTextChannelBuilder = processing.CloneServerTextChannel(textChannel, serverTextChannelBuilder);
                        long serverTextChannelId = serverTextChannelBuilder.create().join().getId();
                        textChannel.delete().join();
                        dao.updateStaticTextChannel(textChannelId, serverTextChannelId);
                    }
                }
            }
        });
    }
}
