package h4131.model;

public enum TimeWindow {
    EIGHT_NINE("8h-9h"),
    NINE_TEN("9h-10h"),
    TEN_ELEVEN("10h-11h"),
    ELEVEN_TWELVE("11h-12h");

    private String representation;

    private TimeWindow(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }

    public static TimeWindow getTimeWindowByRepresentation(String representation){
        TimeWindow result;
        switch(representation){
            case "8h-9h" :
                result = EIGHT_NINE;
                break;
            case "9h-10h" :
                result = NINE_TEN;
                break;
            case "10h-11h" :
                result = TEN_ELEVEN;
                break;
            case "11h-12h" :
                result = ELEVEN_TWELVE;
                break;
            default:
                result = null;
        }
        return result;
    }
}