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

<img src="https://teixeira983-images.s3.amazonaws.com/voting-session-api-documentation.png" alt="Screenshot of the Swagger UI documentation">

## Qualidade de código
Para analisar a qualidade de código, foi usado o Sonarcloud.
<br>
Você pode verificar a última análise do código através <a href="https://sonarcloud.io/dashboard?id=iwhrim_VotingSessionAPI-Project">desse</a> link.

<img src="https://teixeira983-images.s3.amazonaws.com/code_quality-voting-session-api.png" alt="Screenshot of the Swagger UI documentation">
Obs: Screenshot tirado em 02/05/2021 as 19h 30min. Para obter as mais recentes estatísticas, acesse o link informado anteriormente.

## Guia de como usar essa API
Nessa seção, irei demonstrar como usar essa API.
<br>
Primeiramente, precisamos criar um pauta. Na API, que foi desenvolvida em inglês, chamamos uma pauta de "agenda".
Também precisamos de uma duração dessa pauta (duration), porém não é obrigatória. Caso você não defina ela, por padrão a pauta terá a duração de 1 minuto.
Para fazer as requisições, irei usar o curl. Abaixo, segue o exemplo para criar uma agenda chamada "Aumento de 3% no imposto de internet durante a pandemia" com duração de 10 minutos:

```
curl --location --request POST 'https://github-api-microservice.herokuapp.com/agenda' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name":"Aumento de 3% no imposto de internet durante a pandemia",
    "duration": 10
}'
```

O resultado dessa requisição 