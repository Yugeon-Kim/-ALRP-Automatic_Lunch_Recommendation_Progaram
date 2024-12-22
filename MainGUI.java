import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainGUI extends JFrame {
    private DefaultListModel<String> rankingModel;

    public MainGUI(String dataFilePath) {
        // 메뉴 데이터 로드
        List<MenuData> menuList = MenuLoader.loadMenuData(dataFilePath);
        if (menuList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "메뉴 데이터를 로드할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
        JTextField priceField = new JTextField("");

        JLabel excludeLabel = new JLabel("제외:");
        JTextField excludeField = new JTextField("");

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
        updateRanking(); // 프로그램 시작 시 랭킹 업데이트

        rankingList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 더블 클릭 시 동작
                    int index = rankingList.getSelectedIndex();
                    if (index != -1) {
                        String selected = rankingModel.get(index);
                        showRankingDetailDialog(selected, menuList);
                    }
                }
            }
        });

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
                showRecommendationDialog(result); // 다이얼로그 호출
            } else {
                JOptionPane.showMessageDialog(this, "조건에 맞는 메뉴가 없습니다.", "추천 결과", JOptionPane.WARNING_MESSAGE);
            }
        });


        setVisible(true);
    }
    private void showRankingDetailDialog(String selected, List<MenuData> menuList) {
        // 추천 항목의 맛집 이름과 메뉴 이름 추출
        String[] parts = selected.split("\\(")[0].trim().split(","); // "(3회 추천)" 제거
        if (parts.length >= 2) {
            String name = parts[0].trim();
            String menuName = parts[1].trim();

            // 해당 메뉴를 찾아 다이얼로그 표시
            for (MenuData menu : menuList) {
                if (menu.getName().equals(name) && menu.getMenuName().equals(menuName)) {
                    showRecommendationDialog(menu);
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "해당 메뉴를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
    }



    // 다이얼로그 표시
    private void showRecommendationDialog(MenuData menu) {
        JDialog dialog = new JDialog(this, "추천 결과", true);
        dialog.setSize(400, 350);
        dialog.setLayout(new BorderLayout());

        // 메뉴 정보 패널
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        // 첫 줄: 맛집 이름
        JLabel nameLabel = new JLabel(menu.getName());
        nameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 두 번째 줄: 메뉴 사진
        ImageIcon icon = new ImageIcon(menu.getImagePath());
        if (icon.getIconWidth() > 0) {
            Image image = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);
        } else {
            icon = new ImageIcon(); // 이미지 로드 실패 시 기본 빈 아이콘
        }
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 세부 정보
        JLabel menuLabel = new JLabel("메뉴: " + menu.getMenuName());
        JLabel tasteLabel = new JLabel("맛: " + menu.getTaste());
        JLabel priceLabel = new JLabel("가격: " + menu.getPrice() + "원");
        JLabel typeLabel = new JLabel("종류: " + menu.getType());

        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tasteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 패널에 컴포넌트 추가
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(10)); // 간격
        infoPanel.add(imageLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(menuLabel);
        infoPanel.add(tasteLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(typeLabel);

        dialog.add(infoPanel, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        JButton recommendButton = new JButton("추천");
        JButton closeButton = new JButton("뒤로");

        // 추천 버튼 동작
        recommendButton.addActionListener(e -> {
            saveRecommendation(menu);
            updateRanking(); // 추천 후 랭킹 업데이트
            dialog.dispose();
        });

        // 뒤로 버튼 동작
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

        // 파일 존재 여부 확인 및 생성
        File file = new File("recommendations.txt");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("recommendations.txt 파일이 생성되었습니다.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 추천 파일 읽기
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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
        new MainGUI("menu_data.txt");
    }
}
