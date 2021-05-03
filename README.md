<h1 align="center"> Knowledge Base </h1>

<p align="center">
   <img src="https://img.shields.io/static/v1?label=issues&message=0&color=yellow&style=plastic"/>
  <img src="https://img.shields.io/static/v1?label=last%20commit&message=may&color=information&style=plastic"/>
  <img src="https://img.shields.io/static/v1?label=angular&message=framework&color=red&style=plastic&logo=ANGULAR"/>
  <img src="https://img.shields.io/static/v1?label=java&message=language&color=red&style=plastic&logo=JAVA"/>
  <img src="https://img.shields.io/static/v1?label=spring&message=framework&color=green&style=plastic&logo=SPRING"/>
  <img src="https://img.shields.io/static/v1?label=mysql&message=SGBD&color=blue&style=plastic&logo=MYSQL"/>
  <img src="http://img.shields.io/static/v1?label=license&message=MIT&color=green&style=plastic"/>
  <img src="http://img.shields.io/static/v1?label=status&message=em%20desenvolvimento&color=red&style=plastic"/>
</p>

> Status do Projeto: Em desenvolvimento :warning:

### Tópicos 

:small_blue_diamond: [Descrição do projeto](#descrição-do-projeto-star)

:small_blue_diamond: [Funcionalidades](#funcionalidades-checkered_flag)

:small_blue_diamond: [Tecnologias e dependências](#tecnologias-e-dependências)


## Descrição do Projeto :star:
<p align="justify">
Knowledge Base é um sistema de gerenciamento de artigos.

### Notas: 
- Apenas o administrador é responsável pela criação, edição e remoção de artigos, categorias e seções, além da criação de usuários.
- Na parte pública, fica disponível a visualização de todas as categorias, seções e artigos publicados.
- Os artigos possuem dois estados, sendo publish e draft.
- Cada artigo deve estar contido em um categoria, sendo a seção facultativa.
- Cada seção deve estar contida em uma categoria.
- Utilização do padrão MVC e API RESTful
</p>

## Funcionalidades :checkered_flag:
### Back-end
- [X] CRUD de artigos, categorias, tags e seções
- [X] Autenticar usuário
- [X] Gerenciar rotas públicas e privadas 
- [X] Listar artigos publicados(público), listar artigos publicados por categoria ou seção(público/privado), listar todos os artigos(privado)
- [X] Buscar por artigo(s)
- [X] Cadastrar usuário(privado)

## Tecnologias e dependências
  ### Front-end

  ### Back-end
  - Swagger
  - Ehcache
  - Spring Boot 
  - Spring Security
  - Spring Data JPA
  - jjwt
  - Flyway

  ### Database
  - MySQL  

  ## Licença 
  The [MIT License](https://github.com/JoaoVictorfss/knowledge-base/blob/master/LICENSE) (MIT)

  Copyright :copyright: 2021 - kowledge base
