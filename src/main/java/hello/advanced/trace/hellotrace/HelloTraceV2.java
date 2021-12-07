package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloTraceV2 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    public TraceStatus begin(String message){ // 출력용 메시지
        TraceId traceId = new TraceId(); // TraceId 생성
        Long startTimeMs = System.currentTimeMillis(); // 시간 정보
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message); // 로그 출력
        return new TraceStatus(traceId, startTimeMs, message);
    }

    public TraceStatus beginSync(TraceId beforeTraceId, String message){ // 이전 TraceId, 출력용 메시지
        TraceId nextId = beforeTraceId.createNextId(); // 이전 traceId를 참고해서 동기화하고, level도 하나 증가시킨다.
        Long startTimeMs = System.currentTimeMillis(); // 시간 정보
        log.info("[{}] {}{}", nextId.getId(), addSpace(START_PREFIX, nextId.getLevel()), message); // 로그 출력
        return new TraceStatus(nextId, startTimeMs, message);
    }

    public  void end(TraceStatus status){ // 로그를 정상 종료
        // 파라미터로 시작 로그의 상태(TraceStatus)를 전달 받는다.
        complete(status, null);
    }

    public void exception(TraceStatus status, Exception e){ // 로그를 예외 상황으로 종료
        // 파라미터로 시작 로그의 상태(TraceStatus)와 발생 예외 정보를 전달 받는다.
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis(); // 종료 시간
        long resultTimeMs = stopTimeMs - status.getStartTimeMs(); //  걸린 총 시간 = 종료 시간 - 시작 시간
        TraceId traceId = status.getTraceId();

        if (e == null) { // 정상 종료
            log.info("[{}] {}{} time={}ms", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else { // 예외 발생
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }
    }

    private static String addSpace(String prefix, int level) { // 레벨에 맞게 로그를 생성
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "| "); }
        return sb.toString();
    }
}
