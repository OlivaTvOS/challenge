public class UserProfile {
    private String preferences = " ";
    private int completedMissions;
    private String favoriteCategory = " ";
    private String responseType = " ";

    public UserProfile(String preferences){
        this.preferences = preferences;
    }

    public void trackMission(){
        completedMissions += 1;
    }

    public String getPreferences(){
        return preferences;
    }

    public String getFavoriteCategory(){
        return favoriteCategory;
    }

    public void setFavoriteCategory(String category){
        this.favoriteCategory = category;
    }
}
