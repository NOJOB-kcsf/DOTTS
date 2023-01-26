package kcs.graduation.processing;

import java.util.List;

public class Db {
    public String SQL_Statement_Replacement(String sql, List<Object> repress) {
        char[] disassembling = sql.toCharArray();
        StringBuilder sql_data = new StringBuilder();
        if (repress.size() != 0) {
            int i = 0;
            for (char object : disassembling) {
                if (object == '?') {
                    sql_data.append(repress.get(i).toString());
                    i++;
                } else {
                    sql_data.append(object);
                }
            }
        }
        return sql_data.toString();
    }
}
