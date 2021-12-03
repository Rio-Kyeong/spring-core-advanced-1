package hello.advanced.trace;

import java.util.UUID;

public class TraceId {

    private String id;
    private int level;

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    // 클래스 내부에서만 값 셋팅을 위해 사용하는 생성자
    private TraceId(String id, int level){
        this.id = id;
        this.level = level;
    }

    private String createId() {
        // 생성된 범용 고유 식별자(UUID) : ab99e16f-3cde-4d24-8241-256108c203a2
        // ab99e16f <- 앞 8자리만 사용
        return UUID.randomUUID().toString().substring(0,8);
    }

    public TraceId createNextId(){
        return new TraceId(this.id, this.level + 1);
    }

    public TraceId createPreviousId(){
        return new TraceId(this.id, this.level - 1);
    }

    public boolean isFirstLevel(){ // 첫번째 레벨인지 확인
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
