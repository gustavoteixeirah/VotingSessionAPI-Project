# Voting Session API

##### API para Sessão de Votação
Esse código é a minha implementação de um desafio técnico proposto por uma empresa para uma oportunidade como Desenvolvedor Back-End Java.

## O Desafio
No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. A partir disso, você precisa criar uma solução back-end para gerenciar essas sessões de votação.
<br>

### Requisitos
Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST:

1. Cadastrar uma nova pauta;
2. Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minutos por default);
3. Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta);
4. Contabilizar os votos e dar o resultado da votação na pauta.

## Documentação da API
A documentação da API está disponível no formato Swagger. O arquivo com a documentação pode localizado em ./voting-session.yml


## Qualidade de código
Para analisar a qualidade de código, foi usado o Sonarcloud.
<br>
Você pode verificar a última análise do código através <a href="https://sonarcloud.io/dashboard?id=iwhrim_VotingSessionAPI-Project">desse</a> link.

## Guia de como usar essa API
Nessa seção, irei demonstrar como usar essa API. Caso você já tenha olhado a documentação do Swagger e queira fazer requisições, sinta-se a vontade para usar a URL desse projeto em produção:

```
https://voting-session-api.herokuapp.com/
```

Primeiramente, precisamos criar um pauta. Na API, que foi desenvolvida em inglês, chamamos uma pauta de "agenda".
Também precisamos de uma duração dessa pauta (duration), porém não é obrigatória. Caso você não defina ela, por padrão a pauta terá a duração de 1 minuto.
Para fazer as requisições, irei usar o curl. Abaixo, segue o exemplo para criar uma agenda chamada "Aumento de 3% no imposto de internet durante a pandemia" com duração de 10 minutos:

```
curl --location --request POST 'https:/voting-session-api.herokuapp.com/agenda' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name":"Aumento de 3% no imposto de ianternet duraante a pandemia",
    "duration": 10
}'
```

O resultado dessa requisição vai ser os status 201 CREATED, e também vai ser retornado no header a localização dessa pauta, através do identificador:

```
https://voting-session-api.herokuapp.com/agenda/608f2b4fe422d16ab6d208ea
```

Com esse identificador, podemos fazer a requisição de GET, que retorna a pauta:

```
curl --location --request GET 'https:/voting-session-api.herokuapp.com/agenda/608f2b4fe422d16ab6d208ea' \
--header 'Content-Type: application/json'
```

O retorno é o objeto representando a agenda:

```
{
    "id": "608f2b4fe422d16ab6d208ea",
    "name": "Aumento de 3% no imposto de ianternet durante a pandemia",
    "startTime": null,
    "duration": 10,
    "positiveVotes": 0,
    "negativeVotes": 0,
    "opened": false
}
```

Podemos ver que a pauta está com o status "opened" como falso. Para abrirmos a pauta, dando inicio a possibilidade de votação nela, devemos chamar o endpoint de abertura de pauta:

```
curl --location --request PATCH 'https:/voting-session-api.herokuapp.com/agenda/608f2b4fe422d16ab6d208ea/start' \
--header 'Content-Type: application/json'
```

Com isso, a pauta estará aberta, e podemos votar nela. Podemos validar isso chamando novamente a requisição GET que retorna a pauta:

```
{
    "id": "608f2b4fe422d16ab6d208ea",
    "name": "Aumento de 3% no imposto de ianternet durante a pandemia",
    "startTime": "2021-05-02T22:46:56.075",
    "duration": 10,
    "positiveVotes": 0,
    "negativeVotes": 0,
    "opened": true
}
```

Perceba que agora o status "opened" está como true. Dessa maneira, podemos votar. Para isso, chamamos o endpoint de votação, informando o identificador da pauta, e um objeto no corpo da requisição descrevendo o nosso voto:

```
curl --location --request POST 'https:/voting-session-api.herokuapp.com/agenda/608f2b4fe422d16ab6d208ea/vote' \
--header 'Content-Type: application/json' \
--data-raw '{
"associate": "19839091069",
"choice": "Não"
}'
```

As possibilidades de voto são apenas "Sim" ou "Não", como descrito na documentação da API.
<br>
Se buscarmos as informações da pauta através do GET, veremos que o voto foi registrado:

```
{
    "id": "608f2b4fe422d16ab6d208ea",
    "name": "Aumento de 3% no imposto de ianternet durante a pandemia",
    "startTime": "2021-05-02T22:46:56.075",
    "duration": 10,
    "positiveVotes": 0,
    "negativeVotes": 1
    "opened": true
}
```

Após o tempo da pauta ser encerrado, o status "opened" vai mudar para false e então não será mais possível votar nessa pauta.

## Continuous Integration and Continuous Delivery
Sou um grande fã de CI/CD. Por isso, adicionei nesse projeto uma pipeline, utilizando o GitHub Actions, para executar ações automatizadas que facilitam a minha vida.
<br>
A pipeline é constituída das seguintes etapas:
1. Test: onde são executados os testes da aplicação;
2. Code Quality: onde o Sonarcloud faz a análise da qualidade do código e da cobertura de código;
3. Deploy to Heroku: essa etapa realiza o deploy automatizado da aplicação no Heroku;
4. Publish on Docker Hub: essa etapa cria o container da aplicação e então publica ela no Docker Hub
5. Notify: nessa, são duas possibilidades, se der tudo certo com as etapas anteriores, será notificado no meu Slack uma mensagem de sucesso, senão, uma mensagem de falha.

