import java.sql.SQLException;

public class PGC {
    static DB db = new DB();

    public static void main(String[] args) throws SQLException{
        System.out.print("Heloooo ");

        System.out.println(DB.GetUserNameByID(1));
    }
}
