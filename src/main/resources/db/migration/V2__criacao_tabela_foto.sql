CREATE TABLE foto_perfil (
     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     usuario_id BIGINT NOT NULL UNIQUE,
     caminho_arquivo VARCHAR(500) NOT NULL,
     criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

     CONSTRAINT fk_foto_perfil_usuario
         FOREIGN KEY (usuario_id)
             REFERENCES usuario(id)
             ON DELETE CASCADE
);