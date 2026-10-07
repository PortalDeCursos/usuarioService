
-- =========================================
-- USUARIO
-- =========================================

CREATE TABLE usuario (
     id BIGSERIAL PRIMARY KEY,
     nome VARCHAR(255) NOT NULL,
     email VARCHAR(255) NOT NULL UNIQUE,
     username VARCHAR(255) NOT NULL UNIQUE,
     senha VARCHAR(255) NOT NULL,
     tipo_usuario VARCHAR(255) NOT NULL,
     criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP

);


-- =========================================
-- PROFESSOR
-- =========================================

CREATE TABLE professor (
   id BIGSERIAL PRIMARY KEY,

   usuario_id BIGINT NOT NULL,
   curso_id BIGINT NOT NULL,

   titulacao VARCHAR(255) NOT NULL,
   descricao VARCHAR(255),

   CONSTRAINT fk_professor_usuario
       FOREIGN KEY (usuario_id)
           REFERENCES usuario(id)

);