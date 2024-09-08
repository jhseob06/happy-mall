시각장애인을 위한 쇼핑몰 😶‍🌫️
===
<br>

## 프로젝트 소개 🌟

**해피몰**은 시각장애인을 위한 쇼핑몰 API서버로, 교내 프로젝트의 일환으로 개발되었습니다. 이 서버는 **행복한 심봉사**라는 로봇과 연결되어, 시각장애인이 옷 쇼핑을 편리하게 이용할 수 있도록 돕습니다.
로봇은 카메라와 TensorFlow를 활용하여 사용자의 치수를 측정합니다. 이후, 측정된 치수에 맞는 사이즈와 시각장애인이 선호하는 옷 유형을 서버로 전송하면, 해피몰은 이를 바탕으로 최적의 옷을 추천합니다.
이 프로젝트는 시각장애인의 쇼핑 경험을 혁신하고, 시각장애인들이 보다 편리한  옷 쇼핑을 할 수 있도록 하기 위해 만들어졌습니다.

<br>

## API 명세서 📃

### MEMBER
[<MEMBER API 명세서>](https://mature-crow-b6f.notion.site/3af4834ac95d413f80c431d054618f79?v=5c12aaa85abb4f8fa7892ee860d0021e&pvs=74)

### ADMIN
[<ADMIN API 명세서>](https://mature-crow-b6f.notion.site/6f007d90125843bf96ff8375f7038f4a?v=80d0b69d6b3f40c0b71a71f4a296cbd7&pvs=4)

<br>

## ERD
![ERD](https://github.com/jhseob06/happy-mall/blob/main/erd.png)
[<ERD 연결>](https://www.erdcloud.com/d/Tgjz7iDzaZcG7TkR9)

<br>

## 프로젝트 초기세팅 ⚙️

1. **저장소를 클론합니다**
  ```bash
  git clone https://github.com/jhseob06/happy-mall.git
  ```
<br>

2. **application.properties를 변경합니다**
 - shoppingmall/src/main/resources/application.properties 열기
 - SQL 서버 변경
 - project.domain 변경
 - shoppingmall/src/test/resources/application.properties에도 똑같이 적용
<br>

3. **프로젝트를 실행합니다**

<br>

## 기술스택 🪛

### BE
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JUnit5

### 환경
- IntelliJ
- Postman
- SourceTree
