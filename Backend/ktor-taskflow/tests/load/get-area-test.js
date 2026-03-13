import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomString } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export const options = {
    stages: [
        { duration: '30s', target: 20000 },  // Разгон до 50 активных юзеров
        { duration: '1m', target: 25000 }, // Держим 100 юзеров (проверка стабильности)
        { duration: '1m', target: 20000 }, // Пиковая нагрузка 200 (проверка на отказ)
        { duration: '30s', target: 400 },   // Остывание
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
    const url = 'http://localhost:8080/api/v1/area/sync';


    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI4NzYyZWUwMC00MzE3LTQ1YjktOWFhNy1lMjYzYWFhZGY2MmEiLCJhdWQiOiJ0YXNrZmxvdy11c2VyIiwiaXNzIjoiaHR0cHM6Ly90YXNrZmxvdy5ydSIsInVzZXJfaWQiOiI2MTdiYjVmMS1iMjRiLTQwMjItYmQxZi01YzA5OTM5MWU4MGEiLCJ0eXBlIjoiQUNDRVNTIiwiZXhwIjoxNzc1OTExMTg2fQ.qE_A_fPmyq88vDbMJvjifLDYLQN8X4PqOztL-FHIbX8'
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