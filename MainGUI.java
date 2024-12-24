import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 메인 GUI를 생성하고 사용자와의 상호작용을 관리하는 클래스입니다.
 * 사용자는 메뉴 데이터를 필터링하고 추천받을 수 있으며, 데이터를 추가하거나 관리할 수 있습니다.
 *
 * @author Yugeon-Kim
 * @version 1.6
 * @since 1.0
 *
 * @created 2024-11-25
 * @lastModified 2024-12-24
 *
 * @changelog
 * <ul>
 *   <li>2024-11-25: 최초 생성</li>
 *   <li>2024-12-19: MenuData와 MenuSelector와 연결 및 수정</li>
 *   <li>2024-12-19: MenuLoader과 연결 및 데이터를 읽어오는 방식 변경 </li>
 *   <li>2024-12-21: 다이얼로그 추가 및 레이아웃 업데이트</li>
 *   <li>2024-12-21: 추천 랭킹 추가</li>
 *   <li>2024-12-22: 추천 랭킹 다이얼로그 개선</li>
 *   <li>2024-12-23: 위치 필터 추가 및 디자인 개선</li>
 * </ul>
 *
 */
public class MainGUI extends JFrame {

    /**
     * 메인 GUI를 초기화하고 구성 요소를 설정합니다.
     * 기본 레이아웃과 스타일을 적용합니다.
     * @param dataFilePath 메뉴 데이터를 로드할 파일 경로
     */

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
        JLabel titleLabel = ComponentFactory.createLabel("추천 메뉴 시스템");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24)); // 타이틀 폰트 조정
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 왼쪽: 추천 조건 입력
        JPanel leftPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setBackground(new Color(236, 240, 241));

        JLabel tasteLabel = ComponentFactory.createLabel("맛:");
        JComboBox<String> tasteComboBox = ComponentFactory.createComboBox(new String[]{"", "달콤한", "매운", "짭짤한", "담백한"});

        JLabel typeLabel = ComponentFactory.createLabel("종류:");
        JComboBox<String> typeComboBox = ComponentFactory.createComboBox(new String[]{"", "양식", "중식", "한식", "일식"});

        JLabel priceLabel = ComponentFactory.createLabel("가격:");
        JTextField priceField = ComponentFactory.createTextField();

        JLabel excludeLabel = ComponentFactory.createLabel("제외:");
        JTextField excludeField = ComponentFactory.createTextField();

        JLabel locationLabel = ComponentFactory.createLabel("위치:");
        JComboBox<String> locationComboBox = ComponentFactory.createComboBox(new String[]{"", "중문", "정문", "문화제조창"});

        JButton drawButton = ComponentFactory.createButton("추첨하기");
        JButton addMenuButton = ComponentFactory.createButton("메뉴 추가");

        // 이벤트 설정
        addMenuButton.addActionListener(e -> showAddMenuDialog(menuList));

        // 왼쪽 패널에 추가
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

    /**
     * 새 메뉴를 추가하기 위한 다이얼로그를 표시합니다.
     * 사용자는 새로운 메뉴 데이터를 입력하고 저장할 수 있습니다.
     *
     * @created 2024-12-23
     *
     * @param menuList 현재 로드된 메뉴 데이터 목록
     */
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

    /**
     * 주어진 데이터를 menu_data.txt 파일에 저장합니다.
     * 파일이 존재하지 않으면 생성됩니다.
     *
     * @created 2024-12-23
     *
     * @param name 맛집 이름
     * @param menuName 메뉴 이름
     * @param taste 메뉴의 맛
     * @param price 메뉴의 가격
     * @param type 메뉴의 종류
     * @param location 메뉴의 위치
     * @param imagePath 메뉴 이미지의 파일 경로
     */

    private void saveMenuDataToFile(String name, String menuName, String taste, String price, String type, String location, String imagePath) {
        try (FileWriter writer = new FileWriter("menu_data.txt", true)) {
            // 데이터를 텍스트 형식으로 저장
            writer.write(name + "," + menuName + "," + taste + "," + price + "," + type + "," + location + "," + imagePath + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "파일 저장 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 추천 메뉴를 선택하고 다이얼로그에 결과를 표시합니다.
     *
     * @created 2024-12-22
     *
     * @param  menu 사용자가 선택한 맛,종류,가격,위치 필터
     */

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

        JButton recommendButton =ComponentFactory.createButton("추천");
        JButton closeButton = ComponentFactory.createButton("닫기");

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

    /**
     * 추천 랭킹을 업데이트하여 UI에 반영합니다.
     * recommendations.txt 파일에서 데이터를 읽어와 정렬한 후 상위 5개를 표시합니다.
     *
     * @created 2024-12-21
     *
     *
     */

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

    /**
     * 추천 랭킹에서 선택된 메뉴의 세부 정보를 보여주는 다이얼로그를 표시합니다.
     *
     * @created 2024-12-22
     *
     * @param selected 선택된 랭킹 항목 (맛집 이름과 메뉴 이름 포함)
     * @param menuList 현재 로드된 메뉴 데이터 목록
     */

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


    /**
     * 추천된 메뉴 데이터를 recommendations.txt 파일에 저장합니다.
     * @created 2024-12-22
     * @param menu 추천된 메뉴 데이터
     */

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
