package com.crdc.duplicatas.service;

import com.crdc.duplicatas.enums.Acao;
import com.crdc.duplicatas.model.ArquivoModel;
import com.crdc.duplicatas.model.LogTransacaoModel;
import com.crdc.duplicatas.repository.LogTransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogTransacaoService {

    @Autowired
    private LogTransacaoRepository logTransacaoRepository;

    public void adicionarLog(ArquivoModel arquivoModel, Acao acao, String mensagem) {
        LogTransacaoModel log = new LogTransacaoModel();
        log.setMensagem(mensagem);
        log.setArquivo(arquivoModel);
        log.setAcao(acao);
        log.setCriadoEm(LocalDateTime.now());
        logTransacaoRepository.save(log);
    }

    public List<LogTransacaoModel> getAll() {
        return logTransacaoRepository.findAll();
    }
}
