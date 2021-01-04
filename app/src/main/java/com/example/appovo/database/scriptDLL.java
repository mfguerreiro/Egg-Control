package com.example.appovo.database;

public class scriptDLL {
    public static String getCreateTableEstoque(){
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE IF NOT EXISTS ESTOQUE(\n" +
                "    LOTE       INTEGER NOT NULL\n" +
                "                       PRIMARY KEY AUTOINCREMENT,\n" +
                "    QUANTIDADE INTEGER NOT NULL,\n" +
                "    CUSTO      DOUBLE  NOT NULL,\n" +
                "    VENDA      DOUBLE  NOT NULL,\n" +
                "    DATA       DATE    NOT NULL,\n" +
                "    TIPO       INT     REFERENCES TIPO (COD_TIPO) \n" +
                ");\n"
                );
        return sql.toString();
    }
    public static String getCreateTableVendas(){
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE VENDAS (\n" +
                "    COD_VENDA  INTEGER NOT NULL ON CONFLICT ROLLBACK\n" +
                "                       PRIMARY KEY AUTOINCREMENT,\n" +
                "    QUANTIDADE INT     NOT NULL,\n" +
                "    DATA       DATE    NOT NULL,\n" +
                "    LOTE       INT     REFERENCES ESTOQUE (LOTE) \n" +
                "                       NOT NULL,\n" +
                "    ROTA       INT     REFERENCES ROTA (COD_ROTA) \n" +
                "                       NOT NULL\n" +
                ");\n");

        return sql.toString();
    }

    public static String getCreateTableTipo(){
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE TIPO (" );
        sql.append("    COD_TIPO  INTEGER PRIMARY KEY AUTOINCREMENT");
        sql.append("                      NOT NULL,");
        sql.append("    DESCRICAO STRING  NOT NULL);");

        return sql.toString();
    }

    public static String getCreateTableRota(){
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ROTA (" );
        sql.append("    COD_ROTA  INTEGER PRIMARY KEY AUTOINCREMENT");
        sql.append("                      NOT NULL,");
        sql.append("    DESCRICAO STRING  NOT NULL);");

        return sql.toString();
    }
}
