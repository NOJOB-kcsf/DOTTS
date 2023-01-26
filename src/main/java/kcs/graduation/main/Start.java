package kcs.graduation.main;

import kcs.graduation.discord.command.DottsCommand;
import kcs.graduation.discord.dao.DiscordDAO;
import kcs.graduation.discord.deepl.system.TranslateSystem;
import kcs.graduation.discord.system.*;
import kcs.graduation.discord.system.core.SystemMaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Start {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter the name of the BOT to be activated.\n -> ");

            SystemMaster master = new SystemMaster();
            master.createApi("MAIN");


            // システムを起動
            new StaticChannel();
            new TempChannel();
            new EventChannel();
            new ServerSetting();
            new DeleteSelect();
            new InfoMessage();
            new JoinAndLeaveServer();
            new SlashCommand();
            new TranslateSystem();

            DottsCommand command = new DottsCommand();
            while (true) {
                String cmd = br.readLine();
                if ("stop".equalsIgnoreCase(cmd)) {
                    System.out.println("Exit the system.");
                    System.exit(0);
                } else if ("create".equalsIgnoreCase(cmd)) {
                    command.createdAllCommands();
                } else if ("delete".equalsIgnoreCase(cmd)) {
                    command.deleteAllCommands();
                } else if ("show".equalsIgnoreCase(cmd)) {
                    command.allCommandShow();
                } else if ("deepl".equalsIgnoreCase(cmd)) {
                    System.out.print("Enter the name.\n -> ");
                    String botName = br.readLine();
                    System.out.print("Enter the DeepLToken.\n -> ");
                    String newToken = br.readLine();
                    DiscordDAO dao = new DiscordDAO();
                    System.out.println("Update Count: " + dao.setDeepLToken(botName, newToken));
                } else {
                    System.out.println("Command not found.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
