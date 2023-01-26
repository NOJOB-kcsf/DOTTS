package kcs.graduation.discord.system;

import kcs.graduation.discord.dao.JoinAndLeaveServerDAO;
import kcs.graduation.discord.system.core.SystemMaster;
import org.javacord.api.DiscordApi;

public class JoinAndLeaveServer extends SystemMaster {
    DiscordApi api = getAPI();
    // madeW

    public JoinAndLeaveServer() {
        JoinAndLeaveServerDAO dao = new JoinAndLeaveServerDAO();
        api.addServerJoinListener(event -> dao.addServerData(event.getServer().getId()));

        api.addServerLeaveListener(event -> dao.deleteServerData(event.getServer().getId()));
    }
}
