package br.imd.ufrn.ms2_ai_powered.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.imd.ufrn.ms2_ai_powered.service.AiChatService;

@RestController
@RequestMapping("/ia")
public class AiController {

    private final AiChatService aiChatService;

    public AiController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @GetMapping("/perguntar")
    public String fazerPergunta(
            @RequestParam(defaultValue = "chat-padrao") String chatId, 
            @RequestParam String mensagem) {
        
        return aiChatService.processarMensagem(chatId, mensagem);
    }
}