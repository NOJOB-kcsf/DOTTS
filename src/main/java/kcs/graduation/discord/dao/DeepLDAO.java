package kcs.graduation.discord.dao;

import kcs.graduation.discord.dao.core.BaseDAO;
import kcs.graduation.discord.parameter.BotDataParameters;
import kcs.graduation.discord.parameter.DAOParameters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeepLDAO extends BaseDAO {

    public String getDeepLToken(String BotName) {
        this.open();
        String token = "";
        PreparedStatement preStatement = null;
        try {
            String sql = "SELECT " + BotDataParameters.D_TOKEN.getParam() + " FROM " + DAOParameters.TABLE_BOT_DATA.getParam() + " WHERE " + BotDataParameters.B_NAME.getParam() + " = ?";
            preStatement = con.prepareStatement(sql);
            preStatement.setString(1, BotName);
            ResultSet rs = preStatement.executeQuery();

            while (rs.next()) token = rs.getString(BotDataParameters.D_TOKEN.getParam());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(preStatement);
        }
        return token;
    }
}
