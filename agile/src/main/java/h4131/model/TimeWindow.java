package h4131.model;

public enum TimeWindow {
    WAREHOUSE("warehouse"),
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

    /**
     * Get the enum value corresponding to a representation
     * @param representation String representation of the time window
     * @return Corresponding enum value
     */
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