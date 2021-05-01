package dev.gustavoteixeira.api.votingsession.exception.handler;

import dev.gustavoteixeira.api.votingsession.exception.AgendaAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdviceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {AgendaAlreadyExistsException.class})
    public ResponseEntity<String> handleAgendaAlreadyExistsExceptionL(AgendaAlreadyExistsException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body("Já existe uma pauta com esse nome. ");
    }

}
