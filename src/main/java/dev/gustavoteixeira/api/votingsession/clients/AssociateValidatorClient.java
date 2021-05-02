package dev.gustavoteixeira.api.votingsession.clients;

import dev.gustavoteixeira.api.votingsession.dto.AssociateVotingStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "AssociateValidatorAPIClient", url = "https://user-info.herokuapp.com/users/")
public interface AssociateValidatorClient {

    @GetMapping(value = "/{cpf}")
    AssociateVotingStatusDTO getAssociateVotingStatus(@PathVariable String cpf);

}
