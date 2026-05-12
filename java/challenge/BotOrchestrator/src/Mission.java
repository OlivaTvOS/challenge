public class Mission {
    private String id;
    private String title;
    private Category category;
    private String missionType;
    private String description;

    public Mission(String id, String title, Category category){
        this.id = id;
        this.title = title;
        this.category = category;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public Category getCategory(){
        return category;
    }

    public String getMissionType(){
        return missionType;
    }

    public String getDescription(){
        return description;
    }
}
