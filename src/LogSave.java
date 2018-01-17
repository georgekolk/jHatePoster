import java.sql.*;

public class LogSave {
    private Connection conn;
    private Statement statmt;
    private String dbPathAndName;
    private String dbTableName;

    public LogSave(String dbPathAndName, String dbTableName){
        this.dbPathAndName = dbPathAndName;
        this.dbTableName = dbTableName;
    }

    public void conn() throws ClassNotFoundException, SQLException{
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbPathAndName);

        //System.out.println("База " + dbPathAndName + " подключена!");
    }

    public void CreateDB() throws ClassNotFoundException, SQLException{
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists '" + dbTableName + "' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'event' TEXT, 'result' TEXT, 'message' TEXT, 'timestamp' DATETIME DEFAULT CURRENT_TIMESTAMP);");

        //System.out.println("Таблица " + dbTableName + " создана или уже существует.");
    }

    public void WriteEventToDB(String event, String result, String message) throws SQLException{
        statmt.execute("INSERT INTO '" + dbTableName + "' ('event', 'result', 'message', 'timestamp') VALUES ('" + event + "', '" + result + "', '" + message.replaceAll("[-'^,]","") + "', DateTime('now', 'localtime')); ");
    }

    public void CloseDB() throws ClassNotFoundException, SQLException{
        statmt.close();
        conn.close();

        //System.out.println("База и сеединения с ней закрыты.");
    }
}