package com.crdc.duplicatas.service;

import com.crdc.duplicatas.enums.Acao;
import com.crdc.duplicatas.model.ArquivoModel;
import com.crdc.duplicatas.model.ResponseModel;
import com.crdc.duplicatas.repository.ArquivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArquivoService {

    @Autowired
    private ArquivoRepository arquivoRepository;
    @Autowired
    private ValidacaoService validacaoService;
    @Autowired
    private LogTransacaoService logTransacaoService;
    @Autowired
    private TransmissaoService transmissaoService;

    public List<ArquivoModel> getArquivos() {
        return arquivoRepository.findAll();
    }

    public ArquivoModel getById(Long id) {
        return arquivoRepository.getReferenceById(id);
    }

    public ArquivoModel saveArquivo(ArquivoModel arquivo) {
        return arquivoRepository.save(arquivo);
    }

    public ArquivoModel upload(MultipartFile multipartArquivo) throws IOException {
        String nome = multipartArquivo.getOriginalFilename();
        byte[] conteudo = multipartArquivo.getBytes();

        ArquivoModel arquivo = new ArquivoModel();
        arquivo.setNome(nome);
        arquivo.setConteudo(conteudo);
        arquivo.setCriadoEm(LocalDateTime.now());
        ArquivoModel arquivoModel = arquivoRepository.save(arquivo);
        logTransacaoService.adicionarLog(arquivoModel, Acao.ARQUIVO_CRIADO, null);
        return arquivoModel;
    }

    public ResponseModel validar(Long id) {
        ArquivoModel arquivoModel = getById(id);
        try {
            ResponseModel responseModel = validacaoService.validacaoDuplicatas(arquivoModel);
            if (CollectionUtils.isEmpty(responseModel.getErrors())) {
                arquivoModel.setValido(Boolean.TRUE);
                arquivoModel.setValidadoEm(LocalDateTime.now());
                saveArquivo(arquivoModel);
            }
            logTransacaoService.adicionarLog(arquivoModel, Acao.ARQUIVO_VALIDACAO_SUCESSO, null);
            return responseModel;
        } catch (IOException e) {
            logTransacaoService.adicionarLog(arquivoModel, Acao.ARQUIVO_VALIDACAO_ERRO, null);
            throw new RuntimeException(e);
        }
    }

    public ResponseModel enviar(Long id) {
        ArquivoModel arquivoModel = getById(id);
        try {
            ResponseModel responseModel = transmissaoService.transmitir(arquivoModel);
            if (CollectionUtils.isEmpty(responseModel.getErrors())) {
                arquivoModel.setEnviado(Boolean.TRUE);
                arquivoModel.setEnviadoEm(LocalDateTime.now());
                saveArquivo(arquivoModel);
            }
            logTransacaoService.adicionarLog(arquivoModel, Acao.TRANSACAO_AUTORIZADA, null);
            return responseModel;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}