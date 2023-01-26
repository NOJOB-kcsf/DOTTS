package kcs.graduation.discord.system;

import kcs.graduation.discord.dao.TempChannelDAO;
import kcs.graduation.discord.record.MessageRecord;
import kcs.graduation.discord.record.NamePresetRecord;
import kcs.graduation.discord.record.ServerSettingRecord;
import kcs.graduation.discord.record.TempChannelRecord;
import kcs.graduation.discord.system.core.Processing;
import kcs.graduation.discord.system.core.SystemMaster;
import kcs.graduation.discord.system.embed.Constant;
import kcs.graduation.discord.system.embed.create.CancelEmbed;
import kcs.graduation.discord.system.embed.create.CompleteEmbed;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.*;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;

import java.util.Objects;
import java.util.concurrent.CompletionException;

public class TempChannel extends SystemMaster {
    DiscordApi api = getAPI();

    public TempChannel() {

        api.addServerVoiceChannelMemberJoinListener(event -> {
            TempChannelDAO dao = new TempChannelDAO();
            Server server = event.getServer();
            User joinUser = event.getUser();
            User bot = api.getYourself();
            if ( (event.getUser().getId() == 1065093895437963384L || !event.getUser().isBot()) && dao.firstCheckFit(event.getChannel().getId())) {
                Processing processing = new Processing();
                ServerSettingRecord settingRecord = dao.getTempSetting(server.getId());
                boolean createChannel = false;
                boolean newCategory = false;
                for (Role role : bot.getRoles(server)) {
                    if (role.getAllowedPermissions().contains(PermissionType.MANAGE_CHANNELS)) {
                        createChannel = true;
                        break;
                    }
                }
                ChannelCategory VCCategory = null;
                ChannelCategory TCCategory = null;
                if (settingRecord.isTemp_created_by() && api.getChannelCategoryById(settingRecord.getTemp_voice_category_id()).isPresent()) {
                    VCCategory = api.getChannelCategoryById(settingRecord.getTemp_voice_category_id()).get();
                    if (settingRecord.isTemp_text_created_by() && api.getChannelCategoryById(settingRecord.getTemp_text_category_id()).isPresent()) {
                        TCCategory = api.getChannelCategoryById(settingRecord.getTemp_text_category_id()).get();
                    }
                } else if (settingRecord.isTemp_created_by() && settingRecord.isNew_under_category_by()
                        && api.getChannelCategoryById(settingRecord.getCreated_under_category_id()).isPresent()
                        && createChannel) {
                    newCategory = true;
                    int position = api.getChannelCategoryById(settingRecord.getCreated_under_category_id()).get().getRawPosition();
                    VCCategory = new ChannelCategoryBuilder(server).setName(processing.channelNameRePress(settingRecord.getDefault_name() + "-Category", joinUser.getName())).setRawPosition(position).addPermissionOverwrite(server.getEveryoneRole(), new PermissionsBuilder().setDenied(PermissionType.VIEW_CHANNEL).build()).create().join();
                    if (settingRecord.isTemp_text_created_by()) TCCategory = VCCategory;
                }
                ServerTextChannel tempTextChannel = null;
                ServerVoiceChannel tempVoiceChannel = null;
                try {
                    if (VCCategory != null) {
                        tempVoiceChannel = new ServerVoiceChannelBuilder(server).setCategory(VCCategory).setUserlimit(settingRecord.getDefault_size()).setName(processing.channelNameRePress(settingRecord.getDefault_name(), joinUser.getName())).addPermissionOverwrite(joinUser, processing.getChannelManagePermission()).create().join();
                        joinUser.move(tempVoiceChannel).join();
                        long infoMessageId = 0;
                        if (TCCategory != null) {
                            tempTextChannel = new ServerTextChannelBuilder(server).setCategory(TCCategory).setName(processing.channelNameRePress(settingRecord.getDefault_name(), joinUser.getName())).addPermissionOverwrite(server.getEveryoneRole(), new PermissionsBuilder().setDenied(PermissionType.VIEW_CHANNEL).build()).addPermissionOverwrite(joinUser, processing.getChannelManagePermission()).create().join();
                            infoMessageId = new MessageBuilder().setEmbed(processing.rePressInfoMessage(joinUser, false, false)).addComponents(
                                    ActionRow.of(Button.success("name", "通話名前変更"),
                                            Button.success("size", "通話人数変更"),
                                            Button.success("send-recruiting", "募集送信"),
                                            Button.success("claim", "通話権限獲得"),
                                            Button.danger("next", "次の項目"))).send(tempTextChannel).join().getId();
                        }
                        TempChannelRecord tempChannelRecord = new TempChannelRecord();
                        tempChannelRecord.setServer_id(server.getId());
                        tempChannelRecord.setVoice_channel_id(tempVoiceChannel.getId());
                        if (TCCategory != null) {
                            tempChannelRecord.setText_channel_id(tempTextChannel.getId());
                            tempChannelRecord.setInfo_message_id(infoMessageId);
                        }
                        tempChannelRecord.setOwner_user_id(joinUser.getId());
                        tempChannelRecord.setNewCategoryBy(newCategory);
                        dao.addTempChannel(tempChannelRecord);
                    }
                } catch (CompletionException e) {
                    Objects.requireNonNull(tempVoiceChannel).delete();
                    if (newCategory) VCCategory.delete();
                }
            }
        });

        api.addServerVoiceChannelMemberJoinListener(serverVoiceChannelMemberJoinEvent -> {
            TempChannelDAO dao = new TempChannelDAO();
            ServerVoiceChannel channel = serverVoiceChannelMemberJoinEvent.getChannel();
            if (dao.tempVoiceChannelCheck(channel.getId())) {
                TempChannelRecord tempChannelRecord = dao.geTempJoinVoiceChannel(channel.getId());
                User joinUser = serverVoiceChannelMemberJoinEvent.getUser();
                if (api.getServerTextChannelById(tempChannelRecord.getText_channel_id()).isPresent()) {
                    Processing processing = new Processing();
                    ServerTextChannel textChannel = api.getServerTextChannelById(tempChannelRecord.getText_channel_id()).get();
                    textChannel.createUpdater().addPermissionOverwrite(joinUser, joinUser.getId() == tempChannelRecord.getOwner_user_id() ? processing.getChannelManagePermission() : processing.getUserPermission()).update().join();
                }
            }
        });

        api.addServerVoiceChannelMemberLeaveListener(serverVoiceChannelMemberLeaveEvent -> {
            TempChannelDAO dao = new TempChannelDAO();
            ServerVoiceChannel channel = serverVoiceChannelMemberLeaveEvent.getChannel();
            if (dao.tempVoiceChannelCheck(channel.getId()) && channel.getConnectedUserIds().size() != 0) {
                TempChannelRecord tempChannelRecord = dao.geTempLeaveVoiceChannel(channel.getId());
                User leaveUser = serverVoiceChannelMemberLeaveEvent.getUser();
                if (api.getServerTextChannelById(tempChannelRecord.getText_channel_id()).isPresent()) {
                    ServerTextChannel textChannel = api.getServerTextChannelById(tempChannelRecord.getText_channel_id()).get();
                    textChannel.createUpdater().removePermissionOverwrite(leaveUser).update().join();
                }
            }
        });

        api.addServerVoiceChannelMemberLeaveListener(serverVoiceChannelMemberLeaveEvent -> {
            TempChannelDAO dao = new TempChannelDAO();
            ServerVoiceChannel channel = serverVoiceChannelMemberLeaveEvent.getChannel();
            if (dao.tempVoiceChannelCheck(channel.getId()) && channel.getConnectedUserIds().size() == 0) {
                TempChannelRecord tempChannelRecord = dao.geTempVoiceChannelZeroUsers(channel.getId());
                if (tempChannelRecord.isNewCategoryBy() && api.getChannelCategoryById(tempChannelRecord.getCategory_id()).isPresent()) {
                    api.getChannelCategoryById(tempChannelRecord.getCategory_id()).get().delete();
                }
                if (api.getServerVoiceChannelById(tempChannelRecord.getVoice_channel_id()).isPresent()) {
                    api.getServerVoiceChannelById(tempChannelRecord.getVoice_channel_id()).get().delete();
                }
                if (api.getServerTextChannelById(tempChannelRecord.getText_channel_id()).isPresent()) {
                    api.getServerTextChannelById(tempChannelRecord.getText_channel_id()).get().delete();
                }
                for (MessageRecord messageRecord : dao.getMentionMessage(tempChannelRecord.getVoice_channel_id())) {
                    if (api.getMessageByLink(messageRecord.getLink()).isPresent()) {
                        api.getMessageByLink(messageRecord.getLink()).get().join().delete();
                    }
                }
                dao.deleteTempChannel(tempChannelRecord);
            }
        });

        api.addSlashCommandCreateListener(slashCommandCreateEvent -> {
            SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
            if (interaction.getCommandName().equals("add") && interaction.getOptionByIndex(0).isPresent() && interaction.getServer().isPresent()) {
                TempChannelDAO dao = new TempChannelDAO();
                Server server = interaction.getServer().get();
                User user = interaction.getUser();
                if (server.isAdmin(user)) {
                    SlashCommandInteractionOption slashCommandInteractionOption = interaction.getOptionByIndex(0).get();
                    int namePresetSize = dao.getNamePresetSize(server.getId());
                    if (namePresetSize >= 25) {
                        CancelEmbed cancelEmbed = new CancelEmbed();
                        interaction.createImmediateResponder().addEmbed(cancelEmbed.createEmbed(Constant.NAME_PRESET_SIZE_OVER)).setFlags(MessageFlag.EPHEMERAL).respond();
                    } else if (slashCommandInteractionOption.getName().equals("name_preset") && slashCommandInteractionOption.getOptionStringValueByName("name").isPresent()) {
                        String name = slashCommandInteractionOption.getOptionStringValueByName("name").get();
                        NamePresetRecord namePresetRecord = new NamePresetRecord();
                        namePresetRecord.setName(name);
                        namePresetRecord.setServer_id(server.getId());
                        dao.addNamePreset(namePresetRecord);
                        CompleteEmbed completeEmbed = new CompleteEmbed();
                        interaction.createImmediateResponder().addEmbed(completeEmbed.createEmbed(name, Constant.NAME_PRESET_SET)).setFlags(MessageFlag.EPHEMERAL).respond();
                    }
                } else {
                    CancelEmbed cancelEmbed = new CancelEmbed();
                    interaction.createImmediateResponder().addEmbed(cancelEmbed.createEmbed(Constant.NOT_ADMIN)).setFlags(MessageFlag.EPHEMERAL).respond();
                }
            }
        });
    }
}
