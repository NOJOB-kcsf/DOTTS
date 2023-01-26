package kcs.graduation.discord.system.core;

import kcs.graduation.discord.dao.DiscordDAO;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class SystemMaster {
    private static DiscordApi api;

    public void createApi(String botName) {
        if (api == null) {
            String botToken = new DiscordDAO().BotGetToken(botName);
            api = new DiscordApiBuilder().setToken(botToken).setAllIntents().login().join();
            System.out.println(api.createBotInvite());
        }
    }

    public DiscordApi getAPI() {
        return api;
    }
}
