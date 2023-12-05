package h4131.model;

public enum TimeWindow {
    EIGHT_NINE(0),
    NINE_TEN(1),
    TEN_ELEVEN(2),
    ELEVEN_TWELVE(3);

    private final int index;

    TimeWindow(int index){
        this.index = index;
    }

    public int getIndex(){
        return this.index;
    }
}