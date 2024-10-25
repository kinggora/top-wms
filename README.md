# WMS
신세계아이앤씨 JAVA 기반 백엔드 개발자 과정 2차 프로젝트

## 프로젝트 소개 : Effitopia
Effitopia는 B2B 창고 관리 시스템(WMS) 개발 프로젝트 입니다. 창고 내 재고를 효율적으로 관리하고, 입고부터 배송까지 신뢰성 있는 물류 운영을 지원하는 WMS(창고 관리 시스템)의 이상향을 실현하는 것을 목표로 하였습니다.

- ``Effitopia`` : 효율성을 의미하는 'Efficiency', 이상향을 의미하는 'Utopia'의 합성어

## 프로젝트 일정
- **9/26** : 프로젝트 주제 선정, 기획안 작성

- **9/26 - 10/2** : 프로젝트 설계, 구현 및 테스트, 포트폴리오 작성, 마무리 및 발표 준비

- **10/2** : 프로젝트 발표 및 평가

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
`Java 17`
- **IDE**: IntelliJ IDEA Ultimate
- **Framework**: Spring Boot 3.0.1, Spring Security 6
- **Database**: MySQL 8.0.21, MyBatis, Spring Data Redis
- **Library**: Thymeleaf, Bootstrap
- **Tools**: GitHub, Notion, Slack

## 프로젝트 구조
```
📦effitopia
 ┣ 📂config
 ┣ 📂controller
 ┃ ┣ 📂formatter
 ┣ 📂domain
 ┣ 📂dto
 ┣ 📂enumeration
 ┣ 📂exception
 ┣ 📂mapper
 ┣ 📂security
 ┗ 📂service
```

## 주요 기능
- 로그인/회원 관리
- 창고 관리
- 입고 관리
- 출고 관리
- 재고 관리
- 계약 관리
- 재무 관리
- 고객 센터
- 대시 보드

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
