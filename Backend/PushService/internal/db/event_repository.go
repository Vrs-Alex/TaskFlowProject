package db

import (
	"context"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
)

type EventWithTokens struct {
	EventID    int64
	EventName  string
	StartDate  time.Time
	DaysBefore int
	Tokens     []string
}

type EventRepository struct {
	pool *pgxpool.Pool
}

func NewEventRepository(pool *pgxpool.Pool) *EventRepository {
	return &EventRepository{pool: pool}
}

func (r *EventRepository) FindUpcomingEvents(ctx context.Context) ([]EventWithTokens, error) {
	query := `
		SELECT
			e.id,
			i.name,
			e.start_date,
			days.days_before,
			ARRAY_AGG(ud.fcm_token) AS tokens
		FROM event e
		JOIN item i ON i.id = e.id
		JOIN user_device ud ON ud.user_id = i.user_id
		JOIN (VALUES (7), (3), (1)) AS days(days_before)
			ON DATE(e.start_date AT TIME ZONE 'UTC') = DATE((NOW() + (days.days_before || ' days')::INTERVAL) AT TIME ZONE 'UTC')
		WHERE
			i.is_deleted = FALSE
			AND ud.fcm_token IS NOT NULL
			AND NOT EXISTS (
				SELECT 1 FROM push_log
				WHERE entity_type = 'EVENT'
				  AND entity_id = e.id
				  AND days_before = days.days_before
			)
		GROUP BY e.id, i.name, e.start_date, days.days_before
	`

	rows, err := r.pool.Query(ctx, query)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var results []EventWithTokens
	for rows.Next() {
		var e EventWithTokens
		if err := rows.Scan(&e.EventID, &e.EventName, &e.StartDate, &e.DaysBefore, &e.Tokens); err != nil {
			return nil, err
		}
		results = append(results, e)
	}
	return results, nil
}
