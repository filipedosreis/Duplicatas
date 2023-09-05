package com.crdc.duplicatas.controller;

import com.crdc.duplicatas.model.LogTransacaoModel;
import com.crdc.duplicatas.service.LogTransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/log")
@CrossOrigin(origins = "http://localhost:4200")
public class LogArquivoController {

    @Autowired
    private LogTransacaoService logTransacaoService;

    @GetMapping("/logs")
    Collection<LogTransacaoModel> arquivos() {
        return logTransacaoService.getAll();
    }
}
