package kcs.graduation.discord.dao;

import kcs.graduation.discord.dao.core.BaseDAO;
import kcs.graduation.discord.parameter.BotDataParameters;
import kcs.graduation.discord.parameter.DAOParameters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DiscordDAO extends BaseDAO {

    public String BotGetToken(String botName) {
        this.open();
        String token = "";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            String sql = "SELECT " + BotDataParameters.B_TOKEN.getParam() + " FROM " + DAOParameters.TABLE_BOT_DATA.getParam() + " WHERE " + BotDataParameters.B_NAME.getParam() + " = '" + botName + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) token = rs.getString(BotDataParameters.B_TOKEN.getParam());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(stmt);
        }
        return token;
    }

    public int setDeepLToken(String botName, String newToken) {
        this.open();
        PreparedStatement preStatement = null;
        int updateCount = 0;
        try {
            String sql = "UPDATE " + DAOParameters.TABLE_BOT_DATA.getParam() + " SET " + BotDataParameters.B_TOKEN.getParam() + " = ? WHERE " + BotDataParameters.B_NAME.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, newToken);
            preStatement.setString(2, botName);
            updateCount = preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }

        return updateCount;
    }
}
