package scheduler

import (
	"context"
	"fmt"
	"log"
	"push-service/internal/db"
	"push-service/internal/fcm"
	"time"
)

type Scheduler struct {
	eventRepo *db.EventRepository
	fcm       *fcm.Client
}

func New(eventRepo *db.EventRepository, fcm *fcm.Client) *Scheduler {
	return &Scheduler{eventRepo: eventRepo, fcm: fcm}
}

func (s *Scheduler) Start(ctx context.Context) {
	log.Println("Scheduler started")
	// Сразу при старте проверяем
	s.checkEvents(ctx)

	ticker := time.NewTicker(1 * time.Hour)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			s.checkEvents(ctx)
		case <-ctx.Done():
			log.Println("Scheduler stopped")
			return
		}
	}
}

func (s *Scheduler) checkEvents(ctx context.Context) {
	log.Println("Checking upcoming events...")

	events, err := s.eventRepo.FindUpcomingEvents(ctx)
	if err != nil {
		log.Printf("Error fetching events: %v", err)
		return
	}

	log.Printf("Found %d events to notify", len(events))

	for _, event := range events {
		err := s.fcm.SendMulticast(ctx, event.Tokens,
			"Завтра: "+event.EventName,
			"Мероприятие начнётся в "+event.StartDate.Format("15:04"),
			map[string]string{
				"type":    "event",
				"eventId": fmt.Sprintf("%d", event.EventID),
			},
		)
		if err != nil {
			log.Printf("FCM error for event %d: %v", event.EventID, err)
		} else {
			log.Printf("Notified %d devices for event %d", len(event.Tokens), event.EventID)
		}
	}
}
