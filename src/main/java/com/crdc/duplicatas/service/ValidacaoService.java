package com.crdc.duplicatas.service;

import com.crdc.duplicatas.enums.Status;
import com.crdc.duplicatas.enums.TipoTransacao;
import com.crdc.duplicatas.exception.ValidacaoException;
import com.crdc.duplicatas.model.ArquivoModel;
import com.crdc.duplicatas.model.ErrorModel;
import com.crdc.duplicatas.model.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.crdc.duplicatas.util.MensagensUtil.*;

@Service
public class ValidacaoService {

    public static final String REGISTRO_CABECALHO = "001";
    public static final String REGISTRO_TRANSACAO = "002";
    public static final String REGISTRO_RODAPE = "003";
    public static final Pattern TRANSACAO_PATTERN = Pattern.compile("^(002)(C|D|T{1})([0-9]{15})([0-9]{15})([0-9]*$)");
    private static final Pattern TRANSACAO_CAMPOS_PATTERN = Pattern.compile("^(\\d{3})(.{1})(.{15})(.{15})(.{1,15})");

    private final Logger log = LoggerFactory.getLogger(ValidacaoService.class);

    public ResponseModel validacaoDuplicatas(ArquivoModel arquivo) throws IOException {
        List<ErrorModel> erros = new ArrayList<>();
        try (InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(arquivo.getConteudo()),
                StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {

            AtomicReference<Boolean> cabecalho = new AtomicReference<>(true);
            AtomicReference<Boolean> rodape = new AtomicReference<>(false);
            AtomicInteger i = new AtomicInteger(0);

            br.lines().forEach(linha -> {
                i.getAndIncrement();
                if (linha != null && !linha.isEmpty()) {
                    if (linha.startsWith(REGISTRO_CABECALHO)) {
                        validaCabecalho(linha, cabecalho, i.get(), erros);
                    } else if (linha.startsWith(REGISTRO_TRANSACAO)) {
                        validaTransacao(linha, erros, cabecalho.get(), rodape.get(), i.get());

                    } else if (linha.startsWith(REGISTRO_RODAPE)) {
                        validaRodape(linha, rodape, i.get(), erros);
                    } else {
                        addErrorModel(i.get(), MENSAGEM_VALIDACAO_ERRO_IDENTIFICACAO_TRANSACAO, erros);
                    }
                }
            });

            if (!rodape.get()) {
                log.warn("Rodape não encontrado");
                throw new ValidacaoException(null, null);
            }
        }

        if (!CollectionUtils.isEmpty(erros)) {
            throw new ValidacaoException(null, erros);
        }

        ResponseModel responseModel = new ResponseModel();
        responseModel.setStatus(Status.success);
        return responseModel;

    }

    private void addErrorModel(int linha, String mensagem, List<ErrorModel> erros) {
        ErrorModel errorModel = new ErrorModel();
        errorModel.setLine(linha);
        errorModel.setError(mensagem);
        erros.add(errorModel);
    }

    private void validaCabecalho(final String linha, AtomicReference<Boolean> cabecalho, int numeroLinha, List<ErrorModel> erros) {
//        001 - Registro de Cabeçalho
//        - Posições 1-3: Identificação do tipo de registro (001)
//        - Posições 4-33: Razão social da empresa
//        - Posições 34-47: Identificador da empresa
//        - Posições 48-80: Espaço reservado para uso futuro
        if (!cabecalho.get()) {
            log.warn("Cabeçalho fora de posição");
            addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_CABECALHO_FORA_POSICAO, erros);
        }
        cabecalho.set(Boolean.FALSE);
    }

    private void validaTransacao(final String linha, List<ErrorModel> erros, Boolean cabecalho, Boolean rodape, int numeroLinha) {
//        002 - Registro de Transação
//        - Posições 1-3: Identificação do tipo de registro (002)
//        - Posição 4: Tipo de transação (C para Crédito, D para Débito, T para Transferência)
//        - Posições 5-20: Valor da transação (formato decimal, sem ponto decimal)
//        - Posições 21-36: Conta origem
//        - Posições 37-52: Conta destino

        if (cabecalho || rodape) {
            log.warn("Cabeçalho ou rodape fora de posição");
            throw new ValidacaoException(null, null);
        }

        Matcher matcher = TRANSACAO_PATTERN.matcher(linha);
        if (matcher.matches()) {
            validaCamposMatchPattern(matcher, numeroLinha, erros);
            return;
        }

        matcher = TRANSACAO_CAMPOS_PATTERN.matcher(linha);
        if (matcher.matches()) {
            int qntCampos = 5;
            if (matcher.groupCount() == qntCampos) {
                validaCamposMatchPattern(matcher, numeroLinha, erros);
            }
        }

        if (linha.length() > 38) {
            String tipoTransacao = linha.substring(3, 4);
            String valorTransacao = linha.substring(5, 20);
            String contaOrigem = linha.substring(21, 36);
            String contaDestino = linha.substring(37);

            validaTipoTransacao(tipoTransacao, numeroLinha, erros);
            validaValor(valorTransacao, numeroLinha, erros);
            validaContaOrigem(contaOrigem, numeroLinha, erros);
            validaContaDestino(contaDestino, numeroLinha, erros);
        } else {
            addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_REGISTRO_TRANSACAO, erros);
        }

    }

    private void validaRodape(final String linha, AtomicReference<Boolean> rodape, int numeroLinha, List<ErrorModel> erros) {
//        003 - Registro de Rodapé
//        - Posições 1-3: Identificação do tipo de registro (003)
//        - Posições 4-80: Espaço reservado para informações de totalização ou controle
        if (rodape.get()) {
            log.warn("Rodape fora de posicao");
            addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_RODAPE_FORA_POSICAO, erros);
        }

        if (linha.length() > 3 && !linha.substring(3).trim().isEmpty()) {
            addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_RODAPE_ESPACO_RESERVADO, erros);
        }

        rodape.set(true);
    }

    private void validaCamposMatchPattern(Matcher matcher, int numeroLinha, List<ErrorModel> erros) {
        String tipoTransacao = matcher.group(2);
        String valorTransacao = matcher.group(3);
        String contaOrigem = matcher.group(4);
        String contaDestino = matcher.group(5);

        validaTipoTransacao(tipoTransacao, numeroLinha, erros);
        validaValor(valorTransacao, numeroLinha, erros);
        validaContaOrigem(contaOrigem, numeroLinha, erros);
        validaContaDestino(contaDestino, numeroLinha, erros);
    }

    private void validaTipoTransacao(String tipoTransacao, int numeroLinha, List<ErrorModel> erros) {
        if (Arrays.stream(TipoTransacao.values()).noneMatch(x -> x.toString().equalsIgnoreCase(tipoTransacao))) {
            addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_TP_TRANSACAO, erros);
        }

    }

    private void validaValor(String valorTransacao, int numeroLinha, List<ErrorModel> erros) {
        if (valorTransacao.isEmpty()) {
            addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_VALOR_NULO, erros);
        } else {
            try {
                Long valor = NumberUtils.<Long>parseNumber(valorTransacao, Long.class);
                if (valor == 0) {
                    addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_VALOR_NULO, erros);
                }
            } catch (Exception e) {
                addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_FORA_FORMATO, erros);
            }
        }
    }

    private void validaContaOrigem(String contaOrigem, int numeroLinha, List<ErrorModel> erros) {
        if (contaOrigem.isEmpty()) {
            addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_CONTA_ORIGEM, erros);
            return;
        }

        try {
            Long valor = NumberUtils.<Long>parseNumber(contaOrigem, Long.class);
            if (valor == 0) {
                addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_CONTA_ORIGEM_OBRIGATORIA, erros);
            }
        } catch (Exception e) {
            addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_CONTA_ORIGEM, erros);
        }
    }

    private void validaContaDestino(String contaDestino, int numeroLinha, List<ErrorModel> erros) {
        if (contaDestino.isEmpty()) {
            addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_CONTA_DESTINO, erros);
        }

        try {
            Long valor = NumberUtils.<Long>parseNumber(contaDestino, Long.class);
            if (valor == 0) {
                addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_CONTA_DESTINO_OBRIGATORIA, erros);
            }
        } catch (Exception e) {
            addErrorModel(numeroLinha, MENSAGEM_VALIDACAO_ERRO_CONTA_DESTINO, erros);
        }
    }
}
