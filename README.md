# Spring Security, 직접 만들면서 이해해보자!

이 프로젝트는 단순한 필터 하나에서 출발하여, 인증(Authentication), 인가(Authorization), 예외 처리, FilterChainProxy, 그리고 Lambda DSL 방식까지 **점진적으로
기능을 확장해가며** Spring Security의 구조를 직접 구현해보는 실습형 학습 프로젝트입니다. 함께 보면 좋은 블로그 시리즈를 보면서 각 단계의 구현을 따라가면, Spring Security의 아키텍처 및 필터에 대한 이해를 높일 수 있습니다.

## 목차

각 단계는 Git Branch로 나누어져 있어, 단계별로 차근차근 따라갈 수 있습니다.

| Step                                      | 설명                  |
|-------------------------------------------|---------------------|
| [완성본]                                     | 최종 구현 결과            |
| [step-1-initial-setup]                    | 프로젝트 초기 설정          |
| [step-2-authentication]                   | JWT 기반 인증 구현        |
| [step-3-authorization]                    | 권한 기반 접근 제어 구현      |
| [step-4-authorization-advanced]           | 인가 필터 위임 처리 구현      |
| [step-5-exception-translation-filter]     | 예외 처리 필터 구현         |
| [step-6-security-context-holder-filter]   | 인증 정보 정리 필터 구현      |
| [step-7-filter-chain-proxy]               | FilterChainProxy 구현 |
| [step-8-lambda-dsl]                       | 람다 기반 DSL 구성        |
| [step-9-handler-method-argument-resolver] | 인증 정보 컨틀로러에 주입      |

## 함께 보면 좋은 블로그 시리즈

| 제목                                                                   |
|----------------------------------------------------------------------|
| [Spring Security, 직접 만들면서 이해해보자! - 1편: 아키텍처 이해]                      |
| [Spring Security, 직접 만들면서 이해해보자! - 2편: 인증, 인가, 예외 처리]                |
| [Spring Security, 직접 만들면서 이해해보자! - 3편: FilterChainProxy와 Lambda DSL] |

<!-- 브랜치 링크 모음 -->

[완성본]: https://github.com/alstn113/spring-security-impl/tree/main

[step-1-initial-setup]: https://github.com/alstn113/spring-security-impl/tree/step-1-initial-setup

[step-2-authentication]: https://github.com/alstn113/spring-security-impl/tree/step-2-authentication

[step-3-authorization]: https://github.com/alstn113/spring-security-impl/tree/step-3-authorization

[step-4-authorization-advanced]: https://github.com/alstn113/spring-security-impl/tree/step-4-authorization-advanced

[step-5-exception-translation-filter]: https://github.com/alstn113/spring-security-impl/tree/step-5-exception-translation-filter

[step-6-security-context-holder-filter]: https://github.com/alstn113/spring-security-impl/tree/step-6-security-context-holder-filter

[step-7-filter-chain-proxy]: https://github.com/alstn113/spring-security-impl/tree/step-7-filter-chain-proxy

[step-8-lambda-dsl]: https://github.com/alstn113/spring-security-impl/tree/step-8-lambda-dsl

[step-9-handler-method-argument-resolver]: https://github.com/alstn113/spring-security-impl/tree/step-9-handler-method-argument-resolver

<!-- 블로그 링크 모음 -->

[Spring Security, 직접 만들면서 이해해보자! - 1편: 아키텍처 이해]: https://alstn113.tistory.com/49

[Spring Security, 직접 만들면서 이해해보자! - 2편: 인증, 인가, 예외 처리]: https://alstn113.tistory.com/50

[Spring Security, 직접 만들면서 이해해보자! - 3편: FilterChainProxy와 Lambda DSL]: https://alstn113.tistory.com/51