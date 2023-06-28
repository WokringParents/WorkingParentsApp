# WorkingParents App   
*맞벌이 부부를 위한 종합 육아 앱 개발*

## 🖥️ 프로젝트 소개
**부부간의 앱공유를 기반으로 동네,유치원,가정을 통합하는 육아플랫폼**
- **개발 배경**
<br>온라인 설문조사,워킹부부 인터뷰, 보도 및 문헌 자료조사 진행 
<br>>> 저출산 문제의 가장 큰 원인? 직장생활과 육아 병행의 어려움
<br>>> 맞벌이 부부의 고충? 육아분담에 대한 서로의 생각차이,직장으로 인한 아이 등하원의 어려움, 그로 인한 부부 갈등
<br>이러한 어려움을 겪는 맞벌이 부부의 일상에 도움을 줄 수 있는 앱 "워킹패런츠:워킹부부가 함께 만드는삶" 을 기획 및 제작 

- **차별성 및 목표**
<br>현대사회에서 저출산은 큰 사회적 문제이며 나날이 맞벌이 부부가 늘어나고 있는 추세
<br>>> 기존 육아앱 시장에서 볼 수 없었던 맞벌이 부부에 특화된 앱
<br>워킹부부가 일과 육아를 양립하기 편한 환경을 구성
<br>>> 워킹부부의 육아에 대한 부담감* 낮추고 나아가 저출산 사회문제 완화에 보탬
<br>

## 🕰️ 개발 기간
* 2022.07.28~2022.10.20

### ⚙️ 개발 환경
- Language : kotlin, java
- IDE :  IntelliJ IDEA
- Framework : Springboot, MyBatis(SQL Mapper)
- Database : MySQL
- Server : AWS Elastic Beanstalk , AWS RDS , FCM(Firebase Cloud Messaging)

![앱구성도](https://github.com/WokringParents/WorkingParentsApp/blob/master/images/System.png)


### 🧑‍🤝‍🧑 맴버 구성
 - 팀장  : 박지연 - 로그인, 부부연결, 육아분담 체크리스트, 유치원 식단표, FCM푸시알람, 이미지 업로드, UI구현
 - 팀원1 : 이경주 - 등하원 게시판, FCM 푸시알람, 유치원 공지 알림장, 이미지 업로드, UI구현 및 아이콘 디자인
 - 팀원2 : 오은아 - 회원가입, 아이등록, 일정공유 캘린더, 학부모 알림, UI구현
 - 팀원3 : 김윤주 - 총괄 디자이너, 로고제작, UI 설계 및 디자인
<br>

## 📌 주요 기능
![System](https://github.com/WokringParents/WorkingParentsApp/blob/master/images/storyboard.png)
<br>

### **#A. 학부모**
#### 로그인 / 회원가입 
- 이메일 인증 
    - SMTP 네이버 메일 전송
    - JavaMail API 사용
 
#### 마이페이지
- 부부연결 코드 발급
    - 발급시간 ~ 24시간 유효성 
    - DB Procedure(프로시저) 활용
- 부부연결 배우자 코드 입력
- 아이등록
  
#### 육아분담 체크리스트
- 월별 주차선택
    - 한달 날짜/요일 고려 주차 계산 
- 부부간 공유 육아 분담 항목 작성
    - 일주일 단위 추가
    - 하루 단위 추가 
- 육아분담 항목 수정/삭제
- 부부 수행완료 스탬프 표시

#### 일정공유 캘린더
- *material-calendarview* 🔗[OpenSource-github](https://github.com/prolificinteractive/material-calendarview)
- 일정 추가/삭제/수정
- 부부 일정 색깔 구분

#### 등하원 게시판 
- 게시글 작성/삭제
- 게시글 내용 키워드 검색
- 댓글 작성/삭제
- 대댓글 작성/삭제
- 사용자 푸시알람
    - 같은 동네 주민 선별 알림
    - FCM(Firebase Cloud Messaging) 사용
<br>

### **#B. 선생님**
#### 힉부모 알림
- 등원/하원 확인시 학부모 푸시 안심알림
- 학부모 긴급 연락
    - 전화 걸기
    - 문자 보내기

#### 유치원 공지 알림장
- 알림장 작성 
    - 단일 이미지 업로드
    - 다중 이미지 업로드
    - 갤러리 선택/사진 촬영 가능
- 알림장 상세보기

#### 유치원 식단표
- 하루 식단 모아보기
- 식단표 작성
    - 급식유형 선택
    - 단일 이미지 업로드
    - 갤러리 선택/사진 촬영 가능
<br>

✅ **이미지 업로드**
- Server
    - Springboot : 파일 업로드/다운로드
    - DB : '파일경로/이름' 저장
- Android
    - 이미지 경로 얻기  *GetRealPath*  🔗[OpenSource-github](https://gist.github.com/tatocaster/32aad15f6e0c50311626)
    - 이미지 압축  *Bitmap Compress*
    - 이미지 로드 속도 개선  *Glide* 라이브러리 사용 
    - 서버 파일 전송 *Retrofit2 Multipart* 
<br>

## 🎥 시연 영상

[**[YouTube] <span style="font-size:89%">워킹패런츠: 워킹부부가 함께 만드는 삶</span>**](https://www.youtube.com/watch?v=xoTjoYSUeEU)
<br>
[![Thumbnail](https://github.com/WokringParents/WorkingParentsApp/blob/master/images/Thumbnail.png)](https://www.youtube.com/watch?v=xoTjoYSUeEU)
<br>
<br>
[**[YouTube] <span style="font-size:89%">워킹패런츠 서비스 소개 영상</span>**](https://www.youtube.com/watch?v=KqCub6TXcQg)
<br>
[![Service](https://github.com/WokringParents/WorkingParentsApp/blob/master/images/Service.png)](https://www.youtube.com/watch?v=KqCub6TXcQg)

<br>
<br>


## 🏆 수상 실적
**제10회 K-해커톤 전국 대학생 앱 개발 챌린지 우수상 수상**
<br>
[[영남대 학과소식] <span style="font-size:80%">박지연, 오은아, 이경주 학생, ‘제10회 전국 대학생 앱개발 챌린지 K-해커톤’ 우수상 수상</span>](https://www.yu.ac.kr/cse/community/news.do?mode=view&articleNo=4825877&article.offset=10&articleLimit=10)
<br>
[[SEN 서울경제TV] <span style="font-size:80%">영남대 컴퓨터공학과, 주요 공모전 휩쓸며 전공 역량 공인</span>](https://m.sentv.co.kr/news/view/648671)
<br>
[[YU News Room] <span style="font-size:80%">‘대학생 앱개발 챌린지 K-해커톤’ 등 전공 관련 경진대회서 잇달아 수상</span>](https://www.yu.ac.kr/pr/yunews/yu-news-room.do?mode=view&articleNo=5993495&article.offset=70&articleLimit=10)
<br>

**한국정보처리학회 춘계학술대회 ASK 2023 동상 수상 - 맞벌이 부부를 위한 종합 육아 앱 설계 및 구현** 
<br>*(논문) Design and Implement of an Integrated Parenting App for Working Parents*
<br>
[[영남대 학과소식] <span style="font-size:80%">오은아, 박지연, 이경주 학생 ‘한국정보처리학회 춘계학술발표대회 (ASK 2023)’ 동상 수상 </span>](https://www.yu.ac.kr/cse/community/news.do?mode=view&articleNo=6343769&article.offset=0&articleLimit=10)
<br>
<br>

### 🔗 멤버 깃허브
 - 박지연 - [github/codingGoover](https://github.com/codingGoover)
 - 이경주 - [github/rodwnl](https://github.com/rodwnl)
  - 오은아 - [github/eunalove](https://github.com/eunalove)
