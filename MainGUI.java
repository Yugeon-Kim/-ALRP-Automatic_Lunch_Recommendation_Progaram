import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MainGUI extends JFrame {
    public static void main(String[] args) {
        // 테스트 데이터
        List<MenuData> menuList = Arrays.asList(
                new MenuData("맛집A", "김치찌개", "매운", "7000", "한식"),
                new MenuData("맛집B", "파스타", "달콤한", "12000", "양식"),
                new MenuData("맛집C", "초밥", "담백한", "15000", "일식"),
                new MenuData("맛집D", "짜장면", "짭짤한", "8000", "중식")
        );

        MenuSelector selector = new MenuSelector(menuList);

        // 프레임 생성
        JFrame frame = new JFrame("취향 선택기");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2, 10, 10));

        // GUI 컴포넌트
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
