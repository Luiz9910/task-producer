# Task - Docker Setup
Siga os passos abaixo para rodar a aplicação com Docker.

## Pré-requisitos
Antes de iniciar, certifique-se de que você tem o Docker instalado na sua máquina. Você pode verificar se o Docker está instalado com o seguinte comando:

```bash
docker --version
```

Se o Docker não estiver instalado, siga as instruções na documentação oficial do Docker para instalar a versão adequada para o seu sistema operacional.

## Rodar todos os containers
Comando para rodar a aplicação com o docker compose, assim tudo ambiente entrará em modo start

```bash
docker compose up
```

## Rodar e instalar todos os containers
Comando para fazer o build do docker compose e rodar a aplicação

```bash
docker compose up --build
```

## Derrubar os containers
Comando para derrubar os containers

```bash
docker compose down
```
