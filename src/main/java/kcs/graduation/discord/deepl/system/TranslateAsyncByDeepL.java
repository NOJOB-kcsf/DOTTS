package kcs.graduation.discord.deepl.system;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import kcs.graduation.discord.dao.DeepLDAO;
import kcs.graduation.discord.dao.DiscordDAO;
import kcs.graduation.discord.record.TranslateRecord;

public class TranslateAsyncByDeepL {
    public TranslateRecord byDeepLToAPI(TranslateRecord translateRecord) {
        DeepLDAO dao = new DeepLDAO();
        String authKey = dao.getDeepLToken("MAIN");
        Translator translator = new Translator(authKey);
        TextResult result;
        try {
            result = translator.translateText(translateRecord.getText(), null, translateRecord.getLanguage());
            translateRecord.setResult(result.getText());
            translateRecord.setComplete(true);
        } catch (DeepLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return translateRecord;
    }
}
