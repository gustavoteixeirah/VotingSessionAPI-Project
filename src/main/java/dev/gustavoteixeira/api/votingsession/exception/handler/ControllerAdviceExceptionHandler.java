package dev.gustavoteixeira.api.votingsession.exception.handler;

import dev.gustavoteixeira.api.votingsession.exception.*;
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
            = {AgendaIsAlreadyOpenException.class})
    public ResponseEntity<String> handleAgendaIsAlreadyOpenException(AgendaIsAlreadyOpenException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE).body("Essa pauta já está aberta.");
    }

    @ExceptionHandler(value
            = {AgendaHasAlreadyBeenClosedException.class})
    public ResponseEntity<String> handleAgendaHasAlreadyBeenClosedException(AgendaHasAlreadyBeenClosedException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE).body("Essa pauta já foi encerrada.");
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
                .status(HttpStatus.NOT_FOUND).body("Pauta não encontrada.");
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

    @ExceptionHandler(value
            = {AssociateIsNotAbleToVoteException.class})
    public ResponseEntity<String> handleAssociateIsNotAbleToVoteException(AssociateIsNotAbleToVoteException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN).body("O associado não está apto a votar.");
    }

}
