import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MainGUI extends JFrame {
    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("취향 선택기");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2, 10, 10));

        // 맛 콤보박스
        JLabel tasteLabel = new JLabel("맛:");
        String[] tastes = {"달콤한", "매운", "짭짤한", "담백한"};
        JComboBox<String> tasteComboBox = new JComboBox<>(tastes);

        // 종류 콤보박스
        JLabel typeLabel = new JLabel("종류:");
        String[] types = {"양식", "중식", "한식", "일식"};
        JComboBox<String> typeComboBox = new JComboBox<>(types);

        // 가격 입력 필드
        JLabel priceLabel = new JLabel("가격:");
        JTextField priceField = new JTextField("5000~15000");

        // 제외 항목 입력 필드
        JLabel excludeLabel = new JLabel("제외:");
        JTextField excludeField = new JTextField();

        // 결과 표시 라벨
        JLabel resultLabel = new JLabel("결과가 여기에 표시됩니다.");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 추첨 버튼
        JButton drawButton = new JButton("추첨하기");
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String taste = (String) tasteComboBox.getSelectedItem();
                String type = (String) typeComboBox.getSelectedItem();
                String price = priceField.getText();
                String exclude = excludeField.getText();

                // 임의 메뉴 생성
                String[] menus = {"메뉴1", "메뉴2", "메뉴3", "메뉴4"};
                Random random = new Random();
                String selectedMenu = menus[random.nextInt(menus.length)];

                // 결과 업데이트
                resultLabel.setText("<html>맛: " + taste + "<br>종류: " + type +
                        "<br>가격: " + price + "<br>제외: " + exclude +
                        "<br>선택된 메뉴: " + selectedMenu + "</html>");
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
        frame.add(new JLabel());  // 빈 공간
        frame.add(drawButton);
        frame.add(resultLabel);

        // 프레임 표시
        frame.setVisible(true);
    }
}
