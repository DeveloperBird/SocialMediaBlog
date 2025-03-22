package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    MessageService messageService = new MessageService();
    AccountService accountService = new AccountService();

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/login", this::loginAccountHandler);
        app.post("/register", this::registerAccountHandler);

        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageByAccountHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


    private  void registerAccountHandler(Context ctx) throws JsonProcessingException{
        Account account = ctx.bodyAsClass(Account.class);

        Account addedAccount = this.accountService.addAccount(account);

        Optional.ofNullable(addedAccount).ifPresentOrElse(ctx::json, () -> ctx.status(400));
    }

    private void loginAccountHandler(Context ctx)throws JsonProcessingException{

        Account account = ctx.bodyAsClass(Account.class);

        Optional.ofNullable(accountService.login(account))
                .ifPresentOrElse(ctx::json, () -> ctx.status(401));
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        Message message = ctx.bodyAsClass(Message.class);
        Message addedMessage = this.messageService.addMessage(message, accountService);

        Optional.ofNullable(addedMessage)
                .ifPresentOrElse(ctx::json, () -> ctx.status(400));
    }

    private void getAllMessagesHandler(Context ctx)throws JsonProcessingException{
        ctx.json(this.messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx)throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        ctx.json(Objects.requireNonNullElse(message, ""));
    }

    private void deleteMessageHandler(Context ctx)throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message deletedMessage = messageService.deleteMessageById(messageId);

        Optional.ofNullable(deletedMessage)
                .ifPresentOrElse(ctx::json, () -> ctx.json(""));
    }

    private void updateMessageHandler(Context ctx)throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message messageUpdate = ctx.bodyAsClass(Message.class);

        Message updatedMessage = messageService.updateMessageById(messageId, messageUpdate.getMessage_text());

        Optional.ofNullable(updatedMessage)
                .ifPresentOrElse(ctx::json, () -> ctx.status(400));
    }

    private void getMessageByAccountHandler(Context ctx)throws JsonProcessingException{
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesFromAccountId(accountId);
        ctx.json(messages);
    }

}