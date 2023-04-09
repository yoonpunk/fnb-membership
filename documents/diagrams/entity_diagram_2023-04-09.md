# fnb-membership ERD
fnb-membership의 ERD 입니다. (2023-04-09 기준)

## 변경점
* POINT_ORDER와 MEMBER간 연관관계를 제거하였습니다.
* POINT_ORDER에는 member_id가 여전히 존재하고, MEMBER의 member_id의 값을 사용하지만 실제 연관관계는 없습니다.

## Diagram
```mermaid
erDiagram
    BRAND {
        binary(16) brand_id PK
        varchar(255) brand_name
        datetime(6) created_at
    }
    MEMBER {
        binary(16) member_id PK
        varchar(12) barcode UK
        varchar(11) phone UK
        datetime(6) created_at
    }
    POINT_ORDER {
        binary(16) point_order_id PK
        binary(16) member_id
        varchar(255) brand_name
        varchar(255) store_name
        varchar(255) type
        bigint requested_point_amount
        datetime(6) approved_at
    }
    STORE {
        binary(16) store_id PK
        binary(16) brand_id FK
        varchar(255) store_name
        datetime(6) created_at
    }
    POINT {
        binary(16) point_id PK
        binary(16) brand_id FK
        binary(16) member_id FK
        bigint amount
        datetime(6) created_at
        bigint version
    }

    POINT |o--|| BRAND : brand_id
    POINT }o--|| MEMBER : member_id
    STORE }o--|| BRAND : brand_id

```
## 테이블 설명

|테이블명|내용|
|--|--|
|member|가입된 회원 정보를 저장|
|brand|등록된 브랜드 정보를 저장|
|store|등록된 점포 정보를 저장|
|point|각 회원에 대한 브랜드 별 적립 포인트 정보를 저장|
|point_order|포인트 사용/적립 요청 정보를 저장|

## 연관관계

|관계|내용|설명|
|--|--|--|
|member - point|1:N|회원 하나당 브랜드 별 적립 포인트를 갖음|
|point - brand|N:1|브랜드 하나당 여러 회원의 적립 포인트를 갖음|
|brand - store|1:N|브랜드 하나당 여러 점포를 갖음|

## 특이사항

* point_order 테이블은 회원의 포인트 사용/적립 요청 기록을 저장하기 위한 테이블로 단순 히스토리성 데이터이다. 
따라서, member, brand, store 테이블과는 연관관계를 맺지 않고 각 테이블의 member_id, brand_name, store_name만 저장하도록 설계했다.
* point 테이블의 version 컬럼은 포인트 사용과 적립의 동시성 문제 해결을 위해 낙관적락을 사용하기 위해 만들었다.