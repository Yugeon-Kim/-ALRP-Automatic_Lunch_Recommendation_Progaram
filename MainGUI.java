import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MainGUI extends JFrame {
    public static void main(String[] args) {
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

        JLabel resultLabel = new JLabel("결과가 여기에 표시됩니다.");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton drawButton = new JButton("추첨하기");
        drawButton.addActionListener(e -> {
            String taste = (String) tasteComboBox.getSelectedItem();
            String type = (String) typeComboBox.getSelectedItem();
            String price = priceField.getText();
            String exclude = excludeField.getText();

            MenuData result = selector.selectRandomMenu(taste, type, price, exclude);

            if (result != null) {
                resultLabel.setText("<html>선택된 메뉴:<br>" + result + "</html>");
            } else {
                resultLabel.setText("조건에 맞는 메뉴가 없습니다.");
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
        frame.add(resultLabel);

        frame.setVisible(true);
    }
}
