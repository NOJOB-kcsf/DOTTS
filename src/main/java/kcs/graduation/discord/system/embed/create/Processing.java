package kcs.graduation.discord.system.embed.create;

import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;

import static kcs.graduation.discord.system.embed.Constant.*;

public class Processing {
    public static String getDescription(int num) {
        return switch (num) {
            case TEMP_USER_IS_NO_OWNER -> "あなたはこのチャンネルの所有者ではありません。";
            case STATIC_SETTING_COMPLETE ->
                    "&voice_channel&とこのチャンネルをリンクしました。\n以後&voice_channel&に入ったユーザーにはこのチャンネルが閲覧出来ます。";
            case STATIC_SETTING_CANCEL -> "&voice_channel&はこのサーバーの通話チャンネルではありません。";
            case STATIC_SETTING_NO_PERMISSION_NO_MANAGE_CANCEL ->
                    "&voice_channel&のチャンネル編集権限を持っていません。\nロールまたはチャンネルの権限内の[チャンネルの管理]をオンにしてください。";
            case EVENT_CATEGORY_SET_COMPLETE -> "&category&をイベント通話作成先カテゴリに設定しました。";
            case EVENT_DATA_SET_CANCEL_NOT_DATE_FORMAT ->
                    "以下のフォーマットで入力してください。\nyyyy-MM-dd HH:mm:ss(例:2022-01-01 14:05:09)";
            case EVENT_SET_DATE_EXCESS_ERROR -> "設定した時刻が現在より前になっています。";
            case EVENT_DATA_NOT_CATEGORY_ERROR -> "イベントチャネル作成先カテゴリが設定されていません。";
            case EVENT_DATA_COVERING_NAME_ERROR -> "すでにその名前のイベントは存在しています。";
            case EVENT_DATA_DELAY_OVER_END_TIME_ERROR -> "イベント終了時間より周期時間ほうが短くなってます。";
            case NOT_ADMIN -> "あなたはサーバー管理者ではありません";
            case SETTING_SET_MENTION_CHANNEL -> "&text_channel&を募集送信先をして登録しました。";
            case SETTING_SET_CREATED_UNDER_CATEGORY -> "一時通話をカテゴリごと作る場合は&category&の下に生成されます。";
            case SETTING_SET_FIRST_CHANNEL -> "一時通話作成トリガー通話を&voice_channel&に設定しました。";
            case SETTING_SET_TEMP_VOICE_CATEGORY -> "一時通話作成先を&category&に設定しました。";
            case SETTING_SET_TEMP_BY -> "一時通話作成を&by&しました。";
            case SETTING_SET_TEMP_TEXT_CATEGORY -> "一時テキストチャンネル作成先を&category&に設定しました。";
            case SETTING_SET_TEMP_TEXT_BY -> "一時テキストチャンネル作成を&by&しました。";
            case SETTING_SET_NEW_CATEGORY_BY -> "カテゴリごとの作成を&by&しました。";
            case SETTING_SET_DEFAULT_SIZE -> "通話人数を&int&に設定しました";
            case SETTING_SET_DEFAULT_NAME -> "一時通話名の初期ネームを&str&に設定しました。";
            case SETTING_SET_STEREO_TYPED -> "定型文を&str&に\n設定しました。";
            case TEMP_IS_HIDE -> "&voice_channel&は非表示になりました。";
            case TEMP_IS_NOT_HIDE -> "&voice_channel&は表示されました。";
            case TEMP_IS_LOCK -> "&voice_channel&はロックされました。";
            case TEMP_IS_NOT_LOCK -> "&voice_channel&はロック解除されました。";
            case NAME_PRESET_SET -> "名前の定型文として&str&を追加しました。";
            case NAME_PRESET_NO_ELEMENT ->
                    "選択肢が存在しません。\n`/add name_preset name:`\n**name:**の後に選択肢に追加したい文字に入力して実行してください";
            case MENTION_MESSAGE_DELETE -> "募集メッセージを削除しました。";
            case TEMP_ALREADY_OWNER -> "チャンネル所有者がいらっしゃいます。";
            case TEMP_USER_IS_OWNER -> "あなたは既にこのチャンネルの所有者です。";
            case TEMP_CHANGE_OWNER -> "<@!&Long&>にチャンネルの所有者を譲渡しました。";
            case TEMP_SIZE_SET -> "通話人数を&Int&人に設定しました。";
            case TEMP_CHANGE_NAME -> "一時通話名を&str&に設定しました。";
            case EVENT_DATA_NO_ELEMENT -> "イベントが存在しません。";
            default -> "エラー";
        };
    }

    public static String getTitle(int num) {
        return switch (num) {
            case NOT_ADMIN -> "権限エラー";
            case STATIC_SETTING_COMPLETE -> "固定通話設定完了";
            case STATIC_SETTING_CANCEL
                    , STATIC_SETTING_NO_PERMISSION_NO_MANAGE_CANCEL -> "固定通話設定失敗 code:" + num;
            case EVENT_CATEGORY_SET_COMPLETE -> "イベント通話作成カテゴリ設定完了";
            case EVENT_DATA_SET_COMPLETE -> "イベントデータ設定完了";
            case EVENT_DATA_SET_CANCEL_NOT_DATE_FORMAT -> "フォーマットエラー code:" + num;
            case EVENT_SET_DATE_EXCESS_ERROR
                    , EVENT_DATA_DELAY_OVER_END_TIME_ERROR -> "時刻設定エラー code:" + num;
            case EVENT_DATA_NOT_CATEGORY_ERROR -> "設定なしエラー code:" + num;
            case EVENT_DATA_COVERING_NAME_ERROR -> "イベント名前エラー code:" + num;
            case SETTING_SET_MENTION_CHANNEL
                    , SETTING_SET_FIRST_CHANNEL -> "チャンネル設定完了";
            case SETTING_SET_CREATED_UNDER_CATEGORY
                    , SETTING_SET_TEMP_VOICE_CATEGORY
                    , SETTING_SET_TEMP_TEXT_CATEGORY -> "カテゴリ設定完了";
            case SETTING_SET_TEMP_BY
                    , SETTING_SET_TEMP_TEXT_BY
                    , SETTING_SET_NEW_CATEGORY_BY -> "スイッチ設定";
            case SETTING_SET_DEFAULT_SIZE -> "一時通話初期人数制限設定完了";
            case SETTING_SET_DEFAULT_NAME -> "一時通話名設定完了";
            case SETTING_SET_STEREO_TYPED -> "通話募集定型文設定完了";
            case TEMP_USER_IS_NO_OWNER, TEMP_ALREADY_OWNER, TEMP_USER_IS_OWNER -> "実行エラー";
            case TEMP_IS_HIDE, TEMP_IS_NOT_HIDE -> "非表示変更";
            case TEMP_IS_LOCK, TEMP_IS_NOT_LOCK -> "参加設定変更";
            case NAME_PRESET_NO_ELEMENT -> "名前プリセットエラー";
            case MENTION_MESSAGE_DELETE -> "募集メッセージ削除";
            case TEMP_SIZE_SET -> "通話人数変更";
            case NAME_PRESET_SET -> "名前プリセット追加";
            case TEMP_CHANGE_NAME -> "通話名変更";
            case EVENT_DATA_NO_ELEMENT -> "イベントデータエラー";
            default -> "エラー code:" + num;
        };
    }

    public static String getDescription(int result_code, ServerVoiceChannel voiceChannel) {
        return getDescription(result_code).replaceAll("&voice_channel&", "<#" + voiceChannel.getIdAsString() + ">");
    }

    public static String getDescription(int result_code, ChannelCategory category) {
        return getDescription(result_code).replaceAll("&category&", "<#" + category.getIdAsString() + ">");
    }

    public static String getDescription(int result_code, ServerTextChannel textChannel) {
        return getDescription(result_code).replaceAll("&text_channel&", "<#" + textChannel.getIdAsString() + ">");
    }

    public static String getDescription(int result_code, boolean by) {
        return getDescription(result_code).replaceAll("&by&", by ? "有効化" : "無効化");
    }

    public static String getDescription(int result_code, String str) {
        return getDescription(result_code).replaceAll("&str&", str);
    }

    public static String getDescription(int result_code, int size) {
        return getDescription(result_code).replaceAll("&Int&", Integer.toString(size));
    }

    public static String getDescription(int result_code, long id) {
        return getDescription(result_code).replaceAll("&Long&", Long.toString(id));
    }
}
