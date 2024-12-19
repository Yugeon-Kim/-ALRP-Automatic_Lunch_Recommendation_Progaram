class MenuData {
    private String name;
    private String menuName;
    private String taste;
    private String price;
    private String type;

    public MenuData(String name, String menuName, String taste, String price, String type) {
        this.name = name;
        this.menuName = menuName;
        this.taste = taste;
        this.price = price;
        this.type = type;
    }

    public String getName() { return name; }
    public String getMenuName() { return menuName; }
    public String getTaste() { return taste; }
    public String getPrice() { return price; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return name + " - " + menuName + " (" + taste + ", " + price + "원, " + type + ")";
    }
}