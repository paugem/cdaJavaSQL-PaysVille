package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyConnection {
	private static final Logger logger = LoggerFactory.getLogger("data.MyConnection");
	private static Connection connexion = null;

	private MyConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Properties appProps = new Properties();
			appProps.load(MyConnection.class.getResourceAsStream("/db.properties"));
			connexion = DriverManager.getConnection(appProps.getProperty("url"),
													appProps.getProperty("username"),
													appProps.getProperty("password"));
		}
		catch (Exception e) {
			logger.error("erreur : "+e);
			System.out.println("Connexion à la BDD échouée");
		}
	}

	public static Connection getConnection() {
		if (connexion == null) {
			new MyConnection();
		}
		return connexion;
	}

	public static void stop() {
		if (connexion != null) {
			try {
				connexion.close();
			} catch (SQLException e) {
				logger.error("erreur : "+e);
				e.printStackTrace();
			}
		}
	}
}
