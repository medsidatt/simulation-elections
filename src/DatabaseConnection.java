import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/election_db?useSSL=false&serverTimezone=UTC";
    private final String username = "root"; 
    private final String password = ""; 

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion à MySQL établie.");
        } catch (ClassNotFoundException e) {
            System.out.println("Pilote MySQL non trouvé. Ajoutez mysql-connector-java.jar au classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à MySQL : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connexion à MySQL rétablie.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à MySQL fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}