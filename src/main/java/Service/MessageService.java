package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.*;

public class MessageService {

    public MessageDAO messageDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    public Message addMessage(Message message, AccountService accountService){

        if(message == null || message.message_text.length() > 255 || message.message_text.isEmpty() || !accountService.accountExists(message.getPosted_by())){
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public List<Message> getAllMessagesFromAccountId(int id){
        return messageDAO.getAllMessagesFromAccount(id);
    }

    public Message deleteMessageById(int id){
        return messageDAO.deleteMessage(id);
    }

    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }


    public Message updateMessageById(int id, String message_text){

        if(message_text.isEmpty() || message_text.length() > 255)
        {
            return null;
        }

       return messageDAO.updateMessage(id, message_text);
    }



}
