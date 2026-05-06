FROM golang:1.26.2-alpine AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN go build -o push-service ./cmd/main.go

FROM alpine:3.19
WORKDIR /app
COPY --from=builder /app/push-service .
COPY service-account.json .
CMD ["./push-service"]