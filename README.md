# 자동 메뉴 추천 프로그램

## 작성자:2021011983 김유건

## 1.개요

+ 메인화면

![메인 화면](https://github.com/user-attachments/assets/c7a78e02-8a6c-4978-9a7f-0271a1ee39d1)



+ 추첨 항목에 없을때
  
![image](https://github.com/user-attachments/assets/5a1a8185-bfa5-4d2b-9ecd-536308773fea)



### 1.1.목적

+ 배달의민족의 랜덤 음식 추첨 기능을 참고하여, 사용자에게 맞춤형 필터링을 통해 원하는 메뉴를 추천받는 기능을 구현하고자 했습니다.
+ 사용자 간 추천 랭킹과 다른 사용자와의 상호작용(미구현)을 통해, 다양한 맛집을 발견하고 리뷰를 확인할 수 있는 플랫폼을 목표로 했습니다.

### 1.2.대상 

+ 결정장애가 있는 사람: 선택 과정에서 시간이 많이 소요되거나 결정을 내리기 어려운 사람.

+ 새로운 경험을 원하는 사람: 기존에 자주 가던 곳 대신 새로운 맛집을 탐험하고 싶은 사람.

+랜덤 추첨의 재미를 느끼는 사람: 무작위로 선택된 결과에 재미를 느끼는 사용자.
 
## 2.프로그램의 중요성 및 필요성
---
### 2.1 중요성

1)사용자 선택의 편의성 제공

- 현대인은 수많은 선택지 속에서 결정을 내려야 하는 "선택 피로"를 겪고 있습니다. 이 프로그램은 사용자가 음식점을 선택하는 과정에서 겪는 시간 소모와 스트레스를 줄여줍니다.

2)재미와 기대감 제공

- 필터링된 결과를 기반으로 랜덤 메뉴를 추천함으로써, 사용자는 평소 가지 않던 새로운 맛집이나 메뉴를 시도할 기회를 얻고, 선택 과정에서 흥미를 느낄 수 있습니다.


3)사용자 참여 유도

- 추천 랭킹과 사용자 리뷰(미구현)를 추가하면, 사용자 간의 상호작용이 가능하고, 추천된 맛집의 신뢰도를 높일 수 있습니다.

### 2.2 필요성

1)결정 시간을 단축

- 매일 "어디서 먹을까?", "뭘 먹을까?"라는 질문에 시간을 낭비하는 사람들에게 빠르고 효율적인 선택을 제공합니다.

2)다양한 맛집 경험 기회 제공

- 특정 지역이나 선호하는 맛에 한정되지 않고, 필터를 통해 다양한 맛집을 탐색할 기회를 제공합니다

3)맞춤형 추천 서비스 제공

- 사용자의 맛, 종류, 가격, 위치 등 여러 조건을 고려한 추천을 통해 사용자에게 더 개인화된 경험을 제공합니다.

## 3.프로그램 수행절차 
---
### 3.1 다이어그램

![image](https://github.com/user-attachments/assets/209dd659-7a1e-4ddd-8ac0-299697afa30e)

### 3.2 클래스 다이어그램

![image](https://github.com/user-attachments/assets/42f88771-2828-4028-83bc-8d4c0ae5a57a)


### 3.3 절차 설명

+ 1) 추천 메뉴
  - 사용자가 조건(맛, 종류, 가격, 제외 키워드, 위치)을 입력
  - 조건건에 맞는 메뉴를 필터링하고, 결과 중 하나를 랜덤으로 선택
  - 추천 메뉴의 상세 정보가 다이얼로그에 표시됨:
    - 맛집 이름, 메뉴 이름, 맛, 가격, 종류, 위치, 이미지
  - 추첨 화면
  
  ![image](https://github.com/user-attachments/assets/adfa6826-fa50-4f9a-bba4-9d6567544bd7)

+ 2) 추천 랭킹(오른쪽 리스트)
  - 추천된 메뉴 데이터를 recommendations.txt 파일에 저장
  - 추천 데이터를 기반으로 추천 횟수를 계산하여 상위 5개의 메뉴를 랭킹으로 표시
  - 랭킹 항목을 더블 클릭하면 해당 메뉴의 상세 정보를 확인 가능

  -추천 랭킹

  ![image](https://github.com/user-attachments/assets/ee1a597b-b2a1-4f95-aeb7-586327a3c189)


+ 3) 메뉴 추가(추가하기)
  - 사용자 입력(맛집 이름, 메뉴 이름, 맛, 가격, 종류, 위치, 이미지 경로)을 받아 메뉴를 추가.
  - 추가된 메뉴는 menu_data.txt 파일에 저장
 
  - 추가 화면

  ![image](https://github.com/user-attachments/assets/d3d34400-8caf-4aab-b9b2-b033836c8b94)

  - 추가 성공

  ![image](https://github.com/user-attachments/assets/1cd6e636-8687-4954-b493-8da09fabd353)

    
## 4.느낌점
+ 입출력과 컬렉션 프레임워크의 중요성
  
  외부 파일에서 데이터를 읽어오는 방식이나, 이를 관리하는 구조를 처음에는 구현하기 어려웠습니다.하지만, 파일 입출력과 컬렉션 프레임워크를 익힌 뒤에는 클래스나 메서드를 추가해도 기존 로직에 영향을 주지 않고 작동하는 것을 보고, 기반 코드 설계의 중요성을 실감했습니다

+ 필터링 구현의 어려움
  
  단순한 조건 하나를 처리하는 필터링은 혼자서도 가능했지만, 조건이 5개 이상으로 늘어나자 if문이 비효율적으로 길어지고 복잡해졌습니다. 이를 해결하기 위해 AI의 도움으로 Java Stream API와   filter() 함수를 익히며, 효율적인 조건 처리 방식을 배울 수 있었습니다.

+ 모듈화와 가독성 향상
 
  이전 프로젝트에서는 모든 기능을 하나의 클래스에서 처리하려 했지만, 이번에는 기능별로 클래스를 분리하여 필요할 때 호출하는 구조로 변경했습니다. 이러한 분리는 코드의 가독성을 높이고, 유지보수를 훨씬 용이하게 해준다는 것을 깨달았습니다.+ 아쉬운 점이 있다면 메뉴 추가할때 콤보박스에 설정한 맛들과 종류,위치 등 거기에 밖에서 설정하면 예외처리가 되는 형식이나 추가 되는 형식을 실력이 부족하여 구현하지 못해서 아쉽습니다.
  
+ 아쉬운 점
  
  메뉴 추가 기능에서, 콤보박스에 새로운 맛, 종류, 위치 등을 외부 입력으로 설정하거나, 예외 처리가 자동으로 이뤄지는 형식을 구현하지 못한 점이 아쉬웠습니다. 사용자의 추가 입력에 유연하게 대처하는 프로그램을 만들기 위해 더 많은 학습과 경험이 필요하다고 느꼈습니다.

+ 향후 계획
  
  앞으로는 추천 알고리즘을 개선해 단순 랜덤 추첨이 아니라, 사용자 선호도를 학습하는 개인화된 추천 시스템을 구현하고 싶습니다. 또한, 사용자 간의 랭킹과 리뷰를 통합한 소셜 추천 플랫폼으로 확장할 가능성을 탐구하고 싶습니다.



  
