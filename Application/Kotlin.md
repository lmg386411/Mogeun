# 안드로이드 프로젝트

## 프로젝트 열기

- 안드로이드 스튜디오에서 `Mogeun` 폴더를 연다.

## Compose에서의 상태

### 리컴포지션

- 데이터가 변경되었을때 Compose가 새 데이터로 이러한 함수를 다시 실행하여 업데이트된 UI를 만드는 것.
- 일반적으로 선언된 변수는 Compose에서 데이터를 추적하지 않기 때문에 리컴포지션이 일어나지 않는다.

``` kotlin
var expanded = false
```

### MutableState

- 어떤 값을 보유하고 그 값이 변경될 때마다 UI 업데이트(리컴포지션)를 트리거하는 인터페이스.
- Compose는 데이터가 변경된 구성요소만 다시 구성하고 영향을 받지 않는 구성요소는 다시 구성하지 않고 건너뛴다.

``` kotlin
val expanded = mutableStateOf(false)
```

> 이렇게 선언할 경우 리컴포지션이 일어났을때 expanded 변수도 재설정된다.

### remember

- 여러 리컴포지션 간에 상태를 유지하려면 `remember`를 사용하여 변경 가능한 상태를 기억해야 한다.
- `remember`는 리컴포지션을 방지하는데 사용되므로 상태가 재설정되지 않는다.

``` kotlin
val expanded = remember { mutableStateOf(false) }
```

### rememberSaveable

- `remember` 함수는 Composable이 Composition에 유지되는 동안에만 작동한다.
- 기기를 회전하면 전체 활동이 다시 시작되므로 모든 상태가 손실된다.
- 구성이 변경되거나 프로세스가 중단될 때에도 모든 상태가 손실된다.


``` kotlin
val expanded = rememberSaveable { mutableStateOf(false) }
```

### 변수 사용

- mutableState로 선언된 변수는 `.value`로 변수의 값에 접근하고 수정할 수 있다.

``` kotlin
val expanded = rememberSaveable { mutableStateOf(false) }
expanded.value = !expanded.value
```

### by 키워드

- 매번 `.value`를 입력할 필요가 없도록 해주는 속성 위임

``` kotlin
var expanded = rememberSaveable { mutableStateOf(false) }
expanded = !expanded
```

## Docs

[Compose 예시 어플리케이션](https://github.com/android/compose-samples)