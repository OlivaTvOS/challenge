public class User {
    private String id = " ";
    private String email = " ";
    private UserProfile profile;
    private int totalPoints;

    public User(String id, String email){
        this.id = id;
        this.email = email;
    }

    public String getId(){
        return id;
    }

    public void addPoints(int ammount){
        totalPoints += ammount;
    }

    public UserProfile getProfile(){
        return this.profile;
    }
}
