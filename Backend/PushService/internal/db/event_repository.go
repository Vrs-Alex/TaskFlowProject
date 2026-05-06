package db

import (
	"context"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
)

type EventWithTokens struct {
	EventID   int64
	EventName string
	StartDate time.Time
	Tokens    []string // все fcm токены юзера
}

type EventRepository struct {
	pool *pgxpool.Pool
}

func NewEventRepository(pool *pgxpool.Pool) *EventRepository {
	return &EventRepository{pool: pool}
}

// Находим все события которые начинаются завтра + все fcm токены их владельцев
func (r *EventRepository) FindUpcomingEvents(ctx context.Context) ([]EventWithTokens, error) {
	query := `
		SELECT 
			e.id,
			i.name,
			e.start_date,
			ARRAY_AGG(ud.fcm_token) AS tokens
		FROM event e
		JOIN item i ON i.id = e.id
		JOIN user_device ud ON ud.user_id = i.user_id
		WHERE
			i.is_deleted = FALSE
			AND DATE(e.start_date AT TIME ZONE 'UTC') = DATE((NOW() + INTERVAL '1 day') AT TIME ZONE 'UTC')
			AND ud.fcm_token IS NOT NULL
		GROUP BY e.id, i.name, e.start_date
	`

	rows, err := r.pool.Query(ctx, query)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var results []EventWithTokens
	for rows.Next() {
		var e EventWithTokens
		if err := rows.Scan(&e.EventID, &e.EventName, &e.StartDate, &e.Tokens); err != nil {
			return nil, err
		}
		results = append(results, e)
	}
	return results, nil
}
