import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * GUI 구성 요소를 생성하는 유틸리티 클래스입니다.
 *
 * @author Yugeon-Kim
 * @version 1.0
 * @since 2024-12-24
 */
public class ComponentFactory {

    /**
     * 텍스트와 스타일이 지정된 JLabel을 생성합니다.
     *
     * @param text 라벨에 표시할 텍스트
     * @return 스타일이 적용된 JLabel
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(new Color(44, 62, 80)); // 텍스트 색상
        return label;
    }

    /**
     * 스타일이 지정된 JComboBox를 생성합니다.
     *
     * @param items 콤보박스에 표시할 아이템 배열
     * @return 스타일이 적용된 JComboBox
     */
    public static JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(44, 62, 80));
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return comboBox;
    }

    /**
     * 스타일이 지정된 JTextField를 생성합니다.
     *
     * @return 스타일이 적용된 JTextField
     */
    public static JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.WHITE);
        textField.setForeground(new Color(44, 62, 80));
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setCaretColor(new Color(44, 62, 80)); // 커서 색상
        return textField;
    }

    /**
     * 동적 스타일이 적용된 JButton을 생성합니다.
     *
     * @param text 버튼에 표시할 텍스트
     * @return 스타일이 적용된 JButton
     */
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(93, 173, 226)); // 기본 버튼 색상
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        // 마우스 호버 효과
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(52, 152, 219)); // 호버 색상
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(93, 173, 226)); // 기본 색상
            }
        });
        return button;
    }
}
