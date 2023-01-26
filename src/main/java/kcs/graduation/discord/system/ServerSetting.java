package kcs.graduation.discord.system;

import kcs.graduation.discord.dao.ServerSettingDAO;
import kcs.graduation.discord.system.core.SystemMaster;
import kcs.graduation.discord.system.embed.Constant;
import kcs.graduation.discord.system.embed.ErrorNumber;
import kcs.graduation.discord.system.embed.create.CompleteEmbed;
import kcs.graduation.discord.system.embed.create.ErrorEmbed;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

public class ServerSetting extends SystemMaster {
    DiscordApi api = getAPI();

    public ServerSetting() {
        api.addSlashCommandCreateListener(slashCommandCreateEvent -> {
            SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
            String cmd = interaction.getCommandName();
            User sendUser = interaction.getUser();
            if (interaction.getServer().isPresent() && interaction.getChannel().isPresent() && interaction.getOptionByIndex(0).isPresent()) {
                InteractionImmediateResponseBuilder responseBuilder = interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL);
                Server server = interaction.getServer().get();
                long serverId = server.getId();
                if (cmd.equals("set")) {
                    if (server.isAdmin(sendUser)) {
                        SlashCommandInteractionOption subcommand = interaction.getOptionByIndex(0).get();
                        switch (subcommand.getName()) {
                            case "under_category" -> {
                                if (subcommand.getOptionChannelValueByName("category").isPresent()
                                        && subcommand.getOptionChannelValueByName("category").get().asChannelCategory().isPresent()) {
                                    ServerSettingDAO dao = new ServerSettingDAO();
                                    ChannelCategory underCategory = subcommand.getOptionChannelValueByName("category").get().asChannelCategory().get();
                                    dao.updateUnderCategory(serverId, underCategory.getId());
                                    responseBuilder.addEmbed(new CompleteEmbed().createEmbed(underCategory, Constant.SETTING_SET_CREATED_UNDER_CATEGORY)).respond();
                                }
                            }
                            case "temp" -> {
                                if (subcommand.getOptionByIndex(0).isPresent()) {
                                    subcommand = subcommand.getOptionByIndex(0).get();
                                    switch (subcommand.getName()) {
                                        case "first" -> {
                                            if (subcommand.getOptionChannelValueByName("channel").isPresent()
                                                    && subcommand.getOptionChannelValueByName("channel").get().asServerVoiceChannel().isPresent()) {
                                                ServerSettingDAO dao = new ServerSettingDAO();
                                                ServerVoiceChannel firstChannel = subcommand.getOptionChannelValueByName("channel").get().asServerVoiceChannel().get();
                                                dao.updateFirstChannel(serverId, firstChannel.getId());
                                                responseBuilder.addEmbed(new CompleteEmbed().createEmbed(firstChannel, Constant.SETTING_SET_FIRST_CHANNEL)).respond();
                                            }
                                        }
                                        case "mention" -> {
                                            if (subcommand.getOptionChannelValueByName("channel").isPresent()
                                                    && subcommand.getOptionChannelValueByName("channel").get().asServerTextChannel().isPresent()) {
                                                ServerSettingDAO dao = new ServerSettingDAO();
                                                ServerTextChannel mentionChannel = subcommand.getOptionChannelValueByName("channel").get().asServerTextChannel().get();
                                                dao.updateMentionChannel(serverId, mentionChannel.getId());
                                                responseBuilder.addEmbed(new CompleteEmbed().createEmbed(mentionChannel, Constant.SETTING_SET_MENTION_CHANNEL)).respond();
                                            }
                                        }
                                        case "vc_category" -> {
                                            if (subcommand.getOptionChannelValueByName("category").isPresent()
                                                    && subcommand.getOptionChannelValueByName("category").get().asChannelCategory().isPresent()) {
                                                ServerSettingDAO dao = new ServerSettingDAO();
                                                ChannelCategory voiceCategory = subcommand.getOptionChannelValueByName("category").get().asChannelCategory().get();
                                                dao.updateTempVoiceCategory(serverId, voiceCategory.getId());
                                                responseBuilder.addEmbed(new CompleteEmbed().createEmbed(voiceCategory, Constant.SETTING_SET_TEMP_VOICE_CATEGORY)).respond();
                                            }
                                        }
                                        case "vc_by" -> {
                                            if (subcommand.getOptionBooleanValueByName("by").isPresent()) {
                                                ServerSettingDAO dao = new ServerSettingDAO();
                                                boolean by = subcommand.getOptionBooleanValueByName("by").get();
                                                dao.updateTempVoiceCreateBy(serverId, by);
                                                responseBuilder.addEmbed(new CompleteEmbed().createEmbed(by, Constant.SETTING_SET_TEMP_BY)).respond();
                                            }
                                        }
                                        case "tc_category" -> {
                                            if (subcommand.getOptionChannelValueByName("category").isPresent()
                                                    && subcommand.getOptionChannelValueByName("category").get().asChannelCategory().isPresent()) {
                                                ServerSettingDAO dao = new ServerSettingDAO();
                                                ChannelCategory textCategory = subcommand.getOptionChannelValueByName("category").get().asChannelCategory().get();
                                                dao.updateTempTextCategory(serverId, textCategory.getId());
                                                responseBuilder.addEmbed(new CompleteEmbed().createEmbed(textCategory, Constant.SETTING_SET_TEMP_TEXT_CATEGORY)).respond();
                                            }
                                        }
                                        case "tc_by" -> {
                                            if (subcommand.getOptionBooleanValueByName("by").isPresent()) {
                                                ServerSettingDAO dao = new ServerSettingDAO();
                                                boolean by = subcommand.getOptionBooleanValueByName("by").get();
                                                dao.updateTempTextCreateBy(serverId, by);
                                                responseBuilder.addEmbed(new CompleteEmbed().createEmbed(by, Constant.SETTING_SET_TEMP_TEXT_BY)).respond();
                                            }
                                        }
                                        case "new_category" -> {
                                            if (subcommand.getOptionBooleanValueByName("by").isPresent()) {
                                                ServerSettingDAO dao = new ServerSettingDAO();
                                                boolean by = subcommand.getOptionBooleanValueByName("by").get();
                                                dao.updateNewCategoryCreateBy(serverId, by);
                                                responseBuilder.addEmbed(new CompleteEmbed().createEmbed(by, Constant.SETTING_SET_NEW_CATEGORY_BY)).respond();
                                            }
                                        }
                                        case "default_size" -> {
                                            if (subcommand.getOptionLongValueByName("size").isPresent()) {
                                                ServerSettingDAO dao = new ServerSettingDAO();
                                                int size = subcommand.getOptionLongValueByName("size").get().intValue();
                                                dao.updateDefaultSize(serverId, size);
                                                responseBuilder.addEmbed(new CompleteEmbed().createEmbed(size, Constant.SETTING_SET_DEFAULT_SIZE)).respond();
                                            }
                                        }
                                        case "default_name" -> {
                                            if (subcommand.getOptionStringValueByName("name").isPresent()) {
                                                ServerSettingDAO dao = new ServerSettingDAO();
                                                String name = subcommand.getOptionStringValueByName("name").get();
                                                dao.updateDefaultName(serverId, name);
                                                responseBuilder.addEmbed(new CompleteEmbed().createEmbed(name, Constant.SETTING_SET_DEFAULT_NAME)).respond();
                                            }
                                        }
                                        case "template" -> {
                                            if (subcommand.getOptionStringValueByName("template").isPresent()) {
                                                ServerSettingDAO dao = new ServerSettingDAO();
                                                String template = subcommand.getOptionStringValueByName("template").get();
                                                dao.updateTemplate(serverId, template);
                                                responseBuilder.addEmbed(new CompleteEmbed().createEmbed(template, Constant.SETTING_SET_STEREO_TYPED)).respond();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        responseBuilder.addEmbed(new ErrorEmbed().create(ErrorNumber.NOT_ADMIN)).respond();
                    }
                }
            }
        });
    }
}
