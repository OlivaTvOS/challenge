public class Category {
    private String id;
    private String name;
    private String description;
    private int responseCount;
    private static int categoryCount;

    public Category(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public void addResponse(){
        responseCount += 1;
    }

    public static int getTotalCategories(){
        return categoryCount;
    }
}
