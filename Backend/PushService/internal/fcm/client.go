package fcm

import (
	"context"
	"log"

	firebase "firebase.google.com/go/v4"
	"firebase.google.com/go/v4/messaging"
	"google.golang.org/api/option"
)

type Client struct {
	messaging *messaging.Client
}

func New(credentialsPath string) *Client {
	opt := option.WithCredentialsFile(credentialsPath)
	app, err := firebase.NewApp(context.Background(), nil, opt)
	if err != nil {
		log.Fatalf("Failed to init Firebase: %v", err)
	}
	msgClient, err := app.Messaging(context.Background())
	if err != nil {
		log.Fatalf("Failed to init FCM: %v", err)
	}
	return &Client{messaging: msgClient}
}

// Отправка на одно устройство
func (c *Client) Send(ctx context.Context, token, title, body string, data map[string]string) error {
	_, err := c.messaging.Send(ctx, &messaging.Message{
		Token: token,
		Notification: &messaging.Notification{
			Title: title,
			Body:  body,
		},
		Data: data,
	})
	return err
}

// Отправка на несколько устройств (все девайсы юзера)
func (c *Client) SendMulticast(ctx context.Context, tokens []string, title, body string, data map[string]string) error {
	resp, err := c.messaging.SendEachForMulticast(ctx, &messaging.MulticastMessage{
		Tokens: tokens,
		Notification: &messaging.Notification{
			Title: title,
			Body:  body,
		},
		Data: data,
	})
	if err != nil {
		return err
	}
	log.Printf("FCM multicast: %d success, %d failure", resp.SuccessCount, resp.FailureCount)
	return nil
}
