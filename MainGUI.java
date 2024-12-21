import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainGUI extends JFrame {
 static void showResultDialog(String string, String imagePath) {
    }    public static void main(String[] args) {
        // 메뉴 데이터 로드
        String filePath = "menu_data.txt"; // 파일 경로
        List<MenuData> menuList = MenuLoader.loadMenuData(filePath);
        if (menuList.isEmpty()) {
            System.out.println("메뉴 데이터를 로드할 수 없습니다. 프로그램을 종료합니다.");
            return;
        }

        MenuSelector selector = new MenuSelector(menuList);

        // GUI 구성
        JFrame frame = new JFrame("취향 선택기");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2, 10, 10));

        // 컴포넌트 생성
        JLabel tasteLabel = new JLabel("맛:");
        String[] tastes = {"", "달콤한", "매운", "짭짤한", "담백한"};
        JComboBox<String> tasteComboBox = new JComboBox<>(tastes);

        JLabel typeLabel = new JLabel("종류:");
        String[] types = {"", "양식", "중식", "한식", "일식"};
        JComboBox<String> typeComboBox = new JComboBox<>(types);

        JLabel priceLabel = new JLabel("가격:");
        JTextField priceField = new JTextField("12000");

        JLabel excludeLabel = new JLabel("제외:");
        JTextField excludeField = new JTextField();

        JButton drawButton = new JButton("추첨하기");
        drawButton.addActionListener(e -> {
            String taste = (String) tasteComboBox.getSelectedItem();
            String type = (String) typeComboBox.getSelectedItem();
            String price = priceField.getText();
            String exclude = excludeField.getText();

            MenuData result = selector.selectRandomMenu(taste, type, price, exclude);

            if (result != null) {
                showResultDialog(result);
            } else {
                JOptionPane.showMessageDialog(null, "조건에 맞는 메뉴가 없습니다.", "추천 메뉴", JOptionPane.WARNING_MESSAGE);
            }
        });



        // 프레임에 컴포넌트 추가
        frame.add(tasteLabel);
        frame.add(tasteComboBox);
        frame.add(typeLabel);
        frame.add(typeComboBox);
        frame.add(priceLabel);
        frame.add(priceField);
        frame.add(excludeLabel);
        frame.add(excludeField);
        frame.add(new JLabel()); // 빈 공간
        frame.add(drawButton);

        frame.setVisible(true);
    }



    private static void showResultDialog(MenuData menu) {
        // 이미지 로드
        ImageIcon icon = new ImageIcon(menu.getImagePath());
        Image image = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image);

        // 다이얼로그 패널 생성
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 첫 줄: 맛집 이름
        JLabel nameLabel = new JLabel(menu.getName());
        nameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 두 번째 줄: 사진
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 세부 정보 추가
        JLabel tasteLabel = new JLabel("맛: " + menu.getTaste());
        JLabel priceLabel = new JLabel("가격: " + menu.getPrice() + "원");
        JLabel typeLabel = new JLabel("종류: " + menu.getType());
        JLabel locationLabel = new JLabel("위치: " + menu.getMenuName());

        tasteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 패널에 컴포넌트 추가
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(10)); // 간격
        panel.add(imageLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(tasteLabel);
        panel.add(priceLabel);
        panel.add(typeLabel);
        panel.add(locationLabel);

        // 다이얼로그 생성 및 표시
        JOptionPane.showMessageDialog(null, panel, "추천 메뉴", JOptionPane.PLAIN_MESSAGE);
    }


}
