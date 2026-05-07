package db

import (
	"context"

	"github.com/jackc/pgx/v5/pgxpool"
)

type PushLogRepository struct {
	pool *pgxpool.Pool
}

func NewPushLogRepository(pool *pgxpool.Pool) *PushLogRepository {
	return &PushLogRepository{pool: pool}
}

func (r *PushLogRepository) MarkSent(ctx context.Context, entityType string, entityId int64, daysBefore int) error {
	_, err := r.pool.Exec(ctx,
		`INSERT INTO push_log (entity_type, entity_id, days_before) VALUES ($1, $2, $3) ON CONFLICT DO NOTHING`,
		entityType, entityId, daysBefore,
	)
	return err
}
