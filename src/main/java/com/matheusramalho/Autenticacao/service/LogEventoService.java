package com.matheusramalho.Autenticacao.service;

import com.matheusramalho.Autenticacao.model.LogEvento;
import com.matheusramalho.Autenticacao.model.TipoEvento;
import com.matheusramalho.Autenticacao.repository.LogEventoRepository;
import org.springframework.stereotype.Service;

@Service
public class LogEventoService {

    private final LogEventoRepository logEventoRepository;

    public LogEventoService(LogEventoRepository logEventoRepository){
        this.logEventoRepository = logEventoRepository;
    }

    public void registrar(TipoEvento tipo, String username, String detalhe){
        LogEvento log = new LogEvento();
        log.setTipoEvento(tipo);
        log.setUsername(username);
        log.setDetalhe(detalhe);
        logEventoRepository.save(log);
    }
}
