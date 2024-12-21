import java.io.*;
import java.util.*;

public class MenuLoader {
    public static List<MenuData> loadMenuData(String filePath) {
        List<MenuData> menuList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) { // 데이터가 5개의 필드를 포함해야 함
                    String name = parts[0].trim();
                    String menuName = parts[1].trim();
                    String taste = parts[2].trim();
                    String price = parts[3].trim();
                    String type = parts[4].trim();
                    menuList.add(new MenuData(name, menuName, taste, price, type));
                } else {
                    System.out.println("잘못된 데이터 형식: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menuList;
    }
}
