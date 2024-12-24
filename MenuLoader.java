import java.io.*;
import java.util.*;

/**
 * menu_data.txt 파일에서 메뉴 데이터를 로드하고 관리하는 클래스입니다.
 * 파일에서 데이터를 읽어와 프로그램에서 사용할 수 있도록 변환합니다.
 *
 * @author 사용자
 * @version 1.2
 * @since 1.0
 *
 * @created 2023-12-15
 * @lastModified 2024-12-24
 *
 * @changelog
 * <ul>
 *   <li>2024-12-21: 최초 생성</li>
 *   <li>2024-01-20: 이미지 필터 추가</li>
 *   <li>2024-12-24: 위치 필터 추가</li>
 * </ul>
 */

public class MenuLoader {
    /**
     * menu_data.txt 파일에서 데이터를 읽고 MenuData 객체 리스트로 반환합니다.
     *
     * @param filePath 읽을 파일의 경로
     * @return 메뉴 데이터를 담은 리스트
     * @throws IOException 파일 읽기 오류 시 발생
     */
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
                    String imagePath = parts[6].trim();//이미지 추가

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
