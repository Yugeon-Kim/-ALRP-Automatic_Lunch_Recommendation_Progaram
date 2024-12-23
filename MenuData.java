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
