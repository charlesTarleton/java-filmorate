# java-filmorate
Template repository for Filmorate project.

###### После ТЗ 10.5 значения были изменены т.к. у автора не было представления о jdbcTemplate и будущем функционале в ТЗ 11

<details>
<summary>ТЗ 11.0</summary>

![](src/main/resources/filmorate_database_sheme.PNG)

<details>
    <summary>Запрос при добавлении фильма</summary>

        INSERT INTO films (film_name, film_description, film_release_date,
        film_duration, film_rate, adult_rate_id)  
        VALUES(?, ?, ?, ?, ?, ?);

        SELECT MAX(film_id)
        FROM films;
</details>
<details>
    <summary>Запрос при удалении фильма</summary>

        DELETE FROM films
        WHERE film_id = ?;
</details>
<details>
    <summary>Запрос при обновлении фильма</summary>

        UPDATE films 
        SET film_name = ?, film_description = ?, film_release_date = ?,
            film_duration = ?, film_rate = ?, adult_rate_id = ?
        WHERE film_id = ?;

        DELETE FROM films_genre
        WHERE film_id = ?;

        INSERT INTO films_genre (film_id, genre_id)
        VALUES (?, ?);

        SELECT f.*, ar.adult_rate_name
        FROM films AS f
        LEFT OUTER JOIN adult_rate AS ar ON f.adult_rate_id = ar.adult_rate_id
        WHERE f.film_id = ?;
</details>
<details>
    <summary>Запрос на добавление пользователя</summary>

        INSERT INTO users (user_email, user_login, user_name, user_birthday)
        VALUES (?, ?, ?, ?);

        SELECT MAX(user_id)
        FROM users;
</details>
<details>
    <summary>Запрос на удаление пользователя</summary>

        DELETE FROM users
        WHERE user_id = ?;
</details>
<details>
    <summary>Запрос на обновление пользователя</summary>

        UPDATE users
        SET user_email = ?, user_login = ?, user_name = ?, user_birthday = ? 
        WHERE user_id = ?;

        SELECT * 
        FROM users 
        WHERE user_id = ?;
</details>
</details>