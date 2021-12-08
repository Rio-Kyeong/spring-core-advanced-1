package hello.advanced.trace.callback;

public interface TraceCallback<T>{
    T call(); // 상황에 따라 반환타입을 다르게 설정할 수 있도록 제네릭 사용
}
