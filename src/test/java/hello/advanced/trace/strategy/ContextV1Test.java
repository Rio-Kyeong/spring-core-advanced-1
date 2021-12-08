package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    public void strategyV0() throws Exception {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis(); // 시작 시간
        //비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis(); // 종료 시간
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis(); // 시작 시간
        //비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis(); // 종료 시간
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    /**
     * 전략 패턴 사용
     * @throws Exception
     */
    @Test
    public void strategyV1() throws Exception {
        Strategy strategyLogic1 = new StrategyLogic1();
        ContextV1 context1 = new ContextV1(strategyLogic1);
        context1.execute();

        Strategy strategyLogic2 = new StrategyLogic2();
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();
    }

    /**
     * 전략 패턴, 익명 내부 클래스1
     * @throws Exception
     */
    @Test
    public void strategyV2() throws Exception {
        Strategy strategyLogic3 = new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직3 실행");
            }
        };
        ContextV1 context3 = new ContextV1(strategyLogic3);
        log.info("strategyLogic3={}",strategyLogic3.getClass());
        context3.execute();
    }

    /**
     * 전략 패턴, 익명 내부 클래스2
     * @throws Exception
     */
    @Test
    public void strategyV3() throws Exception {
        ContextV1 context4 = new ContextV1(new Strategy(){
            @Override
            public void call() {
                log.info("비즈니스 로직4 실행");
            }
        });
        context4.execute();
    }

    /**
     * 전략 패턴, 람다
     * 인터페이스에 메서드가 1개만 있어야 한다.
     * @throws Exception
     */
    @Test
    public void strategyV4() throws Exception {
        ContextV1 context5 = new ContextV1(() -> log.info("비즈니소 로직5 실행"));
        context5.execute();

        ContextV1 context6 = new ContextV1(() -> log.info("비즈니소 로직6 실행"));
        context6.execute();
    }
}
