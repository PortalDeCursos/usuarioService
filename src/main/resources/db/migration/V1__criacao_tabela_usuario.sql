-- =========================================
-- ENUMS
-- =========================================

CREATE TYPE tipo_usuario AS ENUM (
    'ADMIN',
    'PROFESSOR'
);

CREATE TYPE titulacao AS ENUM (
    'GRADUACAO',
    'ESPECIALIZACAO',
    'MESTRADO',
    'DOUTORADO',
    'POS_DOUTORADO'
);


-- =========================================
-- USUARIO
-- =========================================

CREATE TABLE usuario (
     id BIGSERIAL PRIMARY KEY,
     nome VARCHAR(255) NOT NULL,
     email VARCHAR(255) NOT NULL UNIQUE,
     username VARCHAR(255) NOT NULL UNIQUE,
     senha VARCHAR(255) NOT NULL,
     tipo_usuario tipo_usuario NOT NULL
);


-- =========================================
-- PROFESSOR
-- =========================================

CREATE TABLE professor (
   id BIGSERIAL PRIMARY KEY,

   usuario_id BIGINT NOT NULL,
   curso_id BIGINT NOT NULL,

   titulacao titulacao NOT NULL,
   descricao VARCHAR(255),

   CONSTRAINT fk_professor_usuario
       FOREIGN KEY (usuario_id)
           REFERENCES usuario(id),

   CONSTRAINT fk_professor_curso
       FOREIGN KEY (curso_id)
           REFERENCES curso(id)
);