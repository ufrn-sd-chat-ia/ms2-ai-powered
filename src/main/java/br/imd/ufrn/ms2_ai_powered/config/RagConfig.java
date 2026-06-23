package br.imd.ufrn.ms2_ai_powered.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class RagConfig {

    @Value("classpath:conhecimento.txt")
    private Resource documento;

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel)
            .build();

        TextReader textReader = new TextReader(documento);
        List<Document> documentosLidos = textReader.get();

        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> pedacosDeTexto = splitter.apply(documentosLidos);

        vectorStore.add(pedacosDeTexto);

        return vectorStore;
    }
}