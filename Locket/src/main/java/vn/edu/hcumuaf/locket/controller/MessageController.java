//package vn.edu.hcumuaf.locket.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.annotation.SendToUser;
//import org.springframework.web.bind.annotation.*;
//import vn.edu.hcumuaf.locket.model.Message;
//import vn.edu.hcumuaf.locket.service.MessageService;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//@RestController
//@RequestMapping("api/messages")
//public class MessageController {
//    @Autowired
//    private MessageService messageService;
//
//    //CompletableFuture dùng để xử lí bất đồng bộ và lập trình phản ứng một cách dễ dàng và mạnh mẽ
//    @PostMapping("send/{chatId}")
//    @SendTo("/topic/{chatId}")
////    public CompletableFuture<String> sendMessage(@PathVariable("chatId") String chatId, @RequestBody Message message) {
////        return messageService.sendMessage(chatId, message);
////    }
//    public CompletableFuture<String> sendMessage(@PathVariable("chatId") String chatId, @RequestBody Message message) {
//        return messageService.sendMessage( message);
//    }
//
//    @GetMapping("{chatId}")
//    public CompletableFuture<List<Message>> getMessage(@PathVariable("chatId") String chatId) {
//        return messageService.getMessages(chatId);
//    }
//}
