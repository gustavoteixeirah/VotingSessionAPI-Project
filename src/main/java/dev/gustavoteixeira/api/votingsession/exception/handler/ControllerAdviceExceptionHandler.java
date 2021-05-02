package dev.gustavoteixeira.api.votingsession.exception.handler;

import dev.gustavoteixeira.api.votingsession.exception.AgendaAlreadyExistsException;
import dev.gustavoteixeira.api.votingsession.exception.AgendaClosedException;
import dev.gustavoteixeira.api.votingsession.exception.AgendaNotFoundException;
import dev.gustavoteixeira.api.votingsession.exception.VoteAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdviceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {AgendaAlreadyExistsException.class})
    public ResponseEntity<String> handleAgendaAlreadyExistsException(AgendaAlreadyExistsException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body("Já existe uma pauta com esse nome.");
    }

    @ExceptionHandler(value
            = {VoteAlreadyExistsException.class})
    public ResponseEntity<String> handleVoteAlreadyExistsException(VoteAlreadyExistsException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE).body("Já existe um voto registrado desse associado para essa pauta.");
    }

    @ExceptionHandler(value
            = {AgendaNotFoundException.class})
    public ResponseEntity<String> handleAgendaNotFoundException(AgendaNotFoundException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND).body("Agenda não encontrada.");
    }

    @ExceptionHandler(value
            = {AgendaClosedException.class})
    public ResponseEntity<String> handleAgendaClosedException(AgendaClosedException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE).body("A votação para essa pauta já foi encerrada.");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body("Um ou mais parâmetros da requisição são inválidos.");
    }

}
