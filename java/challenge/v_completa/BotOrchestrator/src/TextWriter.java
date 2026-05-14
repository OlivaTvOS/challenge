public class TextWriter extends Response {
    private String aiModel;
    private int tokensUsed;
    private String userContext;
    private int rewriteCount;

    public TextWriter(String id, String catId, String text){
        super(id, catId, text);
    }

    @Override
    public String rewrite(){
        return rewrite();
    }

    public String getAiModel(){
        return aiModel;
    }

    public void setAiModel(String model){
        aiModel = model;
    }

    public int getTokensUsed(){
        return tokensUsed;
    }

    public void setUserContext(String context){
        userContext = context;
    }
}
