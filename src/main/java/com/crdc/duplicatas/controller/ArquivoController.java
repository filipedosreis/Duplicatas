package com.crdc.duplicatas.controller;

import com.crdc.duplicatas.enums.Status;
import com.crdc.duplicatas.exception.ValidacaoException;
import com.crdc.duplicatas.model.ArquivoModel;
import com.crdc.duplicatas.model.ResponseModel;
import com.crdc.duplicatas.service.ArquivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

import static com.crdc.duplicatas.util.MensagensUtil.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/arquivo")
@CrossOrigin(origins = "http://localhost:4200")
public class ArquivoController {

    @Autowired
    private ArquivoService arquivoService;

    @GetMapping("/arquivos")
    Collection<ArquivoModel> arquivos() {
        return arquivoService.getArquivos();
    }

    @GetMapping("/{id}")
    Collection<ArquivoModel> arquivo(@PathVariable Long id) {
        return arquivoService.getArquivos();
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    ArquivoModel uploadArquivo(@RequestParam("file") MultipartFile multipartArquivo) throws Exception {
        return arquivoService.upload(multipartArquivo);
    }

    @PostMapping("/validar/{id}")
    ResponseEntity<ResponseModel> validarArquivo(@PathVariable Long id) throws Exception {
        ResponseModel responseModel = new ResponseModel();
        try {
            arquivoService.validar(id);
            responseModel.setStatus(Status.success);
            responseModel.setMessage(MENSAGEM_VALIDACAO_SUCESSO);
        } catch (Exception e) {
            responseModel.setStatus(Status.error);
            if (e instanceof ValidacaoException) {
                if (!CollectionUtils.isEmpty(((ValidacaoException) e).getErrors())) {
                    responseModel.setMessage(MENSAGEM_VALIDACAO_ERRO);
                    responseModel.setErrors(((ValidacaoException) e).getErrors());
                }

                if (responseModel.getMessage() == null) {
                    responseModel.setMessage(MENSAGEM_VALIDACAO_ERRO_GENERICO);
                }

                return new ResponseEntity<>(
                        responseModel,
                        HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>(
                responseModel,
                HttpStatus.OK);
    }

    @PostMapping("/enviar/{id}")
    ResponseEntity<ResponseModel> enviarArquivo(@PathVariable Long id) throws Exception {
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel = arquivoService.enviar(id);
            responseModel.setMessage(MENSAGEM_ENVIO_SUCESSO);
        } catch (Exception e) {
            responseModel.setStatus(Status.error);
            if (e instanceof ValidacaoException) {
                if (responseModel.getMessage() == null) {
                    responseModel.setMessage(MENSAGEM_ENVIO_ERRO_GENERICO);
                }

                return new ResponseEntity<>(
                        responseModel,
                        HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>(
                responseModel,
                HttpStatus.OK);
    }
}
