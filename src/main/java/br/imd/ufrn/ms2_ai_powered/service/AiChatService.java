package br.imd.ufrn.ms2_ai_powered.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class AiChatService {

    private final ChatClient chatClient;

    public AiChatService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        
        ChatMemoryRepository chatMemoryRepository = new InMemoryChatMemoryRepository();
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10)
                .build();
        
        this.chatClient = chatClientBuilder
                .defaultSystem("Você é um assistente virtual especialista em Sistemas Distribuídos " +
                               "da Universidade Federal do Rio Grande do Norte (UFRN). " +
                               "Use SEMPRE as informações fornecidas nos documentos de contexto para responder. " +
                               "Se não souber a resposta com base nos documentos, diga que não sabe.")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .build();
    }

    public String processarMensagem(String chatId, String mensagemUsuario) {
        return this.chatClient.prompt()
                .user(mensagemUsuario)
                .advisors(a -> a.param("chat_memory_conversation_id", chatId))
                .call()
                .content();
    }
}