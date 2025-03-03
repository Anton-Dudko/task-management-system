# Task Management System
## (Система управления заданиями)

Тестовое задание.

*****************************
### Описание задания:

Требования:
1.	Сервис должен поддерживать аутентификацию и авторизацию пользователей по email и паролю.
2.	Доступ к API должен быть аутентифицирован с помощью JWT токена.
3.	Создать ролевую систему администратора и пользователей.
4.	Администратор может управлять всеми задачами: создавать новые, редактировать существующие, просматривать и удалять, менять статус и приоритет, назначать исполнителей задачи, оставлять комментарии.
5.	Пользователи могут управлять своими задачами, если указаны как исполнитель: менять статус, оставлять комментарии.
6.	API должно позволять получать задачи конкретного автора или исполнителя, а также все комментарии к ним. Необходимо обеспечить фильтрацию и пагинацию вывода.
7.	Сервис должен корректно обрабатывать ошибки и возвращать понятные сообщения, а также валидировать входящие данные.
8.	Сервис должен быть хорошо задокументирован. API должен быть описан с помощью Open API и Swagger. В сервисе должен быть настроен Swagger UI. Необходимо написать README с инструкциями для локального запуска проекта. Дев среду нужно поднимать с помощью docker compose.
9.	Напишите несколько базовых тестов для проверки основных функций вашей системы.
10.	Используйте для реализации системы язык Java 17+, Spring, Spring Boot. В качестве БД можно использовать PostgreSQL или MySQL. Для реализации аутентификации и авторизации нужно использовать Spring Security. Можно использовать дополнительные инструменты, если в этом есть необходимость (например кэш).


## Запуск

* Проект можно запустить из корневой дирректории с помощью команды:

```
docker-compose up -d
```

Документацию можно посмотреть по путиЖ `http://localhost:8088/swagger-ui/index.html`.
На странице логина вводим данные одного из пользователей, описанных ниже, и получаем в ответе JWT токен, который необходимо добавить в форме авторизации.

В системе предусмотрено 2 пользователя :
1) email: admin@test.com, password: 1111, role: ADMIN
2) email: user@test.com, password: 1111, role: USER
