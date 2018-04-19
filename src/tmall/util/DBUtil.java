package tmall.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil 
{
	private static String ip = "localhost";
	private static String encode = "UTF-8";
	private static String database = "tmall";
	private static int port = 3306;
	private static String loginName = "root";
	private static String password = "";
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException 
	{
		String url = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=%s", ip, port, database, encode);
		return DriverManager.getConnection(url, loginName, password);
	}

	public static void copyFile(InputStream is, File file)
    {
        try {
            if (is != null && is.available() != 0) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte b[] = new byte[1024 * 1024];
                    int length = 0;
                    while ((length = is.read(b)) != -1) {
                        fos.write(b, 0, length);
                    }
                    fos.flush();

                    BufferedImage img = ImageUtil.change2JPG(file);
                    ImageIO.write(img, "jpg", file);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) throws SQLException {
		System.out.println(getConnection());
	}
}
