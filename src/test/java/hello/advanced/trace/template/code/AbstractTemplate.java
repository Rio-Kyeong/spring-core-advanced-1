package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

/**
 * abstract class (추상 클래스)
 * 상속관계에서 부모클래스를 정의할 때 사용
 * 직접 객체화(new 연산자 사용 불가)가 될 수 없다.
 * 자식 클래스를 생성하면 부모 클래스부터 생성된다.
 * abstract method(추상 메서드), 변수, 일반 메서드로 이루어져 있다.
 */
@Slf4j
public abstract class AbstractTemplate {

    public void execute(){
        long startTime = System.currentTimeMillis(); // 시작 시간
        //비즈니스 로직 실행
        call(); // 상속
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis(); // 종료 시간
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    /**
     * abstract method(추상 메서드)
     * 구현의 강제성을 가진 method(자식 클래스에서 반드시 오버라이드해야 한다)
     * 직접 호출 불가(method의 body가 존재하지 않는다)
     * 자식 클래스가 구현해야 할 일의 목록을 정의할 때
     */
    protected abstract void call();
}
