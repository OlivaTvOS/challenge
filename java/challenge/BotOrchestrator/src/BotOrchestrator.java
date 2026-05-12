public class BotOrchestrator {
    private User[] users;
    private Mission[] missions;
    private TextWriter[] responses;
    private Category[] categories;

    public BotOrchestrator(){

    }

    public void addUser(User user){
        users = new User[]{user};
    }

    public void addMission(Mission mission){
        missions = new Mission[]{mission};
    }

    public void addCategory(Category category){
        categories = new Category[]{category};
    }

    public void processMissionCompletion(String userId, String missionId){
        
    }
}
