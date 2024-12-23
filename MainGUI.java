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
        setTitle("추천 메뉴 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(244, 246, 249)); // 밝은 회색 배경

        // 상단: 타이틀
        JLabel titleLabel = new JLabel("추천 메뉴 시스템");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(44, 62, 80)); // 텍스트 색상
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);


        // 왼쪽: 추천 조건 입력
        JPanel leftPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setBackground(new Color(236, 240, 241)); // 밝은 패널 배경

        JLabel tasteLabel = createLabel("맛:");
        String[] tastes = {"", "달콤한", "매운", "짭짤한", "담백한"};
        JComboBox<String> tasteComboBox = createComboBox(tastes);

        JLabel typeLabel = createLabel("종류:");
        String[] types = {"", "양식", "중식", "한식", "일식"};
        JComboBox<String> typeComboBox = createComboBox(types);

        JLabel priceLabel = createLabel("가격:");
        JTextField priceField = createTextField();

        JLabel excludeLabel = createLabel("제외:");
        JTextField excludeField = createTextField();

        JLabel locationLabel = createLabel("위치:");
        String[] locations = {"", "중문", "정문", "문화제조창"};
        JComboBox<String> locationComboBox = createComboBox(locations);

        JButton drawButton = createButton("추첨하기");

        JButton addMenuButton = createButton("메뉴 추가");

// 버튼 클릭 이벤트
        addMenuButton.addActionListener(e -> showAddMenuDialog(menuList));


        leftPanel.add(tasteLabel);
        leftPanel.add(tasteComboBox);
        leftPanel.add(typeLabel);
        leftPanel.add(typeComboBox);
        leftPanel.add(priceLabel);
        leftPanel.add(priceField);
        leftPanel.add(excludeLabel);
        leftPanel.add(excludeField);
        leftPanel.add(locationLabel);
        leftPanel.add(locationComboBox);

        leftPanel.add(addMenuButton);
        leftPanel.add(drawButton);

        add(leftPanel, BorderLayout.CENTER);

        // 오른쪽: 추천 랭킹
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(new Color(236, 240, 241)); // 밝은 패널 배경

        JLabel rankingLabel = new JLabel("추천 랭킹 (Top 5)");
        rankingLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        rankingLabel.setForeground(new Color(44, 62, 80)); // 텍스트 색상
        rankingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        rankingModel = new DefaultListModel<>();
        JList<String> rankingList = new JList<>(rankingModel);
        rankingList.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        rankingList.setBackground(new Color(255, 255, 255)); // 리스트 배경색
        rankingList.setForeground(new Color(44, 62, 80)); // 텍스트 색상
        rankingList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        updateRanking();

        rankingList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
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
            String location = (String) locationComboBox.getSelectedItem();

            MenuSelector selector = new MenuSelector(menuList);
            MenuData result = selector.selectRandomMenu(taste, type, price, exclude, location);

            if (result != null) {
                showRecommendationDialog(result);
            } else {
                JOptionPane.showMessageDialog(this, "조건에 맞는 메뉴가 없습니다.", "추천 결과", JOptionPane.WARNING_MESSAGE);
            }
        });

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(new Color(44, 62, 80)); // 텍스트 색상
        return label;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(44, 62, 80));
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return comboBox;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.WHITE);
        textField.setForeground(new Color(44, 62, 80));
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setCaretColor(new Color(44, 62, 80)); // 커서 색상
        return textField;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(93, 173, 226)); // 밝은 블루
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
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

    private void showAddMenuDialog(List<MenuData> menuList) {
        JDialog dialog = new JDialog(this, "새 메뉴 추가", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(8, 2, 10, 10)); // GridLayout 설정 (8행 2열)

        dialog.getContentPane().setBackground(new Color(220, 220, 220)); // 밝은 배경색

        // 입력 필드 및 라벨
        JLabel nameLabel = new JLabel("맛집 이름:");
        JTextField nameField = new JTextField();
        JLabel menuNameLabel = new JLabel("메뉴 이름:");
        JTextField menuNameField = new JTextField();
        JLabel tasteLabel = new JLabel("맛:");
        JTextField tasteField = new JTextField();
        JLabel priceLabel = new JLabel("가격:");
        JTextField priceField = new JTextField();
        JLabel typeLabel = new JLabel("종류:");
        JTextField typeField = new JTextField();
        JLabel locationLabel = new JLabel("위치:");
        JTextField locationField = new JTextField();
        JLabel imagePathLabel = new JLabel("이미지 경로:");
        JTextField imagePathField = new JTextField();

        // 컴포넌트를 다이얼로그에 추가
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(menuNameLabel);
        dialog.add(menuNameField);
        dialog.add(tasteLabel);
        dialog.add(tasteField);
        dialog.add(priceLabel);
        dialog.add(priceField);
        dialog.add(typeLabel);
        dialog.add(typeField);
        dialog.add(locationLabel);
        dialog.add(locationField);
        dialog.add(imagePathLabel);
        dialog.add(imagePathField);

        // 저장 버튼 추가
        JButton saveButton = new JButton("저장");
        saveButton.setBackground(new Color(0, 123, 255));
        saveButton.setForeground(Color.WHITE);

        // 빈 패널을 하나 추가해서 버튼을 중앙에 배치
        dialog.add(new JLabel()); // 빈 공간
        dialog.add(saveButton);

        // 저장 버튼 동작 정의
        saveButton.addActionListener(e -> {
            // 입력 데이터 가져오기
            String name = nameField.getText();
            String menuName = menuNameField.getText();
            String taste = tasteField.getText();
            String price = priceField.getText();
            String type = typeField.getText();
            String location = locationField.getText();
            String imagePath = imagePathField.getText();

            // 필수 필드 확인
            if (name.isEmpty() || menuName.isEmpty() || taste.isEmpty() || price.isEmpty() || type.isEmpty() || location.isEmpty() || imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "모든 필드를 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 텍스트 파일에 저장
            saveMenuDataToFile(name, menuName, taste, price, type, location, imagePath);

            // 성공 메시지
            JOptionPane.showMessageDialog(dialog, "메뉴가 성공적으로 추가되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);

            dialog.dispose(); // 다이얼로그 닫기
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // 텍스트 파일에 MenuData 저장
    private void saveMenuDataToFile(String name, String menuName, String taste, String price, String type, String location, String imagePath) {
        try (FileWriter writer = new FileWriter("menu_data.txt", true)) {
            // 데이터를 텍스트 형식으로 저장
            writer.write(name + "," + menuName + "," + taste + "," + price + "," + type + "," + location + "," + imagePath + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "파일 저장 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }


    // menu_data.txt에 MenuData 저장
    private void saveMenuDataToFile(MenuData menu) {
        try (FileWriter writer = new FileWriter("menu_data.txt", true)) {
            // 데이터를 텍스트 형식으로 저장
            writer.write(menu.toString() + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "파일 저장 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRecommendationDialog(MenuData menu) {
        JDialog dialog = new JDialog(this, "추천 결과", true);
        dialog.setSize(400, 500);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(236, 240, 241)); // 밝은 배경

        // 메뉴 정보 패널
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(236, 240, 241));

        // 맛집 이름
        JLabel nameLabel = new JLabel("맛집: " + menu.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setForeground(new Color(44, 62, 80));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 메뉴 이미지
        ImageIcon icon = new ImageIcon(menu.getImagePath());
        JLabel imageLabel;
        if (icon.getIconWidth() > 0) {
            Image scaledImage = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImage);
            imageLabel = new JLabel(icon);
        } else {
            imageLabel = new JLabel("[이미지 없음]");
            imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            imageLabel.setForeground(new Color(44, 62, 80));
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 메뉴 세부 정보
        JLabel menuLabel = new JLabel("메뉴: " + menu.getMenuName());
        JLabel tasteLabel = new JLabel("맛: " + menu.getTaste());
        JLabel priceLabel = new JLabel("가격: " + menu.getPrice() + "원");
        JLabel typeLabel = new JLabel("종류: " + menu.getType());
        JLabel locationLabel = new JLabel("위치: " + menu.getLocation());

        JLabel[] labels = {menuLabel, tasteLabel, priceLabel, typeLabel, locationLabel};
        for (JLabel label : labels) {
            label.setFont(new Font("SansSerif", Font.PLAIN, 14));
            label.setForeground(new Color(44, 62, 80));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // 패널에 컴포넌트 추가
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(imageLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        for (JLabel label : labels) {
            infoPanel.add(label);
            infoPanel.add(Box.createVerticalStrut(5));
        }

        dialog.add(infoPanel, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(236, 240, 241));

        JButton recommendButton = createButton("추천");
        JButton closeButton = createButton("닫기");

        // 추천 버튼 동작
        recommendButton.addActionListener(e -> {
            saveRecommendation(menu); // 추천 데이터 저장
            updateRanking(); // 랭킹 업데이트
            JOptionPane.showMessageDialog(dialog, "추천이 완료되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        // 닫기 버튼 동작
        closeButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(recommendButton);
        buttonPanel.add(closeButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }



    private void updateRanking() {
        Map<String, Integer> recommendationCounts = new HashMap<>();
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

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                recommendationCounts.put(line, recommendationCounts.getOrDefault(line, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    private void showRankingDetailDialog(String selected, List<MenuData> menuList) {
        // 추천 랭킹에서 선택된 항목의 맛집 이름과 메뉴 이름을 분리
        String[] parts = selected.split("\\(")[0].trim().split(","); // "(3회 추천)" 제거
        if (parts.length >= 2) {
            String name = parts[0].trim();
            String menuName = parts[1].trim();

            // 메뉴 리스트에서 해당 메뉴를 찾음
            for (MenuData menu : menuList) {
                if (menu.getName().equals(name) && menu.getMenuName().equals(menuName)) {
                    showRecommendationDialog(menu); // 다이얼로그 호출
                    return;
                }
            }
        }

        // 메뉴를 찾지 못한 경우
        JOptionPane.showMessageDialog(this, "해당 메뉴를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
    }



    private void saveMenuData(MenuData menu) {
        try (FileWriter fw = new FileWriter("menu_data.txt", true)) {
            fw.write(menu.getName() + "," + menu.getMenuName() + "," + menu.getTaste() + "," +
                    menu.getPrice() + "," + menu.getType() + "," + menu.getLocation() + "," +
                    menu.getImagePath() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveRecommendation(MenuData menu) {
        try (FileWriter fw = new FileWriter("recommendations.txt", true)) {
            // 맛집 이름과 메뉴 이름을 파일에 기록
            fw.write(menu.getName() + "," + menu.getMenuName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new MainGUI("menu_data.txt");
    }
}
