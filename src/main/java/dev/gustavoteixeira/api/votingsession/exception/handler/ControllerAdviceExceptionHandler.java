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

import static dev.gustavoteixeira.api.votingsession.constants.ErrorMessages.*;

@ControllerAdvice
public class ControllerAdviceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {AgendaAlreadyExistsException.class})
    public ResponseEntity<String> handleAgendaAlreadyExistsException(AgendaAlreadyExistsException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(AGENDA_ALREADY_EXISTS.getMessage());
    }

    @ExceptionHandler(value
            = {AgendaIsAlreadyOpenException.class})
    public ResponseEntity<String> handleAgendaIsAlreadyOpenException(AgendaIsAlreadyOpenException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE).body(AGENDA_IS_ALREADY_OPEN.getMessage());
    }

    @ExceptionHandler(value
            = {AgendaHasAlreadyBeenClosedException.class})
    public ResponseEntity<String> handleAgendaHasAlreadyBeenClosedException(AgendaHasAlreadyBeenClosedException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE).body(AGENDA_HAS_ALREADY_BEEN_CLOSED.getMessage());
    }

    @ExceptionHandler(value
            = {VoteAlreadyExistsException.class})
    public ResponseEntity<String> handleVoteAlreadyExistsException(VoteAlreadyExistsException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE).body(VOTE_ALREADY_EXISTS.getMessage());
    }

    @ExceptionHandler(value
            = {AgendaNotFoundException.class})
    public ResponseEntity<String> handleAgendaNotFoundException(AgendaNotFoundException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND).body(AGENDA_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(value
            = {AgendaClosedException.class})
    public ResponseEntity<String> handleAgendaClosedException(AgendaClosedException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE).body(AGENDA_CLOSED.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(METHOD_ARGUMENT_NOT_VALID.getMessage());
    }

    @ExceptionHandler(value
            = {AssociateIsNotAbleToVoteException.class})
    public ResponseEntity<String> handleAssociateIsNotAbleToVoteException(AssociateIsNotAbleToVoteException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN).body(ASSOCIATE_IS_NOT_ABLE_TO_VOTE.getMessage());
    }

}
