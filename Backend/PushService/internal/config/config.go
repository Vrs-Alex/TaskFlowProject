package config

import (
	"log"
	"os"

	"github.com/joho/godotenv"
)

type Config struct {
	DatabaseURL        string
	FCMCredentialsPath string
}

func Load() *Config {
	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found")
	}
	return &Config{
		DatabaseURL:        os.Getenv("DATABASE_URL"),
		FCMCredentialsPath: os.Getenv("FCM_CREDENTIALS_PATH"),
	}
}
