INSERT INTO usuarios (cadastro, email, nome, senha, status) 
	SELECT cadastro, email, nome, senha, status
	FROM (SELECT NOW() as cadastro, 'admin@email.com.br' as email, 'Admin' as nome, '$2a$10$/ENYaePxc9k29E01qs972uWoo15SDmJFZUMn3z1IRhEVrRiRbCQ8G' as senha, 1 as status) u
	WHERE NOT EXISTS (SELECT 1 FROM usuarios us WHERE us.email = 'admin@email.com.br');