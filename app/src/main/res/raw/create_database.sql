CREATE TABLE IF NOT EXISTS task ( _id integer primary key autoincrement, title text not null, description text not null, tomatoes int not null default 0, done integer not null default 0);
INSERT INTO task (title, description, tomatoes) VALUES ('Fazer Backup', 'Fazer backup dos arquivos do PC', 1);
INSERT INTO task (title, description, tomatoes) VALUES ('Formatar o PC', 'Formatar o PC e instalar o Linux', 3);
