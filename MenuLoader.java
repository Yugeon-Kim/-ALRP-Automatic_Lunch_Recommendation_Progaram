import java.io.*;
import java.util.*;

public class MenuLoader {
    public static List<MenuData> loadMenuData(String filePath) {
        List<MenuData> menuList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String name = parts[0].trim();
                    String menuName = parts[1].trim();
                    String taste = parts[2].trim();
                    String price = parts[3].trim();
                    String type = parts[4].trim();
                    String imagePath = parts[5].trim();
                    menuList.add(new MenuData(name, menuName, taste, price, type, imagePath));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menuList;
    }
}
