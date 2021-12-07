package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class FieldLogTrace implements LogTrace{

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private TraceId traceIdHolder; // traceId 동기화, 동시성 이슈 발생

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder; // syncTraceId()를 수행하고 난 후이기 때문에 값이 설정되어 있다.
        // 시간 정보
        Long startTimeMs = System.currentTimeMillis();
        // 로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

        return new TraceStatus(traceId, startTimeMs, message);
    }

    private void syncTraceId(){
        if(traceIdHolder == null){ // 최초 호출 시 초기 값 생성
            traceIdHolder = new TraceId();
        }else { // 직전 로그가 있으면 해당 로그의 traceId를 참고해서 동기화하고, level도 하나 증가시킨다.
            traceIdHolder = traceIdHolder.createNextId();
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {

        Long stopTimeMs = System.currentTimeMillis(); // 종료 시간
        long resultTimeMs = stopTimeMs - status.getStartTimeMs(); //  총 시간 = 종료 시간 - 시작 시간
        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else { // 예외 발생
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }
        releaseTraceId(); // 메서드를 호출이 끝나고 level을 하나 감소해주는 메서드
    }

    private void releaseTraceId() {
        if(traceIdHolder.isFirstLevel()){ // 만약 최초 호출 level == 0에 도달하면 내부에서 관리하는 traceId를 제거한다.
            traceIdHolder = null; // destroy
        } else { // level == 0 아니라면 해당 로그의 traceId를 참고해서 동기화하고, level도 하나 감소시킨다.
            traceIdHolder = traceIdHolder.createPreviousId();
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "| "); }
        return sb.toString();
    }
}
