package main

import (
	"context"
	"log"
	"os/signal"
	"push-service/internal/config"
	"push-service/internal/db"
	"push-service/internal/fcm"
	"push-service/internal/scheduler"
	"syscall"
)

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGTERM)
	defer stop()

	cfg := config.Load()

	pool := db.Connect(ctx, cfg.DatabaseURL)
	defer pool.Close()

	fcmClient := fcm.New(cfg.FCMCredentialsPath)
	eventRepo := db.NewEventRepository(pool)
	pushLogRepo := db.NewPushLogRepository(pool)
	sched := scheduler.New(eventRepo, pushLogRepo, fcmClient)

	sched.Start(ctx)
	log.Println("Push service stopped")
}
