# Voting Session API
##### API para Sessão de Votação
Esse código é a minha implementação de um desafio técnico proposto por uma empresa para uma oportunidade como Desenvolvedor Back-End Java.

## O Desafio
No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. A partir disso, você precisa criar uma solução back-end para gerenciar essas sessões de votação.

<p>

### Requisitos

Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST:

1. Cadastrar uma nova pauta;
2. Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minutos por default);
3. Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta);
4. Contabilizar os votos e dar o resultado da votação na pauta.

## Documentação da API

A documentação da API está disponível no formato Swagger. O arquivo com a documentação pode localizado em ./voting-session.yml

<img src="https://teixeira983-images.s3.amazonaws.com/voting-session-api-documentation.png" alt="Screenshot of the Swagger UI documentation">

