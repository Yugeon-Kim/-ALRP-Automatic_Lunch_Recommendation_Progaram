import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainGUI extends JFrame {
    private DefaultListModel<String> rankingModel;

    public MainGUI(List<MenuData> menuList) {
        // 메인 프레임 설정
        setTitle("추천 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout(10, 10));

        // 왼쪽: 추천 조건 입력
        JPanel leftPanel = new JPanel(new GridLayout(6, 2, 10, 10));
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

        leftPanel.add(tasteLabel);
        leftPanel.add(tasteComboBox);
        leftPanel.add(typeLabel);
        leftPanel.add(typeComboBox);
        leftPanel.add(priceLabel);
        leftPanel.add(priceField);
        leftPanel.add(excludeLabel);
        leftPanel.add(excludeField);
        leftPanel.add(new JLabel());
        leftPanel.add(drawButton);

        add(leftPanel, BorderLayout.CENTER);

        // 오른쪽: 추천 랭킹
        JPanel rightPanel = new JPanel(new BorderLayout());
        JLabel rankingLabel = new JLabel("추천 랭킹 (Top 5)");
        rankingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        rankingModel = new DefaultListModel<>();
        JList<String> rankingList = new JList<>(rankingModel);
        updateRanking();

        rightPanel.add(rankingLabel, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(rankingList), BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // 추천 버튼 이벤트
        drawButton.addActionListener(e -> {
            String taste = (String) tasteComboBox.getSelectedItem();
            String type = (String) typeComboBox.getSelectedItem();
            String price = priceField.getText();
            String exclude = excludeField.getText();

            // 메뉴 선택 로직
            MenuSelector selector = new MenuSelector(menuList);
            MenuData result = selector.selectRandomMenu(taste, type, price, exclude);

            if (result != null) {
                showRecommendationDialog(result);
            } else {
                JOptionPane.showMessageDialog(this, "조건에 맞는 메뉴가 없습니다.", "추천 결과", JOptionPane.WARNING_MESSAGE);
            }
        });

        setVisible(true);
    }

    // 다이얼로그 표시
    private void showRecommendationDialog(MenuData menu) {
        JDialog dialog = new JDialog(this, "추천 결과", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        // 메뉴 정보 표시
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel("맛집: " + menu.getName());
        JLabel menuLabel = new JLabel("메뉴: " + menu.getMenuName());
        JLabel tasteLabel = new JLabel("맛: " + menu.getTaste());
        JLabel priceLabel = new JLabel("가격: " + menu.getPrice() + "원");
        JLabel typeLabel = new JLabel("종류: " + menu.getType());

        infoPanel.add(nameLabel);
        infoPanel.add(menuLabel);
        infoPanel.add(tasteLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(typeLabel);

        dialog.add(infoPanel, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        JButton recommendButton = new JButton("추천");
        JButton closeButton = new JButton("뒤로");

        recommendButton.addActionListener(e -> {
            saveRecommendation(menu);
            updateRanking();
            dialog.dispose();
        });

        closeButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(recommendButton);
        buttonPanel.add(closeButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // 추천 기록 저장
    private void saveRecommendation(MenuData menu) {
        try (FileWriter fw = new FileWriter("recommendations.txt", true)) {
            fw.write(menu.getName() + "," + menu.getMenuName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 추천 랭킹 업데이트
    private void updateRanking() {
        Map<String, Integer> recommendationCounts = new HashMap<>();

        // 추천 파일 읽기
        try (BufferedReader br = new BufferedReader(new FileReader("recommendations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                recommendationCounts.put(line, recommendationCounts.getOrDefault(line, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 랭킹 정렬 및 출력
        List<Map.Entry<String, Integer>> sortedList = recommendationCounts.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .collect(Collectors.toList());

        rankingModel.clear();
        for (Map.Entry<String, Integer> entry : sortedList) {
            rankingModel.addElement(entry.getKey() + " (" + entry.getValue() + "회 추천)");
        }
    }

    public static void main(String[] args) {
        List<MenuData> menuList = Arrays.asList(
                new MenuData("맛집A", "김치찌개", "매운", "7000", "한식", "kimchi.jpg"),
                new MenuData("맛집B", "파스타", "달콤한", "12000", "양식", "pasta.jpg"),
                new MenuData("맛집C", "초밥", "담백한", "15000", "일식", "sushi.jpg"),
                new MenuData("맛집D", "짜장면", "짭짤한", "8000", "중식", "jajang.jpg")
        );



        new MainGUI(menuList);
    }
}
