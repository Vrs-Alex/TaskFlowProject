import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomString } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export const options = {
    stages: [
        { duration: '30s', target: 1000 },  // Разгон до 50 активных юзеров
        { duration: '1m', target: 1500 }, // Держим 100 юзеров (проверка стабильности)
        { duration: '1m', target: 1500 }, // Пиковая нагрузка 200 (проверка на отказ)
        { duration: '30s', target: 1000 },   // Остывание
    ],
    thresholds: {
        // Ожидаем успех в 99% случаев.
        // Если база начнет выдавать ошибки из-за таймаутов, тест пометится как проваленный.
        http_req_failed: ['rate<0.01'],

        // p(95) < 750мс.
        // Мы закладываем 450мс на хеш + 300мс на сеть, БД и накладные расходы Ktor.
        http_req_duration: ['p(95)<750'],
    },
};

export default function () {
    const url = 'http://localhost:8080/api/register';

    // Генерация уникальных данных для каждого прохода
    const uniqueId = `vrs1_${__VU}_${__ITER}_${randomString(3)}`;

    const payload = JSON.stringify({
        email: `${uniqueId}@test.com`,
        username: uniqueId,
        password: "123Alex_123KJBDKCWC;LWCdsbfnej;kvce-_"
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);

    // Проверяем статус 201 (Created)
    check(res, {
        'is status 201': (r) => r.status === 201,
        'has token in response': (r) => r.json('accessToken') !== undefined,
    });

    // Небольшая пауза, имитирующая реальное поведение (человек не жмет кнопку 100 раз в секунду)
    sleep(0.1);
}