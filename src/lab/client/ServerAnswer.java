package lab.client;

import lab.util.commands.Commands;

public class ServerAnswer {

    private String answer;
    private Commands.CommandExecutionStatus status;
    private byte[] packedData;

    public ServerAnswer(){

    }

    public String getAnswer(){
        return answer;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public Commands.CommandExecutionStatus getStatus(){
        return status;
    }

    public void setStatus(Commands.CommandExecutionStatus status){
        this.status = status;
    }

    public byte[] getPackedData() {
        return packedData;
    }

    public void setPackedData(byte[] packedData) {
        this.packedData = packedData;
    }

}
