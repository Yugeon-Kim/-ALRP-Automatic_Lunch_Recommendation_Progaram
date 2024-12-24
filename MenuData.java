/**
 * 메뉴 데이터를 나타내는 클래스입니다.
 * 한 개의 메뉴 정보를 저장하며, 메뉴 추천 및 필터링에 사용됩니다.
 *
 * @author Yugeon-Kim
 * @version 1.3
 * @since 1.0
 *
 * @created 2024-12-19
 * @lastModified 2024-12-24
 *
 * @changelog
 * <ul>
 *   <li>2023-12-19: 최초 생성</li>
 *   <li>2024-12-21: 이미지 정보 필드 추가</li>
 *   <li>2024-12-23: 위치 정보 필드 추가</li>
 * </ul>
 */
class MenuData {
    private String name;
    private String menuName;
    private String taste;
    private String price;
    private String type;
    private String location; // 위치 정보 추가
    private String imagePath;

    public MenuData(String name, String menuName, String taste, String price, String type, String location, String imagePath) {
        this.name = name;
        this.menuName = menuName;
        this.taste = taste;
        this.price = price;
        this.type = type;
        this.location = location;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public String getMenuName() { return menuName; }
    public String getTaste() { return taste; }
    public String getPrice() { return price; }
    public String getType() { return type; }
    public String getLocation() { return location; } // Getter 추가
    public String getImagePath() { return imagePath; }
}
