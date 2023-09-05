package com.crdc.duplicatas.service;

import com.crdc.duplicatas.enums.Status;
import com.crdc.duplicatas.model.ArquivoModel;
import com.crdc.duplicatas.model.DataModel;
import com.crdc.duplicatas.model.ResponseModel;
import com.crdc.duplicatas.model.TransactionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.crdc.duplicatas.service.ValidacaoService.REGISTRO_TRANSACAO;
import static com.crdc.duplicatas.service.ValidacaoService.TRANSACAO_PATTERN;

@Service
public class TransmissaoService {

    @Autowired
    private ValidacaoService validacaoService;

    public ResponseModel transmitir(ArquivoModel arquivoModel) throws IOException {
        Pattern transacaoPattern = TRANSACAO_PATTERN;
        ResponseModel responseModel = new ResponseModel();
        responseModel.setStatus(Status.success);
        responseModel.setData(new DataModel());
        responseModel.getData().setTransactions(new ArrayList<>());

        try (InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(arquivoModel.getConteudo()),
                StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {

            br.lines().forEach(linha -> {
                if (linha != null && !linha.isEmpty()) {
                    if (linha.startsWith(REGISTRO_TRANSACAO)) {
                        setTransacaoDetalhe(linha, responseModel, transacaoPattern);
                    }
                }
            });
        }

        return responseModel;
    }

    private void setTransacaoDetalhe(String linha, ResponseModel responseModel, Pattern transacaoPattern) {
        Matcher matcher = transacaoPattern.matcher(linha);
        if (matcher.matches()) {
            TransactionModel model = new TransactionModel();
            model.setType(matcher.group(2));
            model.setValue(NumberUtils.<Double>parseNumber(matcher.group(3), Double.class));
            model.setAccountOrigin(matcher.group(4));
            model.setAccountDestination(matcher.group(5));

            responseModel.getData().getTransactions().add(model);
        }
    }
}
