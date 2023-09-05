package com.crdc.duplicatas.service;

import com.crdc.duplicatas.exception.ValidacaoException;
import com.crdc.duplicatas.model.ArquivoModel;
import com.crdc.duplicatas.model.ResponseModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ValidacaoServiceTest {

    @Autowired
    private ValidacaoService validacaoService;

    @Test
    void validacaoSucesso() throws IOException {
        ArquivoModel arquivoModel = new ArquivoModel();
        String conteudo = "001Empresa A              1900010000010000          Empresa A\n" +
                "002C10000000010000000001234560000012345\n" +
                "002D200000000200000000012345612345600\n" +
                "002T300000000300000000012345623456789\n" +
                "003";
        arquivoModel.setConteudo(conteudo.getBytes());
        ResponseModel response = validacaoService.validacaoDuplicatas(arquivoModel);
        assertTrue(CollectionUtils.isEmpty(response.getErrors()));
    }

    @Test
    void validacaoErroA() throws IOException {
        ArquivoModel arquivoModel = new ArquivoModel();
        String conteudo = "001Empresa B 1900010000020000 Empresa B\n" +
                "002C10000000010000000001234560000012345\n" +
                "002D200000000200000000012345612345600\n" +
                "002T300000000300000000012345623456789\n" +
                "003XPTO";

        arquivoModel.setConteudo(conteudo.getBytes());
        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            validacaoService.validacaoDuplicatas(arquivoModel);
        });

        assertFalse(CollectionUtils.isEmpty(exception.getErrors()));
    }

    @Test
    void validacaoErroB() throws IOException {
        ArquivoModel arquivoModel = new ArquivoModel();
        String conteudo = "001Empresa C 1900010000030000 Empresa C\n" +
                "002C00000000000000000000000000000000000\n" +
                "002D00000000000000000000000000000000000\n" +
                "002T00000000000000000000000000000000000\n" +
                "003";

        arquivoModel.setConteudo(conteudo.getBytes());
        ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
            validacaoService.validacaoDuplicatas(arquivoModel)
        );

        assertFalse(CollectionUtils.isEmpty(exception.getErrors()));
    }
}
