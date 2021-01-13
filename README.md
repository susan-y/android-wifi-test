# android-wifi-test
android wifi 테스트 모음

## 사용방법
소스코드를 내려받고 각 폴더를 Android Studio에서 오픈

## 개발환경
- Android Studio 4.1.1
- Kotlin 1.3.71

## Projects
### 1. android10Wifi
안드로이드 10 이상의 Wi-Fi 연결 테스트를 위한 소스코드

- 참고한 구글샘플
	- https://developer.android.com/guide/topics/connectivity/wifi-suggest

- 상태
	- 안드로이드 10, 11에서 다르게 동작함
	- WifiSuggestion가 Notification에 바로 표시되지 않음
	- Wi-Fi Profile 저장되지 않음

- build.gradle
	- targetSdkVersion 30
	- minSdkVersion 29

