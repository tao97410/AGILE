package h4131.model;
public class Courier {
    private long id;
    private String name;

    public Courier(long anId, String aName) {
        this.id = anId;
        this.name = aName;
    }
      

    /**
     * @return long return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
