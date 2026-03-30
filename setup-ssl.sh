#!/bin/bash

# SSL Setup Script for MeshGate
# This script handles the initial certificate generation for sslip.io

DOMAIN="141-148-43-8.sslip.io"
EMAIL="tijanibwebdev@gmail.com"

# Create directories first (required for Podman volume mounts)
echo "Creating certbot directories..."
mkdir -p certbot/conf certbot/www

# Detect compose command
if command -v docker-compose &> /dev/null; then
  COMPOSE_CMD="docker-compose"
elif docker compose version &> /dev/null; then
  COMPOSE_CMD="docker compose"
elif command -v podman-compose &> /dev/null; then
  COMPOSE_CMD="podman-compose"
else
  # Fallback to 'docker-compose' and hope for the best
  COMPOSE_CMD="docker-compose"
fi

echo "Using $COMPOSE_CMD"

echo "Stopping any existing web servers on port 80..."
$COMPOSE_CMD -f docker-compose-production.yml stop nginx certbot

echo "Generating SSL certificate for $DOMAIN..."
# Use :Z for SELinux labeled volumes (required on Oracle Linux/Podman)
docker run --rm \
  -v "$(pwd)/certbot/conf:/etc/letsencrypt:Z" \
  -v "$(pwd)/certbot/www:/var/www/certbot:Z" \
  -p 80:80 \
  certbot/certbot certonly --standalone \
  -d $DOMAIN --non-interactive --agree-tos -m $EMAIL

echo "Starting MeshGate services with HTTPS..."
$COMPOSE_CMD -f docker-compose-production.yml up -d
