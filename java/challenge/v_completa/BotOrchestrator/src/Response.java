public abstract class Response {
    private String id;
    private String categoryId;
    private String baseText;
    private String responseType;
    protected String createdAt;

    public Response(String id, String catId, String text){
        this.id = id;
        this.categoryId = catId;
        this.baseText = text;
    }

    public String getId(){
        return id;
    }

    public String getCategoryId(){
        return categoryId;
    }

    public String getBaseText(){
        return baseText;
    }

    public String getResponseType(){
        return responseType;
    }

    public String getCreatedAt(){
        return createdAt;
    }

    protected abstract String rewrite(){

    }

    public void setResponseType(String type){
        responseType = type;
    }
}
