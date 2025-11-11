-- Script de inicialização do banco de dados
-- Cria os dois databases necessários para os microserviços

CREATE DATABASE IF NOT EXISTS barberApp_auth;
CREATE DATABASE IF NOT EXISTS barberApp_schedules;

-- Concede permissões ao usuário root
GRANT ALL PRIVILEGES ON barberApp_auth.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON barberApp_schedules.* TO 'root'@'%';

FLUSH PRIVILEGES;
