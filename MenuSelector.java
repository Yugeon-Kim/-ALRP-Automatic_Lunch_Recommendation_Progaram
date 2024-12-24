import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 사용자가 입력한 조건에 따라 메뉴 데이터를 필터링하고, 무작위로 메뉴를 선택하는 클래스입니다.
 *
 * @author Yugeon-Kim
 * @version 1.2
 * @since 1.0
 *
 * @created 2024-12-19
 * @lastModified 2024-12-24
 *
 * @changelog
 * <ul>
 *   <li>2023-12-19: 최초 생성</li>
 *   <li>2024-12-23: 위치 정보 필터링 추가</li>
 * </ul>
 */
public class MenuSelector {

    private List<MenuData> menuList;

    /**
     * MenuSelector 객체를 생성합니다.
     *
     * @param menuList 메뉴 데이터를 포함하는 리스트
     */
    public MenuSelector(List<MenuData> menuList) {
        this.menuList = menuList;
    }

    /**
     * 주어진 조건에 따라 메뉴 데이터를 필터링하고, 무작위로 하나의 메뉴를 선택합니다.
     * 모든 조건이 비어 있으면, 전체 메뉴 데이터 중에서 무작위로 선택합니다.
     *
     * @param taste   사용자가 선택한 맛 (예: "달콤한", null 또는 빈 값 허용)
     * @param type    사용자가 선택한 종류 (예: "한식", null 또는 빈 값 허용)
     * @param price   사용자가 입력한 최대 가격 (예: "10000", null 또는 빈 값 허용)
     * @param exclude 제외할 메뉴 이름 또는 키워드 (예: "김치", null 또는 빈 값 허용)
     * @param location 사용자가 선택한 위치 (예: "중문", null 또는 빈 값 허용)
     * @return 조건에 맞는 MenuData 객체, 조건에 맞는 메뉴가 없으면 null 반환
     */
    public MenuData selectRandomMenu(String taste, String type, String price, String exclude, String location) {
        boolean allEmpty = (taste == null || taste.isEmpty()) &&
                (type == null || type.isEmpty()) &&
                (price == null || price.isEmpty()) &&
                (exclude == null || exclude.isEmpty()) &&
                (location == null || location.isEmpty()); // 위치 조건 추가

        if (allEmpty) {
            // 모든 조건이 비어 있으면 전체 메뉴에서 무작위로 선택
            Random random = new Random();
            return menuList.get(random.nextInt(menuList.size()));
        }

        // 조건에 따라 메뉴를 필터링
        List<MenuData> filteredMenus = menuList.stream()
                .filter(menu -> (taste == null || taste.isEmpty() || menu.getTaste().equalsIgnoreCase(taste)) &&
                        (type == null || type.isEmpty() || menu.getType().equalsIgnoreCase(type)) &&
                        (price == null || price.isEmpty() || isPriceInRange(menu.getPrice(), price)) &&
                        (exclude == null || exclude.isEmpty() || !menu.getMenuName().contains(exclude)) &&
                        (location == null || location.isEmpty() || menu.getLocation().equalsIgnoreCase(location))) // 위치 필터링 추가
                .collect(Collectors.toList());

        if (filteredMenus.isEmpty()) {
            return null; // 조건에 맞는 메뉴가 없으면 null 반환
        }

        // 필터링된 메뉴에서 무작위로 선택
        Random random = new Random();
        return filteredMenus.get(random.nextInt(filteredMenus.size()));
    }

    /**
     * 메뉴의 가격이 사용자가 입력한 최대 가격 범위 내에 포함되는지 확인합니다.
     *
     * @param menuPrice 메뉴 데이터에 저장된 가격 (예: "8000")
     * @param userPriceRange 사용자가 입력한 최대 가격 (예: "10000")
     * @return 메뉴 가격이 사용자 가격 이하이면 true, 그렇지 않으면 false
     */
    private boolean isPriceInRange(String menuPrice, String userPriceRange) {
        try {
            // 숫자만 추출하여 비교
            int menuPriceValue = Integer.parseInt(menuPrice.replaceAll("[^0-9]", ""));
            int userPriceValue = Integer.parseInt(userPriceRange.replaceAll("[^0-9]", ""));
            return menuPriceValue <= userPriceValue;
        } catch (Exception e) {
            return false; // 형식 오류 시 false 반환
        }
    }
}
