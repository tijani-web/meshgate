#!/bin/bash

# SSL Setup Script for MeshGate
# This script handles the initial certificate generation for sslip.io

DOMAIN="141-148-43-8.sslip.io"
EMAIL="your-email@example.com" # You can change this or leave it

echo "Stopping any existing web servers on port 80..."
docker compose -f docker-compose-production.yml stop nginx certbot

echo "Generating SSL certificate for $DOMAIN..."
docker run --rm -it \
  -v "$(pwd)/certbot/conf:/etc/letsencrypt" \
  -v "$(pwd)/certbot/www:/var/www/certbot" \
  -p 80:80 \
  certbot/certbot certonly --standalone \
  -d $DOMAIN --non-interactive --agree-tos -m $EMAIL

echo "Starting MeshGate services with HTTPS..."
docker compose -f docker-compose-production.yml up -d
