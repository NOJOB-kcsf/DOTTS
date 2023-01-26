package kcs.graduation.discord.command;

import kcs.graduation.discord.system.core.SystemMaster;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.interaction.*;

import java.util.Arrays;
import java.util.Collections;

public class DottsCommand extends SystemMaster {
    DiscordApi api = getAPI();

    public void createdAllCommands() {
        SlashCommand.with("ping", "速度計算").createGlobal(api).join();
        SlashCommand.with("claim", "通話権限取得").createGlobal(api).join();
        SlashCommand.with("n", "通話名変更", Collections.singletonList(
                new SlashCommandOptionBuilder().setName("name").setDescription("変更名").setType(SlashCommandOptionType.STRING).setMinLength(1).setMaxLength(100).setRequired(true).build()
        )).createGlobal(api).join();

        SlashCommand.with("name", "通話名変更", Collections.singletonList(
                new SlashCommandOptionBuilder().setName("name").setDescription("変更名").setType(SlashCommandOptionType.STRING).setMinLength(1).setMaxLength(100).setRequired(true).build()
        )).createGlobal(api).join();

        SlashCommand.with("s", "通話人数変更", Collections.singletonList(
                new SlashCommandOptionBuilder().setName("size").setDescription("最大人数").setType(SlashCommandOptionType.LONG).setLongMinValue(1).setLongMaxValue(99).setRequired(true).build()
        )).createGlobal(api).join();

        SlashCommand.with("size", "通話人数変更", Collections.singletonList(
                new SlashCommandOptionBuilder().setName("size").setDescription("最大人数").setType(SlashCommandOptionType.LONG).setLongMinValue(1).setLongMaxValue(99).setRequired(true).build()
        )).createGlobal(api).join();

        SlashCommand.with("men", "通話募集送信", Collections.singletonList(
                new SlashCommandOptionBuilder().setName("text").setDescription("募集詳細").setType(SlashCommandOptionType.STRING).setMinLength(1).setMaxLength(1000).setRequired(true).build()
        )).createGlobal(api).join();

        SlashCommand.with("m", "通話募集送信", Collections.singletonList(
                new SlashCommandOptionBuilder().setName("text").setDescription("募集詳細").setType(SlashCommandOptionType.STRING).setMinLength(1).setMaxLength(1000).setRequired(true).build()
        )).createGlobal(api).join();

        SlashCommand.with("user", "ユーザー操作",
                Arrays.asList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "add", "通話ユーザー追加",
                                Collections.singletonList(
                                        new SlashCommandOptionBuilder().setName("selectUser").setDescription("対象ユーザー").setType(SlashCommandOptionType.USER).setRequired(true).build()
                                )
                        ),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "delete", "通話ユーザー削除",
                                Collections.singletonList(
                                        new SlashCommandOptionBuilder().setName("selectUser").setDescription("対象ユーザー").setType(SlashCommandOptionType.USER).setRequired(true).build()
                                )
                        )
                )
        ).createGlobal(api).join();

        SlashCommand.with("set", "設定",
                Arrays.asList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "static_channel", "固定通話設定",
                                Collections.singletonList(
                                        new SlashCommandOptionBuilder().addChannelType(ChannelType.SERVER_VOICE_CHANNEL).setType(SlashCommandOptionType.CHANNEL).setName("channel").setDescription("リンクするVC").setRequired(true).build()
                                )
                        ),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "event_category", "イベント通話",
                                Collections.singletonList(
                                        new SlashCommandOptionBuilder().addChannelType(ChannelType.CHANNEL_CATEGORY).setType(SlashCommandOptionType.CHANNEL).setName("category").setDescription("イベント通話作成先カテゴリ").setRequired(true).build()
                                )
                        ),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "under_category", "カテゴリの下に一時通話カテゴリ作成する場合",
                                Collections.singletonList(
                                        new SlashCommandOptionBuilder().addChannelType(ChannelType.CHANNEL_CATEGORY).setType(SlashCommandOptionType.CHANNEL).setName("category").setDescription("どこの下に作るか").setRequired(true).build()
                                )
                        ),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND_GROUP, "temp", "一時通話・チャット",
                                Arrays.asList(
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "first", "作成時に入る通話",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().addChannelType(ChannelType.SERVER_VOICE_CHANNEL).setType(SlashCommandOptionType.CHANNEL).setName("channel").setDescription("初期通話定").setRequired(true).build()
                                                )
                                        ),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "mention", "募集送信",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().addChannelType(ChannelType.SERVER_TEXT_CHANNEL).setType(SlashCommandOptionType.CHANNEL).setName("channel").setDescription("送信先").setRequired(true).build()
                                                )
                                        ),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "vc_category", "通話作成先設定",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().addChannelType(ChannelType.CHANNEL_CATEGORY).setType(SlashCommandOptionType.CHANNEL).setName("category").setDescription("どこに一時通話を作成するか").setRequired(true).build()
                                                )
                                        ),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "vc_by", "一時通話を作成するか",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().setType(SlashCommandOptionType.BOOLEAN).setName("by").setDescription("可否").setRequired(true).build()
                                                )
                                        ),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "tc_category", "通話作成先設定",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().addChannelType(ChannelType.CHANNEL_CATEGORY).setType(SlashCommandOptionType.CHANNEL).setName("category").setDescription("どこに一時チャットを作成するか").setRequired(true).build()
                                                )
                                        ),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "tc_by", "一時チャットを作成するか",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().setType(SlashCommandOptionType.BOOLEAN).setName("by").setDescription("可否").setRequired(true).build()
                                                )
                                        ),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "new_category", "カテゴリごと作成するか",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().setType(SlashCommandOptionType.BOOLEAN).setName("by").setDescription("可否").setRequired(true).build()
                                                )
                                        ),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "default_size", "初期人数設定",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().setType(SlashCommandOptionType.LONG).setName("size").setDescription("人数").setLongMaxValue(99).setLongMinValue(0).setRequired(true).build()
                                                )
                                        ),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "default_name", "初期通話ネーム設定",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().setType(SlashCommandOptionType.STRING).setName("name").setDescription("名前").setMaxLength(100).setMinLength(1).setRequired(true).build()
                                                )
                                        ),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "template", "募集定型文設定",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().setType(SlashCommandOptionType.STRING).setName("template").setDescription("定型文").setMaxLength(1000).setMinLength(1).setRequired(true).build()
                                                )
                                        )
                                )
                        )
                )
        ).createGlobal(api).join();
        SlashCommand.with("start", "開始",
                Collections.singletonList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND_GROUP, "auto_delete", "自動チャット削除設定",
                                Arrays.asList(
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "set", "一括設定",
                                                Arrays.asList(
                                                        new SlashCommandOptionBuilder().setType(SlashCommandOptionType.LONG).setName("time").setDescription("時間").setRequired(true).build(),
                                                        new SlashCommandOptionBuilder().setType(SlashCommandOptionType.STRING).setName("unit").setDescription("単位")
                                                                .addChoice("秒", "S")
                                                                .addChoice("分", "M")
                                                                .addChoice("時間", "H")
                                                                .addChoice("日", "D")
                                                                .addChoice("年", "Y")
                                                                .setRequired(true)
                                                                .build()
                                                )
                                        ),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "edit_time", "時間編集",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().setType(SlashCommandOptionType.LONG).setName("time").setDescription("時間").setRequired(true).build()
                                                )
                                        ),
                                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "edit_unit", "単位編集",
                                                Collections.singletonList(
                                                        new SlashCommandOptionBuilder().setType(SlashCommandOptionType.STRING).setName("unit").setDescription("単位")
                                                                .addChoice("秒", "S")
                                                                .addChoice("分", "M")
                                                                .addChoice("時間", "H")
                                                                .addChoice("日", "D")
                                                                .addChoice("年", "Y")
                                                                .setRequired(true)
                                                                .build()
                                                )
                                        )
                                )
                        )
                )
        ).createGlobal(api).join();
        SlashCommand.with("stop", "停止",
                Collections.singletonList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "auto_delete", "自動チャット削除")
                )
        ).createGlobal(api).join();
        SlashCommand.with("add", "追加",
                Arrays.asList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "name_preset", "名前変更",
                                Collections.singletonList(
                                        new SlashCommandOptionBuilder().setType(SlashCommandOptionType.STRING).setName("name").setDescription("候補").setMaxLength(100).setMinLength(1).setRequired(true).build()
                                )
                        ),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "event_data", "イベント情報",
                                Arrays.asList(
                                        new SlashCommandOptionBuilder().setName("event_name").setType(SlashCommandOptionType.STRING).setMaxLength(1000).setMinLength(1).setDescription("イベントの名前").setRequired(true).build(),
                                        new SlashCommandOptionBuilder().setName("channel_name").setType(SlashCommandOptionType.STRING).setMaxLength(100).setMinLength(1).setDescription("エベント通話名").setRequired(true).build(),
                                        new SlashCommandOptionBuilder().setName("next_start_time").setType(SlashCommandOptionType.STRING).setDescription("最初にいつ実行するか(YYYY-MM-DD HH:MM:SS)").setRequired(true).build(),
                                        new SlashCommandOptionBuilder().setName("delay_time").setType(SlashCommandOptionType.LONG).setDescription("次の実行する時間").setRequired(true).build(),
                                        new SlashCommandOptionBuilder().setName("delay_unit").setType(SlashCommandOptionType.STRING).setDescription("次の実行する時間単位")
                                                .addChoice("秒", "S")
                                                .addChoice("分", "M")
                                                .addChoice("時間", "H")
                                                .addChoice("日", "D")
                                                .addChoice("年", "Y")
                                                .setRequired(true)
                                                .build(),
                                        new SlashCommandOptionBuilder().setName("end_time").setType(SlashCommandOptionType.LONG).setDescription("イベント終了時間").setRequired(true).build(),
                                        new SlashCommandOptionBuilder().setName("end_unit").setType(SlashCommandOptionType.STRING).setDescription("イベント終了時間単位")
                                                .addChoice("秒", "S")
                                                .addChoice("分", "M")
                                                .addChoice("時間", "H")
                                                .addChoice("日", "D")
                                                .addChoice("年", "Y")
                                                .setRequired(true)
                                                .build(),
                                        new SlashCommandOptionBuilder().setName("event_description").setDescription("イベントの説明").setType(SlashCommandOptionType.STRING).build(),
                                        new SlashCommandOptionBuilder().setName("mention").setDescription("メンションする場合のロール").setType(SlashCommandOptionType.ROLE).build(),
                                        new SlashCommandOptionBuilder().setName("channel_default_size").setDescription("通話制限人数").setType(SlashCommandOptionType.LONG).setLongMaxValue(99).setLongMinValue(0).build()
                                )
                        )
                )
        ).createGlobal(api).join();
        SlashCommand.with("remove", "設定削除",
                Arrays.asList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "name_preset", "名前削除"),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "event_data", "イベント削除")
                )
        ).createGlobal(api).join();
        SlashCommand.with("translate", "Translate the message into the specified language and send it.",
                        Collections.singletonList(
                                SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "Language", "Language after translation", true,
                                        Arrays.asList(
                                                SlashCommandOptionChoice.create("Bulgarian", "BG"),
                                                SlashCommandOptionChoice.create("Czech", "CS"),
                                                SlashCommandOptionChoice.create("Danish", "DA"),
                                                SlashCommandOptionChoice.create("German", "DE"),
                                                SlashCommandOptionChoice.create("Greek", "EL"),
                                                SlashCommandOptionChoice.create("English (British)", "EN-GB"),
                                                SlashCommandOptionChoice.create("English (American)", "EN-US"),
                                                SlashCommandOptionChoice.create("Spanish", "ES"),
                                                SlashCommandOptionChoice.create("Estonian", "ET"),
                                                SlashCommandOptionChoice.create("Finnish", "FI"),
                                                SlashCommandOptionChoice.create("French", "FR"),
                                                SlashCommandOptionChoice.create("Hungarian", "HU"),
                                                SlashCommandOptionChoice.create("Indonesian", "ID"),
                                                SlashCommandOptionChoice.create("Italian", "IT"),
                                                SlashCommandOptionChoice.create("Japanese", "JA"),
                                                SlashCommandOptionChoice.create("Lithuanian", "LT"),
                                                SlashCommandOptionChoice.create("Latvian", "LV"),
                                                SlashCommandOptionChoice.create("Dutch", "NL"),
                                                SlashCommandOptionChoice.create("Polish", "PL"),
                                                SlashCommandOptionChoice.create("Portuguese (Brazilian)", "PT-BR"),
                                                SlashCommandOptionChoice.create("Romanian", "RO"),
                                                SlashCommandOptionChoice.create("Russian", "RU"),
                                                SlashCommandOptionChoice.create("Turkish", "TR"),
                                                SlashCommandOptionChoice.create("Ukrainian", "UK"),
                                                SlashCommandOptionChoice.create("Chinese (simplified)", "ZH")
                                        )
                                )
                        )
                )
                .addDescriptionLocalization(DiscordLocale.JAPANESE, "指定した言語に翻訳してメッセージを送信します。").createGlobal(api).join();

        new MessageContextMenuBuilder()
                .setName("translate")
                .setDefaultEnabledForEveryone()
                .createGlobal(api).join();
    }

    public void deleteAllCommands() {
        for (SlashCommand slashCommand : api.getGlobalSlashCommands().join()) {
            slashCommand.deleteGlobal().join();
        }

        for (MessageContextMenu messageContextMenu : api.getGlobalMessageContextMenus().join()) {
            messageContextMenu.deleteGlobal();
        }
    }

    public void allCommandShow() {
        for (SlashCommand slashCommand : api.getGlobalSlashCommands().join()) {
            System.out.println(slashCommand.getName());
        }
        for (MessageContextMenu messageContextMenu : api.getGlobalMessageContextMenus().join()) {
            System.out.println(messageContextMenu.getName());
        }
    }
}
