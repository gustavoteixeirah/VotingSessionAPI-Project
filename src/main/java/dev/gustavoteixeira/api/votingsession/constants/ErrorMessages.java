package dev.gustavoteixeira.api.votingsession.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorMessages {

    AGENDA_ALREADY_EXISTS("Já existe uma pauta com esse nome."),
    AGENDA_IS_ALREADY_OPEN("Essa pauta já está aberta."),
    AGENDA_HAS_ALREADY_BEEN_CLOSED("Essa pauta já foi encerrada."),
    AGENDA_NOT_FOUND("Pauta não encontrada."),
    AGENDA_CLOSED("A votação para essa pauta já foi encerrada."),

    ASSOCIATE_IS_NOT_ABLE_TO_VOTE("O associado não está apto a votar."),

    METHOD_ARGUMENT_NOT_VALID("Um ou mais parâmetros da requisição são inválidos."),

    VOTE_ALREADY_EXISTS("Já existe um voto registrado desse associado para essa pauta.");

    @Getter
    private final String message;
}
