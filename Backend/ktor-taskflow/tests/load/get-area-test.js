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
    const url = 'http://localhost:8080/api/area/sync';


    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJkY2Q2OTk2Zi03ZTVmLTRkM2UtODdiZi1mZDRkNDVjZDY3YmMiLCJhdWQiOiJ0YXNrZmxvdy11c2VyIiwiaXNzIjoiaHR0cHM6Ly90YXNrZmxvdy5ydSIsInVzZXJfaWQiOiI2MTdiYjVmMS1iMjRiLTQwMjItYmQxZi01YzA5OTM5MWU4MGEiLCJ0eXBlIjoiQUNDRVNTIiwiZXhwIjoxNzc1OTQwMTUxfQ.hmeGLzbap2NgdykR22WQiKENivLaEvYxAzOvQtonP7M'
        },
    };

    const res = http.get(url, params);

    if (res.status !== 200) {
        console.log(`Error: Status ${res.status}, Body: ${res.body}`);
    }

    // Проверяем статус 201 (Created)
    check(res, {
        'is status 200': (r) => r.status === 200
    });

    sleep(0.1);
}