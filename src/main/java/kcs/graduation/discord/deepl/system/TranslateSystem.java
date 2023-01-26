package kcs.graduation.discord.deepl.system;

import kcs.graduation.discord.record.TranslateRecord;
import kcs.graduation.discord.system.core.SystemMaster;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageContextMenuInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TranslateSystem extends SystemMaster {
    DiscordApi api = getAPI();

    Map<String, String> emojiTargetLanguageMap = new HashMap<>();
    Map<String, String> discordLocalTargetLanguageMap = new HashMap<>();

    public TranslateSystem() {
        putDataEmojiAndLanguage();
        putDataDiscordLocalAndLanguage();
        TranslateAsyncByDeepL translateAsyncByDeepL = new TranslateAsyncByDeepL();
        api.addReactionAddListener(reactionAddEvent -> {
            reactionAddEvent.getEmoji().asUnicodeEmoji();
            if (reactionAddEvent.getUser().isPresent() && reactionAddEvent.getMessageContent().isPresent() && reactionAddEvent.getEmoji().isUnicodeEmoji()
                    && reactionAddEvent.getEmoji().asUnicodeEmoji().isPresent()
                    && emojiTargetLanguageMap.containsKey(reactionAddEvent.getEmoji().asUnicodeEmoji().get())) {
                TranslateRecord record = new TranslateRecord();
                record.setUser(reactionAddEvent.getUser().get());
                record.setText(reactionAddEvent.getMessageContent().get());
                record.setLanguage(emojiTargetLanguageMap.get(reactionAddEvent.getEmoji().asUnicodeEmoji().get()));
                record = translateAsyncByDeepL.byDeepLToAPI(record);
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setAuthor(record.getUser())
                        .setDescription(record.getResult())
                        .setColor(Color.GREEN);
                if (record.isComplete() && reactionAddEvent.getUser().isPresent() && !reactionAddEvent.getUser().get().isBot()) {
                    reactionAddEvent.getUser().get().sendMessage(embedBuilder).join();
                    reactionAddEvent.removeReaction().join();
                }
            }
        });

        api.addMessageContextMenuCommandListener(messageContextMenuCommandEvent -> {
            MessageContextMenuInteraction interaction = messageContextMenuCommandEvent.getMessageContextMenuInteraction();
            if (discordLocalTargetLanguageMap.containsKey(interaction.getLocale().getLocaleCode())) {
                TranslateRecord record = new TranslateRecord();
                record.setUser(interaction.getUser());
                record.setText(interaction.getTarget().getContent());
                record.setLanguage(discordLocalTargetLanguageMap.get(interaction.getLocale().getLocaleCode()));
                record = translateAsyncByDeepL.byDeepLToAPI(record);
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setAuthor(record.getUser())
                        .setDescription(record.getResult())
                        .setColor(Color.GREEN);
                if (record.isComplete() && !interaction.getUser().isBot()) {
                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).addEmbed(embedBuilder).respond();
                }
            }
        });

        api.addSlashCommandCreateListener(slashCommandCreateEvent -> {
            SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
            String cmd = interaction.getCommandName();
            if (Objects.equals(cmd, "translate") && interaction.getOptionStringValueByName("text").isPresent() && interaction.getOptionStringValueByName("Language").isPresent()) {
                if (emojiTargetLanguageMap.containsValue(interaction.getOptionStringValueByName("Language").get())) {
                    TranslateRecord record = new TranslateRecord();
                    record.setUser(interaction.getUser());
                    record.setText(interaction.getOptionStringValueByName("text").get());
                    record.setLanguage(interaction.getOptionStringValueByName("Language").get());
                    record = translateAsyncByDeepL.byDeepLToAPI(record);
                    EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setAuthor(record.getUser())
                            .setDescription(record.getResult())
                            .setColor(Color.GREEN);
                    if (record.isComplete() && !interaction.getUser().isBot()) {
                        interaction.createImmediateResponder().addEmbed(embedBuilder).respond();
                    }
                }
            }
        });
    }


    public void putDataDiscordLocalAndLanguage() {
        discordLocalTargetLanguageMap.put("bg", "BG");    // ブルガリア語
        discordLocalTargetLanguageMap.put("cs", "CS");    // チェコ語
        discordLocalTargetLanguageMap.put("da", "DA");    // デンマーク語
        discordLocalTargetLanguageMap.put("de", "DE");    // ドイツ語
        discordLocalTargetLanguageMap.put("el", "EL");    // ギリシャ語
        discordLocalTargetLanguageMap.put("en-GB", "EN-GB"); // 英語 (イギリス)
        discordLocalTargetLanguageMap.put("en-US", "EN-US"); // 英語 (アメリカ)
        discordLocalTargetLanguageMap.put("es", "ES");    // スペイン語
        discordLocalTargetLanguageMap.put("es-ES", "ET");    // エストニア語
        discordLocalTargetLanguageMap.put("fi", "FI");    // フィンランド語
        discordLocalTargetLanguageMap.put("fr", "FR");    // フランス語
        discordLocalTargetLanguageMap.put("hu", "HU");    // ハンガリー語
        // discordLocalTargetLanguageMap.put("","ID");    // インドネシア語 なし
        discordLocalTargetLanguageMap.put("it", "IT");    // イタリア語
        discordLocalTargetLanguageMap.put("ja", "JA");    // 日本語
        discordLocalTargetLanguageMap.put("lt", "LT");    // リトアニア語
        // discordLocalTargetLanguageMap.put("","LV");    // ラトビア語
        discordLocalTargetLanguageMap.put("nl", "NL");    // オランダ語
        discordLocalTargetLanguageMap.put("pl", "PL");    // ポーランド語
        discordLocalTargetLanguageMap.put("pt-BR", "PT-BR"); // ポルトガル語 (ブラジル)
        // discordLocalTargetLanguageMap.put("","PT-PT"); // ポルトガル語（ブラジル系ポルトガル語を除くすべてのポルトガル語）
        discordLocalTargetLanguageMap.put("ro", "RO");    // ルーマニア語
        discordLocalTargetLanguageMap.put("ru", "RU");    // ロシア語
        // discordLocalTargetLanguageMap.put("","SK");    // スロバキア語
        // discordLocalTargetLanguageMap.put("","SL");    // スロベニア語
        discordLocalTargetLanguageMap.put("sv-SE", "SV");    // スウェーデン語
        discordLocalTargetLanguageMap.put("tr", "TR");    // トルコ語
        discordLocalTargetLanguageMap.put("uk", "UK");    // ウクライナ語
        discordLocalTargetLanguageMap.put("zh-CN", "ZH");    // 中国語
    }

    public void putDataEmojiAndLanguage() {
        emojiTargetLanguageMap.put("\uD83C\uDDE7\uD83C\uDDEC", "BG");    // ブルガリア語
        emojiTargetLanguageMap.put("\uD83C\uDDE8\uD83C\uDDFF", "CS");    // チェコ語
        emojiTargetLanguageMap.put("\uD83C\uDDE9\uD83C\uDDF0", "DA");    // デンマーク語
        emojiTargetLanguageMap.put("\uD83C\uDDE9\uD83C\uDDEA", "DE");    // ドイツ語
        emojiTargetLanguageMap.put("\uD83C\uDDEC\uD83C\uDDF7", "EL");    // ギリシャ語
        emojiTargetLanguageMap.put("\uD83C\uDDEC\uD83C\uDDE7", "EN-GB"); // 英語 (イギリス)
        emojiTargetLanguageMap.put("\uD83C\uDDFA\uD83C\uDDF8", "EN-US"); // 英語 (アメリカ)
        emojiTargetLanguageMap.put("\uD83C\uDDEA\uD83C\uDDF8", "ES");    // スペイン語
        emojiTargetLanguageMap.put("\uD83C\uDDEA\uD83C\uDDEA", "ET");    // エストニア語
        emojiTargetLanguageMap.put("\uD83C\uDDEB\uD83C\uDDEE", "FI");    // フィンランド語
        emojiTargetLanguageMap.put("\uD83C\uDDEB\uD83C\uDDF7", "FR");    // フランス語
        emojiTargetLanguageMap.put("\uD83C\uDDED\uD83C\uDDFA", "HU");    // ハンガリー語
        emojiTargetLanguageMap.put("\uD83C\uDDEE\uD83C\uDDE9", "ID");    // インドネシア語
        emojiTargetLanguageMap.put("\uD83C\uDDEE\uD83C\uDDF9", "IT");    // イタリア語
        emojiTargetLanguageMap.put("\uD83C\uDDEF\uD83C\uDDF5", "JA");    // 日本語
        emojiTargetLanguageMap.put("\uD83C\uDDF1\uD83C\uDDF9", "LT");    // リトアニア語
        emojiTargetLanguageMap.put("\uD83C\uDDF1\uD83C\uDDFB", "LV");    // ラトビア語
        emojiTargetLanguageMap.put("\uD83C\uDDF3\uD83C\uDDF1", "NL");    // オランダ語
        emojiTargetLanguageMap.put("\uD83C\uDDF5\uD83C\uDDF1", "PL");    // ポーランド語
        emojiTargetLanguageMap.put("\uD83C\uDDE7\uD83C\uDDF7", "PT-BR"); // ポルトガル語 (ブラジル)
        emojiTargetLanguageMap.put("\uD83C\uDDF5\uD83C\uDDF9", "PT-PT"); // ポルトガル語（ブラジル系ポルトガル語を除くすべてのポルトガル語）//
        emojiTargetLanguageMap.put("\uD83C\uDDF7\uD83C\uDDF4", "RO");    // ルーマニア語
        emojiTargetLanguageMap.put("\uD83C\uDDF7\uD83C\uDDFA", "RU");    // ロシア語
        emojiTargetLanguageMap.put("\uD83C\uDDF8\uD83C\uDDF0", "SK");    // スロバキア語 //
        emojiTargetLanguageMap.put("\uD83C\uDDF8\uD83C\uDDEE", "SL");    // スロベニア語 //
        emojiTargetLanguageMap.put("\uD83C\uDDF8\uD83C\uDDEA", "SV");    // スウェーデン語
        emojiTargetLanguageMap.put("\uD83C\uDDF9\uD83C\uDDF7", "TR");    // トルコ語
        emojiTargetLanguageMap.put("\uD83C\uDDFA\uD83C\uDDE6", "UK");    // ウクライナ語
        emojiTargetLanguageMap.put("\uD83C\uDDE8\uD83C\uDDF3", "ZH");    // 中国語
    }
}
