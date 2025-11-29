# WMS
신세계아이앤씨 JAVA 기반 백엔드 개발자 과정 2차 프로젝트

## 프로젝트 소개 : Effitopia
``Effitopia``는 B2B 창고 관리 시스템(WMS) 개발 프로젝트 입니다. 창고 내 재고를 효율적으로 관리하고, 입고부터 배송까지 신뢰성 있는 물류 운영을 지원하는 WMS(창고 관리 시스템)의 이상향을 실현하는 것을 목표로 하였습니다.

- ``Effitopia`` : 효율성을 의미하는 'Efficiency', 이상향을 의미하는 'Utopia'의 합성어
<img width="700" alt="Image" src="https://github.com/user-attachments/assets/ed92fb3a-2ae4-4f55-8c69-0bcd5f2c56fc" />

## 프로젝트 일정
#### 개발 기간: 2024.09.26 - 2024.10.02 (7일)
<image src="https://github.com/user-attachments/assets/cc1da0b0-3727-4ffe-9dc4-9248081605fd" width="600">

## 개발 팀
**TOP(This is Our Page)**
<table>
  <thead>
    <tr align=center >
      <td>
      <b>@kinggora</b>
      </td>
      <td>
        <b>@HongYong-Woo</b>
      </td>
      <td>
        <b>@PARK-TH</b>
      </td>
      <td>
        <b>@qeeeeeqeqq</b>
      </td>
      <td>
        <b>@bottomsUp-99</b>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr valign=top>
      <td>
        <div>팀장</div>
        <ul>
          <li>로그인/회원 관리</li>
          <li>재무 관리</li>
          <li>대시 보드</li>
        </ul>
      </td>
      <td>
        <div>팀원</div>
        <ul>
          <li>재고 관리</li>
          <li>고객 센터</li>
        </ul>
      </td>
      <td>
        <div>팀원</div>
        <ul>
          <li>입고 관리</li>
          <li>거래처 관리</li>
        </ul>
      </td>
      <td>
        <div>팀원</div>
        <ul>
          <li>창고 관리</li>
          <li>계약 관리</li>
        </ul>
      </td>
      <td>
        <div>팀원</div>
        <ul>
          <li>출고 관리</li>
          <li>차량 관리</li>
        </ul>
      </td>
    </tr>
  </tbody>
</table>

## 개발 환경
- **IDE**: IntelliJ IDEA Ultimate
- **Language**: Java 17, Javascript, HTML5, CSS3
- **Framework**: Spring Boot 3.0.1, Spring Security 6, Spring Batch 6
- **Data**: MySQL 8.0.21, MyBatis, Spring Data Redis
- **Frontend**: Thymeleaf, Bootstrap
- **Library**: Chart.js, Kakao Map, Gson, Zxing
- **Tools**: GitHub, ERDCloud, Figma, Notion, Slack, Google Workspace

## ERD
![Image](https://github.com/user-attachments/assets/a27fdc0f-827f-493b-967a-462039e05416)

## Wireframe
![Image](https://github.com/user-attachments/assets/858a443d-29e8-41e4-8e08-18181efdc465)

## 프로젝트 구조
```
📦effitopia
 ┣ 📂config
 ┣ 📂controller
 ┣ 📂domain
 ┣ 📂dto
 ┣ 📂enumeration
 ┣ 📂exception
 ┣ 📂mapper
 ┣ 📂security
 ┗ 📂service
```

## 요구사항 분석 및 도메인 설계
### 1. 회원 설계
- 사업자: 하나 이상의 창고와 계약하여, 입/출고 요청을 통해 재고 관리를 하는 주체이다.
- 배송 기사: 차량을 등록하고, 배차 단계에서 출고 건을 배정 받는다.
- 창고 관리자: 하나의 창고를 관리하는 회원으로, 관리하는 창고에 대해 사업자와 계약을 체결하고 창고 내 재고 관리에 대한 책임이 있다.
- 총 관리자: WMS 를 관리하는 회원. 위 회원들에 대한 자격을 확인하고 가입 및 탈퇴 승인 권한이 있다.
### 2. 창고 설계
- 종류: 상온, 냉장, 냉동
- 창고 내에 세부 구역이 존재한다.
- 공통 재고 통합 관리 방식을 채택하여 입고 시 동일한 상품을 동일한 구역에 적재하며 수용 가능 용량을 초과할 경우 빈 구역에 적재한다.
- 재고 박스의 가로, 세로, 높이를 고려하여 적재한다.
### 3. 재고 설계
- 임시 재고와 재고 실사를 통해 재고를 관리한다.
- 임시 재고: 전산 상 재고. 입/출고 발생 시 임시 재고 테이블의 값을 변경하고, 매일 입/출고 마감 시간에 임시 재고 테이블의 변동 사항을 재고 테이블에 일괄 반영한다.
- 재고 실사: 실사 당시의 실 재고. 실제 창고에 존재하는 재고의 수량을 실사 담당자가 재고 실사 테이블에 값을 등록하고 매일 입/출고 마감 시간 이후에 재고 테이블에 반영한다.
### 4. 입고 시나리오
1. 거래처 등록 & 상품 등록: 사업자는 계약을 맺은 거래처와 창고에 저장할 상품을 등록하고, 거래처에 직접 발주 요청을 한다.
2. 입고 요청: 사업자는 계약 창고에 입고 요청서를 작성한다. 창고 관리자는 해당 요청을 확인하고 승인/반려 처리를 한다. 재고 관리 차원에서 창고 관리자의 대리 입고 요청이 가능하다.
3. 입고: 상품이 창고에 도착하면 창고 관리자는 입고 요청 건에 대해 적재 구역을 지정하여 입고 처리를 한다.
### 5. 출고 시나리오
1. 출고 요청: 사업자가 재고에 대해 출고 요청서를 작성한다. 창고 관리자는 해당 요청을 확인하고 승인/반려 처리를 한다.
2. 배차 등록: 출고 승인 된 건에 대해 차량이 등록된 배송 기사를 배정한다. 이때 배송 기사의 담당 지역이 가까운 순, 차량의 적재 가능량이 많은 순으로 배정한다.
3. 운송장 등록: 출고 승인 & 배차 승인 된 건에 대해 출고, 배차 정보를 모두 포함하는 운송장을 등록한다.
4. 출고: 출고 요청된 상품은 남은 유통기한이 짧은 순으로 출고한다.
### 6. 재무 설계
- 창고 관리 중 발생하는 비용을 창고 관리자입장에서 매출, 지출로 나누어 관리한다. (사업자 입장에서 매출 -> 지출, 지출 -> 매출이 됨)
- 정산 카테고리: 창고 계약비, 창고 보관료, 입/출고 수수료, 운임료, 창고 관리비, 기타
  - 창고 계약비: 창고 계약 시점 1회 부과
  - 창고 보관료: 월간 사용 면적에 대해 부과
  - 입/출고 수수료: 일일 입/출고 박스 수에 대해 부과
  - 운임료: 일일 운송장 수에 대해 부과
  - 기타 창고 관리 비용은 창고 관리자가 직접 등록할 수 있다.

## 주요 기능
### 대시 보드
<img width="1662" height="893" alt="Image" src="https://github.com/user-attachments/assets/6714fc40-fbc1-4724-a1a4-2913671a5ae3" />

### 로그인/회원 관리
![Image](https://github.com/user-attachments/assets/83a6972b-9fa7-40d3-a2fc-d0800ed207aa)

### 창고 관리
<img width="1664" height="946" alt="Image" src="https://github.com/user-attachments/assets/24bbffb4-6b57-406a-93b1-234055a83247" />

### 입고 관리
<img width="1664" height="885" alt="Image" src="https://github.com/user-attachments/assets/9cb988e7-b151-40c6-bb8a-95fee40ef726" />

### 출고 관리
<img width="1667" height="878" alt="Image" src="https://github.com/user-attachments/assets/c07a3b69-a70b-427e-9bb1-981d39d7810d" />

### 재고 관리
<img width="1664" height="919" alt="Image" src="https://github.com/user-attachments/assets/bbe12b4c-0289-449e-afae-d4e813e3a391" />

### 재무 관리
<img width="1667" height="878" alt="Image" src="https://github.com/user-attachments/assets/af1e668b-cd80-4546-8251-b8b6421c8b58" />

## 커밋 메세지 컨벤션
**Commit Type Gitmoji**
- 🐛 bug: 버그 수정
- ✨ sparkles: 새로운 기능 도입
- 🔥 fire: 코드나 파일 삭제
- 📝 memo: 문서 추가 또는 업데이트
- 🎨 art: 코드의 구조/형식 개선
- 🚧 construction: 작업 진행 중
- 🎉 tada: 프로젝트 시작
- ✅ white_check_mark: 테스트 추가, 업데이트 또는 통과
- 🔧 wrench: 설정 파일 추가 또는 업데이트
- 📦️ package: 컴파일된 파일이나 패키지 추가 또는 업데이트
- ♻️ recycle: 코드 리팩토링
