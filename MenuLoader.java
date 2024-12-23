import java.io.*;
import java.util.*;

public class MenuLoader {
    public static List<MenuData> loadMenuData(String filePath) {
        List<MenuData> menuList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) { // 7개의 필드 처리
                    String name = parts[0].trim();
                    String menuName = parts[1].trim();
                    String taste = parts[2].trim();
                    String price = parts[3].trim();
                    String type = parts[4].trim();
                    String location = parts[5].trim(); // 위치 추가
                    String imagePath = parts[6].trim();

                    menuList.add(new MenuData(name, menuName, taste, price, type, location, imagePath));
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
