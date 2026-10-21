
CREATE TABLE usuario (
     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     nome VARCHAR(255) NOT NULL,
     email VARCHAR(255) NOT NULL UNIQUE,
     senha VARCHAR(255) NOT NULL,
     role_usuario VARCHAR(50) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela Aluno
CREATE TABLE aluno (
   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   usuario_id BIGINT NOT NULL,
   curso_id BIGINT NOT NULL,
   CONSTRAINT fk_aluno_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON DELETE CASCADE
);

-- Tabela Professor
CREATE TABLE professor (
       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       usuario_id BIGINT NOT NULL,
       curso_id BIGINT NOT NULL,
       titulacao VARCHAR(50) NOT NULL,

       descricao TEXT,
       CONSTRAINT fk_professor_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON DELETE CASCADE
);

-- Tabela Coordenador
CREATE TABLE coordenador (
         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
         professor_id BIGINT NOT NULL,
         curso_id BIGINT NOT NULL,
         CONSTRAINT fk_coordenador_professor FOREIGN KEY (professor_id) REFERENCES professor (id) ON DELETE CASCADE
);