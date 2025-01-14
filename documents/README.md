# Процедура запуска автотестов

## Подключение к MySQL

1. Открыть Docker Desktop
2. Открыть проект в IDEA
3. В первом терминале запустить контейнеры, через команду: `docker-compose up -d`
4. Открыть второй терминал и запустить приложение, через команду: `java -jar .\artifacts\aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app`
5. Открыть третий терминал и запустить тесты, через команду: `.\gradlew clean test -DdbUrl=jdbc:mysql://localhost:3306/app`
6. Сгенерировать отчёт Allure, через команду: `.\gradlew allureServe`
7. Просмотреть отчёт в браузере, после просмотра закрыть его
8. Во втором терминале остановить приоржени, через сочетание клавишь: `CTRL + C`
9. В первом терминале остановить контейнеры, через команду: `docker-compose down`

## Подключение к PostgreSQL

1. Открыть Docker Desktop
2. Открыть проект в IDEA
3. В первом терминале запустить контейнеры, через команду: `docker-compose up -d`
4. Открыть второй терминал и запустить приложение, через команду: `java -jar .\artifacts\aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app`
5. Открыть третий терминал и запустить тесты, через команду: `.\gradlew clean test -DdbUrl=jdbc:postgresql://localhost:5432/app`
6. Сгенерировать отчёт Allure, через команду: `.\gradlew allureServe`
7. Просмотреть отчёт в браузере, после просмотра закрыть его
8. Во втором терминале остановить приоржени, через сочетание клавишь: `CTRL + C`
9. В первом терминале остановить контейнеры, через команду: `docker-compose down`