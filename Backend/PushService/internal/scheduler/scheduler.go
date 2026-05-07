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
	eventRepo   *db.EventRepository
	pushLogRepo *db.PushLogRepository
	fcm         *fcm.Client
}

func New(eventRepo *db.EventRepository, pushLogRepo *db.PushLogRepository, fcm *fcm.Client) *Scheduler {
	return &Scheduler{eventRepo: eventRepo, pushLogRepo: pushLogRepo, fcm: fcm}
}

func (s *Scheduler) Start(ctx context.Context) {
	log.Println("Scheduler started")
	s.checkEvents(ctx)

	for {
		now := time.Now().UTC()
		next := time.Date(now.Year(), now.Month(), now.Day(), 19, 0, 0, 0, time.UTC)
		if !now.Before(next) {
			next = next.Add(24 * time.Hour)
		}

		log.Printf("Next check scheduled at %s", next.Format("2006-01-02 19:00 UTC"))

		select {
		case <-time.After(time.Until(next)):
			s.checkEvents(ctx)
		case <-ctx.Done():
			log.Println("Scheduler stopped")
			return
		}
	}
}

func eventTitle(daysBefore int, name string) string {
	switch daysBefore {
	case 1:
		return "Завтра: " + name
	case 3:
		return "Через 3 дня: " + name
	default:
		return fmt.Sprintf("Через %d дней: %s", daysBefore, name)
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
		title := eventTitle(event.DaysBefore, event.EventName)
		body := "Мероприятие начнётся в " + event.StartDate.Format("15:04")

		err := s.fcm.SendMulticast(ctx, event.Tokens, title, body,
			map[string]string{
				"type":    "event",
				"eventId": fmt.Sprintf("%d", event.EventID),
			},
		)
		if err != nil {
			log.Printf("FCM error for event %d (-%dd): %v", event.EventID, event.DaysBefore, err)
			continue
		}

		if err := s.pushLogRepo.MarkSent(ctx, "EVENT", event.EventID, event.DaysBefore); err != nil {
			log.Printf("Failed to mark event %d (-%dd) as sent: %v", event.EventID, event.DaysBefore, err)
		} else {
			log.Printf("Notified %d devices for event %d (-%dd)", len(event.Tokens), event.EventID, event.DaysBefore)
		}
	}
}
