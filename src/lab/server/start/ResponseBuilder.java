package lab.server.start;

public class ResponseBuilder{

    private StringBuilder stringBuilder = new StringBuilder();

    public ResponseBuilder(){

    }

    public void append(String string){
        stringBuilder.append(string);
        stringBuilder.append('\n');
    }

    public String toString(){
        return stringBuilder.toString();
    }

}
